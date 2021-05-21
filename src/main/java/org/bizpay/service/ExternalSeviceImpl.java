package org.bizpay.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;

import org.bizpay.common.domain.DelngAdiParam;
import org.bizpay.common.domain.DelngCredtParam;
import org.bizpay.common.domain.DelngParam;
import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.common.util.EncryptUtil;
import org.bizpay.common.util.KSPayMsgBean;
import org.bizpay.common.util.StringUtils;
import org.bizpay.exception.ExorderException;
import org.bizpay.exception.SqlErrorException;
import org.bizpay.mapper.ExternalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.java.Log;
@Log
@Service
public class ExternalSeviceImpl implements ExternalService {
	public static final String KSNET_PG_IP = "210.181.28.137";//"210.181.28.137";	//-필수- ipaddr X(15)   *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)
	public static final int KSNET_PG_PORT = 21001;				//-필수- port 9( 5)    	*KSNET_PORT(21001)
	public static final String STORE_ID = "2999199999"; // 2999199999 -- 자동취소  상점아이디 // 익일은 스와이프 D+2를 2041700460   
	@Autowired
	KSPayMsgBean ksBean;
	
	@Autowired
	ExternalMapper exMapper;
	
	@Autowired
	StringUtils sUtil;
	
	@Autowired
	CertUtil cUtil;
	
	@Autowired
	EncryptUtil eUtil;
	
	// 운영배포시 주석 해제있음
	@Override
	@Transactional
	public String insertExOrder(ExternalOrderInputParam param) throws Exception {
		log.info("외부결제연동 결제정보 부분만 입력 delng 아니다");
		// 1. 요청한 값이 정상적인지 검사
		if("".equals(param.getNextUrl())) {
			return "A002";
		}
		if("".equals(param.getNotiUrl())) {
			return "A003";
		}
		if("".equals(param.getExorderNo())) {
			return "A004";
		}
		if("".equals(param.getMberId())) {
			return "A005";
		}
		if("".equals(param.getOrderName()   ) ) {
			return "A006";
		}
		if(param.getOrderPrice() <100 ) {
			return "A007";
		}
		if("".equals(param.getPkHash() ) ) {
			return "A008";
		}
		
		// 해시 키값 확인 -- 운영배포시 주석 해제
	//	String temp = param.getMberId()+param.getOrderName()+param.getOrderPrice()+param.getExorderNo();
	//	temp = eUtil.encryptSHA256(temp);
	//	if(!temp.equals(  param.getPkHash()  )  ) {
	//		return -2;
	//	}
				
		// 2. 이전 호출한 외부 주문상태 확인 - biz 의 내용아님
		ExternalOrderInputParam reP = exMapper.selectExOrderNo(param);
		if(reP != null) {
			if("0000".equals(reP.getStatus() ) ) {
				log.info("이전 주문만 완료후 후속 결제가 안된 경우");
				return String.valueOf(reP.getSeq()) ;
			}else {
				log.info("이전 주문관련 결제등이 발생해버린 경우");
				return "S001"; // !코드추가후 수정
			}
		}
		
		// 3. 정상적인 사용자인지 확인
		if( exMapper.selectMberCnt(param.getMberId())  <1 ) {
			return "A014";
			//throw new SqlErrorException("서비스 이용제한 사용자입니다.");
		}
		
		// 4. 결제 내용입력
		if(exMapper.insertExOrder(param) <1) {
			return "S001";
			//throw new SqlErrorException("결제주문서버에 문제가 있습니다.");
		}
		
		return String.valueOf(param.getSeq());
	}

	@Override
	public ExternalOrderInputParam selectOrderInfo(long orderNo) throws Exception {
		log.info("외부결제연동 결제정보확인");
		return exMapper.selectOrderInfo(orderNo);
	}

