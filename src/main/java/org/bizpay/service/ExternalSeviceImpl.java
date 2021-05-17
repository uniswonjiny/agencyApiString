package org.bizpay.service;

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
import org.bizpay.domain.ExternalOrderInfo;
import org.bizpay.exception.SqlErrorException;
import org.bizpay.mapper.ExternalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

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
	
	
	@Override
	@Transactional
	public long insertExOrder(ExternalOrderInputParam param) throws Exception {
		log.info("외부결제연동 결제정보 입력");
		// 필수 값확인
		if("".equals(param.getNextUrl())) {
			return -1;
		}
		if("".equals(param.getNotiUrl())) {
			return -1;
		}
		if("".equals(param.getExorderNo())) {
			return -1;
		}
		if("".equals(param.getMberId())) {
			return -1;
		}
		if("".equals(param.getOrderName()   ) ) {
			return -1;
		}
		if(param.getOrderPrice() <100 ) {
			return -1;
		}
		if("".equals(param.getPkHash() ) ) {
			return -1;
		}
		
		// 해시 키값 확인
		String temp = param.getMberId()+param.getOrderName()+param.getOrderPrice()+param.getExorderNo();
		temp = eUtil.encryptSHA256(temp);
		if(!temp.equals(  param.getPkHash()  )  ) {
			return -2;
		}
		
		// 이전 주문이 정보가 있는지 확인
		ExternalOrderInputParam reP = exMapper.selectExOrderNo(param);
		if(reP != null) {
			if("0000".equals(reP.getStatus() ) ) {
				log.info("이전 주문만 완료후 후속 결제가 안된 경우");
				return reP.getSeq();
			}else {
				log.info("이전 주문관련 결제등이 발생해버린 경우");
				return -1;
			}
		}
		
	
		
		
		// 사용가능한 사용자인지 확인
		if( exMapper.selectMberCnt(param.getMberId())  <1 ) {
			throw new SqlErrorException("서비스 이용제한 사용자입니다.");
		}
		
			
		if(exMapper.insertExOrder(param) <1) {
			throw new SqlErrorException("결제주문서버에 문제가 있습니다.");
		}
		return param.getSeq();
	}

	@Override
	public ExternalOrderInfo selectOrderInfo(long orderNo) throws Exception {
		log.info("외부결제연동 결제정보확인");
		return exMapper.selectOrderInfo(orderNo);
	}

	@Override
	@Transactional
	public boolean payRequest(PaymentReqParam param) throws Exception {
		log.info("외부 수기결제 요청");
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
				//storeId,
				"2999199999", // 테스트이후 해제함
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
			delngParam.setAppCode(param.getMemberId());
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
				delngAdiParam.setAdiCn( "722211d65862dac6ab81668e0544b4e3" );								
			}
			if(exMapper.insertDelngAdi(delngAdiParam) <1) {
				throw new SqlErrorException("매출 정보등록 실패. 관리자에게 문의바랍니다.");
			}
			
			// 결제정보 업데이트
			ExternalOrderInputParam exParam = new ExternalOrderInputParam();
			exParam.setStatus("AAAA");
			exParam.setMberId(param.getMemberId());
			exParam.setExorderNo( param.getExOrderNo() );
			exMapper.updateExOrder(exParam);
			
		}else {
			throw new SqlErrorException("결제승인 실패. 관리자에게 문의바랍니다.");
		}
		return true;
	}

}