	@Override
	@Transactional
	public boolean payRequest(PaymentReqParam param) throws Exception {
		log.info("외부 수기결제 요청");
		
		// 이미 결제된 정보 인지 확인
		if(param.getExorderNo() == null) {
			throw new SqlErrorException("결제정보 누락.");
		}
		if( param.getExorderNo().trim().length() <1) {
			throw new SqlErrorException("결제정보 누락.");
		}
		
		ExternalOrderInputParam tparam = new ExternalOrderInputParam();
		tparam.setMberId(param.getMemberId());
		tparam.setExorderNo( param.getExorderNo() );
		ExternalOrderInputParam tparam1 = exMapper.selectExOrderNo(tparam);
		if(tparam1== null ) {
			throw new SqlErrorException("결제정보확인");
		}
		if("AAAA".equals(tparam1.getStatus() )) {
			throw new SqlErrorException("이미 결제완료된 주문입니다.");
		}
		if("C001".equals(tparam1.getStatus() )) {
			throw new SqlErrorException("이미 결제취소된 주문입니다.");
		}
		String storeId = "";
		int mberCode = -1;
		String mberCodeSn = "000";
		// 사용가능 유저인지 판단
		HashMap<String, Object> mberChk =exMapper.selectTbBberIdCheck(param.getMemberId());  
		if(mberChk == null) {
			throw new SqlErrorException("결제처리 실패. 미등록 사업자 입니다.");
		}else {
			if( !"Y".equals(mberChk.get("USEAT"))  ) {
				throw new SqlErrorException("결제처리 실패. 결제요청 불가능한 사업자 입니다.");
			}
			mberCode =  Integer.parseInt(mberChk.get("MBERCODE").toString());
		}
		
		//tbMberDetail mber_code_sn 테이터 추출
		mberCodeSn = exMapper.selectTbMberDetailSn(mberCode);
		if(mberCodeSn == null || "".equals(mberCodeSn)) {
			throw new SqlErrorException("결제처리 실패. 결제 미처리 사업자 입니다.");
		}
		// tbMberBasis
		HashMap<String, Object> tbMberBasis =  exMapper.selectTbMberBasis1(mberCode);
					
		if(tbMberBasis == null) {
			throw new SqlErrorException("결제처리 실패. 결제 상점설정에 문제가 있습니다.");
		}else{
			if("T".equals(tbMberBasis.get("PAYTYPE")) ) {
				storeId =  "2041700460";
			}else if("B".equals(tbMberBasis.get("PAYTYPE")) ) {
				storeId =  "2002307048";
			}else if("N".equals(tbMberBasis.get("PAYTYPE")) ) {
				storeId =  "2552500002";
			}else {
				throw new SqlErrorException("결제처리 실패. 결제 상점설정에 문제가 있습니다.");
			}
		}
		
		// 영수증번호 획득
		HashMap<String , Object> tempMap = new HashMap<>();
		tempMap.put("mber_code",String.valueOf(mberCode) );
		tempMap.put("rcipt_no","" );
		exMapper.propRciptNO(tempMap);
		param.setRciptNo(tempMap.get("rcipt_no").toString());
		tempMap.clear();
		param.setKsnetRcipt(param.getMemberId()+"_"+param.getRciptNo());
		
		// 결제요청 관련 정보 세팅
		// 주민번호 13개로 세팅 sUtil
		param.setPidNum( sUtil.rightPad( param.getPidNum() , " " , 13  ) ); 
		// 카드번호 세팅  *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM]) 
		param.setPTrackII( param.getCardNo() + "=" + param.getExpiration() );
		
		// 
		
		// 카드결제 실행
		Hashtable<String, Object> ht = new Hashtable<>();
		ht = ksBean.sendCardMsg(
				KSNET_PG_IP, 
				KSNET_PG_PORT, 
				storeId,
				//"2999199999", // 테스트이후 해제함
				param.getKsnetRcipt(),  // 주문번호    
				"", 
				param.getPidNum(), 
				param.getEmail(), 
				param.getGoodsName(), // 상품명 
				param.getPhoneNumber(), // 핸드폰번호 
				"K",						// pKeyInType X(12) KEY-IN유형(K:직접입력,S:리더기사용입력)  
				"1",						// pInterestType   X( 1)   *일반/무이자구분 1:일반 2:무이자
				param.getPTrackII(),					 // -필수- pTrackII  X(40)   *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM]) 
				param.getInstallment(), 
				Long.toString(param.getAmount())  , 
				param.getPasswd(), 
				param.getCardPNo());
		
		// 카드결제 성공판단
		if(ht == null) {
			throw new SqlErrorException("결제처리 실패. 관리자에게 문의바랍니다.");
		}
		
		// 성공이후 // 데이터 저장
		String ox = sUtil.getString(ht.get("Status") ).trim();
		if("O".equals(ox)) {
			DelngParam delngParam = new DelngParam();
			// delng
			delngParam.setMberCode( mberCode);
			delngParam.setMberCodeSn( mberCodeSn  );
			delngParam.setRciptNo(param.getRciptNo());
			delngParam.setAppCode("BIZPAY" );
			delngParam.setConfmNo(sUtil.getString(ht.get("AuthNo")).trim());
			delngParam.setConfmDt(sUtil.getString(ht.get("TradeDate")).trim() );
			delngParam.setConfmTime(sUtil.getString(ht.get("TradeTime")).trim() );
			delngParam.setSplpc(  param.getAmount());
			delngParam.setVat(0 ); // 부가세 0 일단은
			delngParam.setTrgetMberCode( mberCode );
			delngParam.setTrgetMberCodeSn(mberCodeSn);
			delngParam.setTrgetRciptNo(param.getRciptNo());
			delngParam.setApprovalConfirm("O");
			delngParam.setDeviceSeqNo(1);
			delngParam.setVanCode("VAN");
			delngParam.setDelngSeCode("CARD_ISSUE");
			delngParam.setGoodNm(param.getGoodsName() );
			delngParam.setDelngPayType( tbMberBasis.get("PAYTYPE").toString() );
			
			if(exMapper.insertDelng(delngParam) <1) {
				throw new SqlErrorException("매출처리 실패. 관리자에게 문의바랍니다.");
			}
			
			// delngCredt
			DelngCredtParam delngCredtParam = new DelngCredtParam();
			delngCredtParam.setMberCode(mberCode);
			delngCredtParam.setMberCodeSn(mberCodeSn);
			delngCredtParam.setRciptNo(param.getRciptNo());
			delngCredtParam.setCardNo( cUtil.encrypt(param.getCardNo()) );
			delngCredtParam.setInstlmtMonth(param.getInstallment());
			delngCredtParam.setIssueCmpnyCode(sUtil.getString(ht.get("IssCode")).trim());
			delngCredtParam.setIssueCmpnyNm(sUtil.getString(ht.get("Message1")).trim());
			delngCredtParam.setPuchasCmpnyCode(sUtil.getString(ht.get("AquCode")).trim());
			delngCredtParam.setPuchasCmpnyNm(sUtil.getString(ht.get("Message1")).trim());
			delngCredtParam.setCdrsrNo(sUtil.getString(ht.get("MerchantNo")).trim());
			delngCredtParam.setCardType("D"); // 확인해보자!
			delngCredtParam.setPgVanGb("P"); // 확인해보자!
			delngCredtParam.setTId(storeId);
			delngCredtParam.setPgRciptNo(sUtil.getString(ht.get("TransactionNo")).trim());////pg거래번호
			delngCredtParam.setGbInfo("U"); // 유니코아
			delngCredtParam.setVanPgComp("PG_KSNET");
			
			if(exMapper.insertDelngCredt(delngCredtParam) < 1) {
				throw new SqlErrorException("카드매출 실패. 관리자에게 문의바랍니다.");
			}
			
			// delng_adi
			DelngAdiParam delngAdiParam = new DelngAdiParam();
			delngAdiParam.setMberCode(mberCode);
			delngAdiParam.setMberCodeSn(mberCodeSn);
			delngAdiParam.setRciptNo(param.getRciptNo());
			delngAdiParam.setAdiCode("PURCHSR_MBTLNUM");
			if(param.getPhoneNumber()!= null && !"".equals(param.getPhoneNumber())) {
				delngAdiParam.setAdiCn( cUtil.encrypt( param.getPhoneNumber() )  );				
			}else {
				delngAdiParam.setAdiCn( "722211d65862dac6ab81668e0544b4e3" );				// 핸드폰번호 없으면 공백값이다.				
			}
			if(exMapper.insertDelngAdi(delngAdiParam) <1) {
				throw new SqlErrorException("매출 정보등록 실패. 관리자에게 문의바랍니다.");
			}
			
			// 결제정보 업데이트
			ExternalOrderInputParam exParam = new ExternalOrderInputParam();
			exParam.setStatus("AAAA"); // 결제완료
			exParam.setMberId(param.getMemberId());
			exParam.setExorderNo( param.getExorderNo() );
			exParam.setConfmNo(delngParam.getConfmNo());
			exParam.setRciptNo(param.getRciptNo());
			exMapper.updateExOrder(exParam);
			
		}else {
			throw new SqlErrorException("결제승인 실패. 관리자에게 문의바랍니다.");
		}
		return true;
	}

	@Override
	@Transactional
	public void payCancel(ExternalOrderInputParam param) throws Exception {
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		// 1. 외부 입력 결제 내역확인
		ExternalOrderInputParam exInfo = exMapper.selectOrderInfo2(param);

		if(exInfo == null) {
			throw new ExorderException("A001");
		}
		if("C001".equals(exInfo.getStatus() ) ) {
			throw new ExorderException("C001");
		}
		if("0000".equals(exInfo.getStatus() ) ) {
			throw new ExorderException("0000");
		}
		
		// 2. 유니코아 주문 정보 확인
		DelngParam dParam = new DelngParam();
		// 추후 결제수단 선택 하게 되면 수정되어 야 할곳!!!!!!!!!!
		// 카드 취소 여부 확인
		dParam.setDelngSeCode("CARD_CNCL");
		dParam.setConfmNo(exInfo.getConfmNo());
		dParam.setMberCode(exInfo.getMberCode());
		dParam.setRciptNo(exInfo.getRciptNo());
		DelngParam delngInfo = exMapper.selectDelngInfo(dParam);

		// 취소정보 보유 이미 취소된경우
		if(delngInfo != null) {
			throw new ExorderException("C001");
		}
		// 결제 정보가 있는지 확인
		dParam.setDelngSeCode("CARD_ISSUE");
		delngInfo = exMapper.selectDelngInfo(dParam);
		// 결제 정보가 없는경우 
		if(delngInfo == null) {
			throw new ExorderException("A001");
		}
		dParam.setMberCodeSn( delngInfo.getMberCodeSn() );
		// 카드 결제 정보 추출
		DelngCredtParam delngCredtInfo = new DelngCredtParam();
		// dto 가 너무 많아 지므로 map 로 변경하거나 결과 dto 를 파라미터로 사용하자!!!!!!
		map1.clear();
		map1.put("mberCode", delngInfo.getMberCode());
		map1.put("rciptNo", delngInfo.getRciptNo());
		
		delngCredtInfo = exMapper.selectDelngCredt(map1);
		// 카드 결제정보가 없는경우
		if(delngCredtInfo == null ) {
			throw new ExorderException("A011");
		}
		
		//  실제 취소 처리부분 ksnet
		Hashtable xht = ksBean.sendCardCancelMsg(
				KSNET_PG_IP, 				// -필수- ipaddr  X(15)   *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)  
				KSNET_PG_PORT,			// -필수- port   9( 5)   *KSNET_PORT(21001)  
				delngCredtInfo.getTId(), 								// -필수- pStoreId  X(10)   *상점아이디(개발:2999199999, 운영:?)  
				"K", 							// -필수- pKeyInType   X(12)  KEY-IN유형(K:직접입력,S:리더기사용입력)  
				delngCredtInfo.getPgRciptNo()							// -필수- pTransactionNo  X( 1)  *거래번호(승인응답시의 KEY:1로시작되는 12자리숫자)  
			);
		// ksnet 결과 처리가 없는경우
		if(xht == null ) {
			throw new ExorderException("C007");
		}
		
//		Hashtable xht = new Hashtable<>();
//		xht.put("Status", "O");
		// 정상적으로 취소 처리가 된경우
		if( "O".equals(sUtil.getString(xht.get(  "Status" ) ) )){
			// 영수증번호 획득
			HashMap<String , Object> tempMap = new HashMap<>();
			tempMap.put("mber_code",String.valueOf(exInfo.getMberCode()) );
			tempMap.put("rcipt_no","" );
			exMapper.propRciptNO(tempMap); // 새로얻어온 영수증번호 여기에 tempMap -- 프로시저 실행이므로 프로시저실행파라미터값에 그대로 변경된 값이 담겨온다!
			
			//취소 요청한 날짜로 취소 일자 기록
			delngInfo.setCardDeleteYn("Y");
			delngInfo.setBigo("연동결제취소");
			delngInfo.setMberId( param.getMberId());
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");
			String cancelDt = format1.format (System.currentTimeMillis());
			dParam.setCancelDt(cancelDt);
			delngInfo.setDelngSeCode("CARD_CNCL");
			delngInfo.setConfmDt(cancelDt);
			delngInfo.setConfmTime("000000");
			delngInfo.setCancelDt(cancelDt);
			delngInfo.setConfmTime("000000");
			delngInfo.setSplpc( delngInfo.getSplpc()*-1 );
			delngInfo.setVat( delngInfo.getVat()*-1 );

				
			if("".equals(param.getBigo() ) || param.getBigo()==null  ){
				delngInfo.setBigo("결제취소요청");
			}else {
				delngInfo.setBigo( param.getBigo());
			}
			
			param.setStatus("C001");
			// 1. 외부 결제정보 수정
			if(exMapper.updateExOrder(param) <1) {
				throw new ExorderException("C002");
			}
			// 2. dlng 결제정보 수정
			if(exMapper.updateDelng(delngInfo) <1) {
				throw new ExorderException("C003");
			}
			delngInfo.setRciptNo( tempMap.get("rcipt_no").toString() );
			// 3. dlng 결제정보 취소정보입력
			if(exMapper.insertDelng(delngInfo) <1) {
				throw new ExorderException("C003");
			}
			
			// 4. delng credt 결제 취소정보 입력
			// 카드 결제 정보 추출			
			delngCredtInfo = exMapper.selectDelngCredt(map1);
			delngCredtInfo.setRciptNo(tempMap.get("rcipt_no").toString() );

			if(exMapper.insertDelngCredt(delngCredtInfo) < 1) {
				throw new ExorderException("C004");
			}
			
			// delng_adi - 부가정보
			DelngAdiParam delngAdiParam = new DelngAdiParam();
			delngAdiParam.setMberCode(dParam.getMberCode());
			delngAdiParam.setMberCodeSn(dParam.getMberCodeSn());
			delngAdiParam.setRciptNo(exInfo.getRciptNo() );
			DelngAdiParam delngAdiParam2 = new DelngAdiParam();
			delngAdiParam2 = exMapper.selectDelngAdi(delngAdiParam);
			delngAdiParam2.setRciptNo(tempMap.get("rcipt_no").toString());
					
			if(exMapper.insertDelngAdi(delngAdiParam2) <1) {
				throw new ExorderException("9999");
			}
		} else {
			throw new ExorderException("9999");
		}
	}

}
