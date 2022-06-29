package org.bizpay.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.bizpay.common.domain.DelngAdiParam;
import org.bizpay.common.domain.DelngCredtParam;
import org.bizpay.common.domain.DelngParam;
import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.TblAtmParam;
import org.bizpay.common.domain.external.OrderStatusInfo;
import org.bizpay.common.util.CertUtil;
import org.bizpay.common.util.EncryptUtil;
import org.bizpay.common.util.KSPayMsgBean;
import org.bizpay.common.util.PayCkeck;
import org.bizpay.common.util.StringUtils;
import org.bizpay.domain.LimitInfo;
import org.bizpay.domain.MemberInfo;
import org.bizpay.domain.OrderErrorType;
import org.bizpay.domain.link.Destination;
import org.bizpay.domain.link.LinkSms;
import org.bizpay.domain.link.PaymentInfo;
import org.bizpay.domain.link.GoodsInfo;
import org.bizpay.domain.link.SellerInfo;
import org.bizpay.domain.link.SmsCardPayment;
import org.bizpay.domain.link.SmsInsert;
import org.bizpay.domain.link.SmsLink;
import org.bizpay.domain.link.SmsPayRequest;
import org.bizpay.exception.ExorderException;
import org.bizpay.mapper.AccountMapper;
import org.bizpay.mapper.AuthMapper;
import org.bizpay.mapper.ExternalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalSeviceImpl implements ExternalService {
	public static final String KSNET_PG_IP = "210.181.28.137";// "210.181.28.137"; //-필수- ipaddr X(15)
																// *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)
	public static final int KSNET_PG_PORT = 21001; // -필수- port 9( 5) *KSNET_PORT(21001)
	@Autowired
	KSPayMsgBean ksBean;

	@Autowired
	ExternalMapper exMapper;

	@Autowired
	AccountMapper acMapper;

	@Autowired
	AuthMapper auMapper;

	@Autowired
	StringUtils sUtil;

	@Autowired
	CertUtil cUtil;

	@Autowired
	EncryptUtil eUtil;
	
	@Autowired
	PayCkeck pCheck;

	// 운영배포시 주석 해제있음
	@Override
	@Transactional
	public String insertExOrder(ExternalOrderInputParam param) throws Exception {
		log.info("외부결제연동 결제정보 부분만 입력 delng 아니다");
		// 금액이 숫자인지 확인
		if (!param.getOrderPrice().matches("[+-]?\\d*(\\.\\d+)?")) {
			log.error(" insertExOrder 금액이 숫자가 아닙니다. - " + param.toString());
			return "A007";
		}

		// 1. 요청한 값이 정상적인지 검사
		if ("".equals(param.getNextUrl())) {
			log.error(" insertExOrder 요청값이정상이 아닙니다.. - " + param.toString());
			return "A002";
		}
		if ("".equals(param.getNotiUrl())) {
			log.error(" insertExOrder noti 주소 없음 - " + param.toString());
			return "A003";
		}
		if ("".equals(param.getExorderNo())) {
			log.error(" insertExOrder 외부 주문번호 없음 - " + param.toString());
			return "A004";
		}
		if ("".equals(param.getMberId())) {
			log.error(" insertExOrder 사용자아이디 없음 - " + param.toString());
			return "A005";
		}
		if ("".equals(param.getOrderName())) {
			log.error(" insertExOrder 주문명 없음 - " + param.toString());
			return "A006";
		}
		if (Long.valueOf(param.getOrderPrice()) < 1000) {
			log.error(" insertExOrder 최소 금액 오류 - " + param.toString());
			return "A007";
		}
		if ("".equals(param.getPkHash())) {
			log.error(" insertExOrder 해시값 없음 - " + param.toString());
			return "A008";
		}

		// 해시 키값 확인 -- 운영배포시 주석 해제
		if (param.getPkHash() == null || param.getPkHash().length() < 1) {
			log.error(" insertExOrder 해시값 없음 - " + param.toString());
			return "A008";
		}
		String temp = param.getMberId() + param.getOrderName() + param.getOrderPrice() + param.getExorderNo()
				+ "unicore";
		String hashkey = eUtil.encryptSHA256(temp);

		if (!hashkey.equals(param.getPkHash())) {
			log.error(" insertExOrder 해시값 불일치 - " + param.toString());
			return "A009";
		}

		// 2. 이전 호출한 외부 주문상태 확인 - biz 의 내용아님
		ExternalOrderInputParam reP = exMapper.selectExOrderNo(param);
		if (reP != null) {
			if ("0000".equals(reP.getStatus())) {
				log.info("이전 주문만 완료후 후속 결제가 안된 경우");
				return String.valueOf(reP.getSeq());
			} else {
				log.error(" insertExOrder 해시값 불일치 - " + param.toString());
				return "S001"; // !코드추가후 수정
			}
		}

		// 3. 정상적인 사용자인지 확인
		MemberInfo mberInfo = auMapper.userInfo(param.getMberId());
		if (mberInfo == null) {
			return "A014";
		}
		if (!"Y".equals(mberInfo.getUseAt())) {
			return "A014";
		}

		// 4. 사용자의 결제 금액등을 확인한다.
		LimitInfo limiInfo = acMapper.limitInfo(mberInfo.getMberCode());
		// 4-1 1회 제한 금액오류
		if (limiInfo.getLimitOne() < Long.valueOf(param.getOrderPrice())) {
			return "L001";// 1회 결제금액 제한
		}
		// 4-2 1일 제한 금액
		if (limiInfo.getLimitDay() < (Long.valueOf(param.getOrderPrice()) + limiInfo.getSumDay())) {
			return "L002";// 1일 결제금액 제한
		}
		// 4-2 1달 제한 금액
		if (limiInfo.getLimitMonth() < (Long.valueOf(param.getOrderPrice()) + limiInfo.getSumMonth())) {
			return "L003";// 1달 결제금액 제한
		}
		// 4-3 1년 제한 금액
		if (limiInfo.getLimitYear() < (Long.valueOf(param.getOrderPrice()) + limiInfo.getSumYear())) {
			return "L004";// 1년 결제금액 제한
		}
		// 5. 결제 내용입력
		if (exMapper.insertExOrder(param) < 1) {
			return "S001";
			// throw new SqlErrorException("결제주문서버에 문제가 있습니다.");
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
	public ExternalOrderInputParam payRequest(PaymentReqParam param) throws Exception {
		log.info("외부 수기결제 요청");
		String storeId = "";
		long mberCode = -1;
		String mberCodeSn = "000";
		OrderErrorType oet = new OrderErrorType();
		oet.setKSNET_PG_IP(KSNET_PG_IP);
		oet.setKSNET_PG_PORT(KSNET_PG_PORT);
		oet.setPKeyInType("K");
		try {
			// 카드결제 실행
			Hashtable<String, Object> ht = new Hashtable<>();
			// 이미 결제된 정보 인지 확인
			if (param.getExorderNo() == null) {
				throw new ExorderException("9002");
			}
			if (param.getExorderNo().trim().length() < 1) {
				throw new ExorderException("9002");
			}

			ExternalOrderInputParam tparam = new ExternalOrderInputParam();
			tparam.setMberId(param.getMemberId());
			tparam.setExorderNo(param.getExorderNo());
			ExternalOrderInputParam tparam1 = exMapper.selectExOrderNo(tparam);
			if (tparam1 == null) {
				throw new ExorderException("9002");
			}
			if ("AAAA".equals(tparam1.getStatus())) {
				throw new ExorderException("1010"); // 이미결제된주문
			}
			if ("C001".equals(tparam1.getStatus())) {
				throw new ExorderException("1010"); // 이미 결제취소된 주문입니다
			}

			// 사용가능 유저인지 판단
			HashMap<String, Object> mberChk = exMapper.selectTbBberIdCheck(param.getMemberId());
			if (mberChk == null) {
				throw new ExorderException("1010"); // 결제처리 실패. 미등록 사업자 입니다
			} else {
				if (!"Y".equals(mberChk.get("USEAT"))) {
					throw new ExorderException("1010"); // 결제처리 실패. 결제요청 불가능한 사업자 입니다
				}
				mberCode = Long. parseLong(mberChk.get("MBERCODE").toString());
			}

			// tbMberDetail mber_code_sn 테이터 추출
			mberCodeSn = exMapper.selectTbMberDetailSn(mberCode);
			if (mberCodeSn == null || "".equals(mberCodeSn)) {
				throw new ExorderException("1010"); // 결제처리 실패. 결제 미처리 사업자 입니다
			}

			// tbMberBasis
			HashMap<String, Object> tbMberBasis = exMapper.selectTbMberBasis1(mberCode);

			if (tbMberBasis == null) {
				throw new ExorderException("1010"); // 결제처리 실패. 결제 상점설정에 문제가 있습니다
			} else {
				if ("T".equals(tbMberBasis.get("PAY_TYPE"))) {
					storeId = "2041700460";
				} else if ("B".equals(tbMberBasis.get("PAY_TYPE"))) {
					storeId = "2002307048";
				} else if ("N".equals(tbMberBasis.get("PAY_TYPE"))) {
					storeId = "2552500002";
				} else {
					throw new ExorderException("1010"); // 결제처리 실패. 결제 상점설정에 문제가 있습니다
				}
			}
			oet.setPStoreId(storeId);

			// 영수증번호 획득
			HashMap<String, Object> tempMap = new HashMap<>();
			tempMap.put("mber_code", String.valueOf(mberCode));
			tempMap.put("rcipt_no", "");
			exMapper.propRciptNO(tempMap);
			param.setRciptNo(tempMap.get("rcipt_no").toString());
			tparam1.setRciptNo(tempMap.get("rcipt_no").toString());

			tempMap.clear();
			param.setKsnetRcipt(param.getMemberId() + "_" + param.getRciptNo());

			// 결제요청 관련 정보 세팅
			// 주민번호 13개로 세팅 sUtil
			param.setPidNum(sUtil.rightPad(param.getPidNum(), " ", 13));
			// 카드번호 세팅 *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM])
			param.setPTrackII(param.getCardNo() + "=" + param.getExpiration());

			ht = ksBean.sendCardMsg(
					KSNET_PG_IP, 
					KSNET_PG_PORT, 
					storeId,
					// "2999199999", // 테스트이후 해제함
					param.getKsnetRcipt(), // 주문번호
					"", 
					param.getPidNum(), 
					param.getEmail(), 
					param.getOrderName(), // 상품명
					param.getPhoneNumber(), // 핸드폰번호
					"K", // pKeyInType X(12) KEY-IN유형(K:직접입력,S:리더기사용입력)
					"1", // pInterestType X( 1) *일반/무이자구분 1:일반 2:무이자
					param.getPTrackII(), // -필수- pTrackII X(40) *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM])
					param.getInstallment(), 
					Long.toString(param.getAmount()), 
					param.getPasswd(), 
					param.getCardPNo());

			// 카드결제 성공판단
			if (ht == null) {
				throw new ExorderException("9999"); // 결제처리 실패. 관리자에게 문의바랍니다
			}
			DecimalFormat df = new DecimalFormat("##0");
			// 성공이후 // 데이터 저장
			String ox = sUtil.getString(ht.get("Status")).trim();
			oet.setPTransactionNo(sUtil.getString(ht.get("TransactionNo")).trim());
			// 에러시 결제 취소 되는지 확인용
//			if("O".equals(ox)) {
//				throw new Exception();
//			}
			if ("O".equals(ox)) {

				DelngParam delngParam = new DelngParam();
				// delng
				delngParam.setMberCode(mberCode);
				delngParam.setMberCodeSn(mberCodeSn);
				delngParam.setRciptNo(param.getRciptNo());
				// delngParam.setAppCode("BIZPAY" );
				delngParam.setAppCode("WEB001");
				delngParam.setConfmNo(sUtil.getString(ht.get("AuthNo")).trim());
				delngParam.setConfmDt(sUtil.getString(ht.get("TradeDate")).trim());
				delngParam.setConfmTime(sUtil.getString(ht.get("TradeTime")).trim());
				delngParam.setSplpc(param.getAmount());
				delngParam.setVat(0); // 부가세 0 일단은
				delngParam.setTrgetMberCode(mberCode);
				delngParam.setTrgetMberCodeSn(mberCodeSn);
				delngParam.setTrgetRciptNo(param.getRciptNo());
				delngParam.setApprovalConfirm("O");
				delngParam.setDeviceSeqNo(1);
				delngParam.setVanCode("PG_KSNET");
				delngParam.setStoreId(storeId);
				delngParam.setDelngSeCode("CARD_ISSUE");
				delngParam.setGoodNm(param.getOrderName());
				delngParam.setDelngPayType(tbMberBasis.get("PAY_TYPE").toString());
				 
				
				
				if ("N".equals(tbMberBasis.get("SUGI_CERTIFICATION").toString())) {
					delngParam.setPaymentDevice("SUGI_NORMAL");
				} else if ("Y".equals(tbMberBasis.get("SUGI_CERTIFICATION").toString())) {
					delngParam.setPaymentDevice("SUGI_CERTIFICATION");
				} else
					delngParam.setPaymentDevice("SUGI_NORMAL");
				// 누락데이터 주의
				delngParam.setToSwiptStatus("TE");
				float feeRate = Float.valueOf(String.valueOf(tbMberBasis.get("FEE_RATE"))); // 수수료율
				long sellAmt = param.getAmount(); // 판매금액 부가세는 0으로
				delngParam.setMberFee(feeRate); // 멤버 수수료 율

				double mberFeeAmt = 0;
				mberFeeAmt = sellAmt * feeRate / 100;
				delngParam.setMberFeeAmt(df.format(mberFeeAmt)); // 멤버 수수료 금액

				// 정산금액
				delngParam.setPayAmt(df.format(sellAmt - sUtil.getDouble(df.format(mberFeeAmt))));

				tparam1.setConfmNo(sUtil.getString(ht.get("AuthNo")).trim());
				if (exMapper.insertDelng(delngParam) < 1) {
					throw new ExorderException("9999"); // 매출처리 실패. 관리자에게 문의바랍니다
				}

				// delngCredt
				DelngCredtParam delngCredtParam = new DelngCredtParam();
				delngCredtParam.setMberCode(mberCode);
				delngCredtParam.setMberCodeSn(mberCodeSn);
				delngCredtParam.setRciptNo(param.getRciptNo());
				delngCredtParam.setCardNo(cUtil.encrypt(sUtil.MarkForCreditCard(param.getCardNo())));
				delngCredtParam.setInstlmtMonth(param.getInstallment());
				delngCredtParam.setIssueCmpnyCode(sUtil.getString(ht.get("IssCode")).trim());
				delngCredtParam.setIssueCmpnyNm(sUtil.getString(ht.get("Message1")).trim());
				delngCredtParam.setPuchasCmpnyCode(sUtil.getString(ht.get("AquCode")).trim());
				delngCredtParam.setPuchasCmpnyNm(sUtil.getString(ht.get("Message1")).trim());
				delngCredtParam.setCdrsrNo(sUtil.getString(ht.get("MerchantNo")).trim());
				delngCredtParam.setCardType("D"); // 확인해보자!
				delngCredtParam.setPgVanGb("P"); // 확인해보자!
				delngCredtParam.setTId(storeId);
				delngCredtParam.setPgRciptNo(sUtil.getString(ht.get("TransactionNo")).trim());//// pg거래번호
				delngCredtParam.setGbInfo("U"); // 유니코아
				delngCredtParam.setVanPgComp("PG_KSNET");
				delngCredtParam.setCardValidNo( cUtil.encrypt( param.getExpiration() ));
				tparam1.setOrderType("C");
				tparam1.setOrderDetail(sUtil.getString(ht.get("Message1")).trim());
				if (exMapper.insertDelngCredt(delngCredtParam) < 1) {
					throw new ExorderException("9999"); // 매출처리 실패. 관리자에게 문의바랍니다
				}

				// delng_adi
				DelngAdiParam delngAdiParam = new DelngAdiParam();
				delngAdiParam.setMberCode(mberCode);
				delngAdiParam.setMberCodeSn(mberCodeSn);
				delngAdiParam.setRciptNo(param.getRciptNo());
				delngAdiParam.setAdiCode("PURCHSR_MBTLNUM");
				if (param.getPhoneNumber() != null && !"".equals(param.getPhoneNumber())) {
					delngAdiParam.setAdiCn(cUtil.encrypt(param.getPhoneNumber()));
				} else {
					delngAdiParam.setAdiCn("722211d65862dac6ab81668e0544b4e3"); // 핸드폰번호 없으면 공백값이다.
				}
				if (exMapper.insertDelngAdi(delngAdiParam) < 1) {
					throw new ExorderException("9999"); // 매출 정보등록 실패. 관리자에게 문의바랍니다
				}

				// 결제정보 업데이트
				ExternalOrderInputParam exParam = new ExternalOrderInputParam();
				exParam.setStatus("AAAA"); // 결제완료
				exParam.setMberId(param.getMemberId());
				exParam.setExorderNo(param.getExorderNo());
				exParam.setConfmNo(delngParam.getConfmNo());
				exParam.setRciptNo(param.getRciptNo());
				exParam.setEmail(param.getEmail());
				exParam.setMobileNum(param.getPhoneNumber());

				tparam1.setMberId(param.getMemberId());
				tparam1.setOrderPrice(String.valueOf(param.getAmount()));
				tparam1.setExorderNo(param.getExorderNo());
				tparam1.setConfmNo(delngParam.getConfmNo());
				tparam1.setRciptNo(param.getRciptNo());
				tparam1.setOrderName(param.getOrderName());
//				tparam1.setOrderType(    );
//				tparam1.setOrderDetail(delngCredtParam.getIssueCmpnyNm());
				exMapper.updateExOrder(exParam);

			} else {
				throw new ExorderException("9999"); // 결제승인 실패. 관리자에게 문의바랍니다
			}
			return tparam1;
		} catch (ExorderException e) {
			throw new ExorderException(e);

		} catch (Exception e) {
			oet.setMessage("9999");
			throw new ExorderException(oet);
		}

	}

	@Override
	@Transactional
	public void payCancel(ExternalOrderInputParam param) throws Exception {
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		// 1. 외부 입력 결제 내역확인
		ExternalOrderInputParam exInfo = exMapper.selectOrderInfo2(param);
		if (exInfo == null) {
			throw new ExorderException("A001");
		}
		if ("C001".equals(exInfo.getStatus())) {
			throw new ExorderException("C001");
		}
		if ("0000".equals(exInfo.getStatus())) {
			throw new ExorderException("0000");
		}

		// 사용자별 취소가능 판단
		// 익일 당일 
		if ( "T".equals( exInfo.getPayType()) && exInfo.getCancelPeriod() !=0) {
			throw new ExorderException("C009");
		}
		// 5일 거래
		if ( "T".equals( exInfo.getPayType()) && exInfo.getCancelPeriod() >=4) {
			throw new ExorderException("C010");
		}
		// 바로정산 --  카드만 있다 카드이외의 결제 방식 생기면 수정해야하는곳 #수정 
		// 결제 금액이 있을것으로 가정
		/*
		if ( "B".equals( exInfo.getPayType()) && exInfo.getCancelPeriod() >=4) {
			// 입금내역확인
			map1.clear();
			map1.put("mberCode", exInfo.getMberCode());
			map1.put("rciptNo", exInfo.getRciptNo());
			Integer tempReqAmt = exMapper.selectReqAmt(map1);
			if(tempReqAmt==null ) throw new ExorderException("C011");
			if(tempReqAmt ==0) throw new ExorderException("C011");
			map1.clear();
			// 출금내역확인
			
			
			
			throw new ExorderException("C011");
			
		}
		*/
		

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
		if (delngInfo != null) {
			throw new ExorderException("C001");
		}
		// 결제 정보가 있는지 확인
		dParam.setDelngSeCode("CARD_ISSUE");
		delngInfo = exMapper.selectDelngInfo(dParam);
		// 결제 정보가 없는경우
		if (delngInfo == null) {
			throw new ExorderException("A001");
		}

		dParam.setMberCodeSn(delngInfo.getMberCodeSn());
		// 카드 결제 정보 추출
		DelngCredtParam delngCredtInfo = new DelngCredtParam();
		// dto 가 너무 많아 지므로 map 로 변경하거나 결과 dto 를 파라미터로 사용하자!!!!!!
		map1.clear();
		map1.put("mberCode", delngInfo.getMberCode());
		map1.put("rciptNo", delngInfo.getRciptNo());

		delngCredtInfo = exMapper.selectDelngCredt(map1);
		// 카드 결제정보가 없는경우
		if (delngCredtInfo == null) {
			throw new ExorderException("A011");
		}

		// 실제 취소 처리부분 ksnet
		Hashtable xht = ksBean.sendCardCancelMsg(KSNET_PG_IP, // -필수- ipaddr X(15) *KSNET_IP(개발:210.181.28.116,
																// 운영:210.181.28.137)
				KSNET_PG_PORT, // -필수- port 9( 5) *KSNET_PORT(21001)
				delngCredtInfo.getTId(), // -필수- pStoreId X(10) *상점아이디(개발:2999199999, 운영:?)
				"K", // -필수- pKeyInType X(12) KEY-IN유형(K:직접입력,S:리더기사용입력)
				delngCredtInfo.getPgRciptNo() // -필수- pTransactionNo X( 1) *거래번호(승인응답시의 KEY:1로시작되는 12자리숫자)
		);
		// ksnet 결과 처리가 없는경우
		log.info(xht.toString());
		if (xht == null) {
			throw new ExorderException("C007");
		}

//		Hashtable xht = new Hashtable<>();
//		xht.put("Status", "O");
		// 정상적으로 취소 처리가 된경우
		if ("O".equals(sUtil.getString(xht.get("Status")))) {
			// 영수증번호 획득
			HashMap<String, Object> tempMap = new HashMap<>();
			tempMap.put("mber_code", String.valueOf(exInfo.getMberCode()));
			tempMap.put("rcipt_no", "");
			exMapper.propRciptNO(tempMap); // 새로얻어온 영수증번호 여기에 tempMap -- 프로시저 실행이므로 프로시저실행파라미터값에 그대로 변경된 값이 담겨온다!

			// 취소 요청한 날짜로 취소 일자 기록
			delngInfo.setCardDeleteYn("Y");
			delngInfo.setBigo("연동결제취소");
			delngInfo.setMberId(param.getMberId());

			dParam.setCancelDt(sUtil.getString(xht.get("TradeDate")).trim());
			delngInfo.setDelngSeCode("CARD_CNCL");
			delngInfo.setConfmDt(sUtil.getString(xht.get("TradeDate")).trim());
			delngInfo.setConfmTime(sUtil.getString(xht.get("TradeTime")).trim());
			delngInfo.setCancelDt(sUtil.getString(xht.get("TradeDate")).trim());
			delngInfo.setConfmTime(sUtil.getString(xht.get("TradeTime")).trim());
			delngInfo.setSplpc(delngInfo.getSplpc() * -1);
			delngInfo.setVat(delngInfo.getVat() * -1);

			if ("".equals(param.getBigo()) || param.getBigo() == null) {
				delngInfo.setBigo("결제취소요청");
			} else {
				delngInfo.setBigo(param.getBigo());
			}

			param.setStatus("C001");
			// 1. 외부 결제정보 수정
			if (exMapper.updateExOrder(param) < 1) {
				throw new ExorderException("C002");
			}
			// 2. dlng 결제정보 수정
			if (exMapper.updateDelng(delngInfo) < 1) {
				throw new ExorderException("C003");
			}
			delngInfo.setRciptNo(tempMap.get("rcipt_no").toString());
			// 3. dlng 결제정보 취소정보입력
			if (exMapper.insertDelng(delngInfo) < 1) {
				throw new ExorderException("C003");
			}

			// 4. delng credt 결제 취소정보 입력
			// 카드 결제 정보 추출
			delngCredtInfo = exMapper.selectDelngCredt(map1);
			delngCredtInfo.setRciptNo(tempMap.get("rcipt_no").toString());

			if (exMapper.insertDelngCredt(delngCredtInfo) < 1) {
				throw new ExorderException("C004");
			}

			// delng_adi - 부가정보
			DelngAdiParam delngAdiParam = new DelngAdiParam();
			delngAdiParam.setMberCode(dParam.getMberCode());
			delngAdiParam.setMberCodeSn(dParam.getMberCodeSn());
			delngAdiParam.setRciptNo(exInfo.getRciptNo());
			DelngAdiParam delngAdiParam2 = new DelngAdiParam();
			delngAdiParam2 = exMapper.selectDelngAdi(delngAdiParam);
			delngAdiParam2.setRciptNo(tempMap.get("rcipt_no").toString());

			if (exMapper.insertDelngAdi(delngAdiParam2) < 1) {
				throw new ExorderException("9999");
			}
		} else {
			throw new ExorderException("9999");
		}
	}

	@Override
	public boolean notiCallParam(ExternalOrderInputParam param) throws Exception {
		log.info("결제완료후 노티 ");
		if (param.getNotiUrl() != null && param.getNotiUrl().trim().length() > 5) {
			URL url = new URL(param.getNotiUrl());
			if (param.getNotiUrl().contains("https://")) {
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setReadTimeout(3000);
				conn.setRequestMethod("post");
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write("orderName=" + param.getOrderName() + "&exorderNo=" + param.getExorderNo() + "&orderPrice="
						+ param.getOrderPrice() + "&mberId=" + param.getMberId() + "&orderStatus=" + param.getStatus()
						+ "&orderMethod=" + param.getOrderType() + "&orderMethodDetail=" + param.getOrderDetail()
						+ "&confmNo=" + param.getConfmNo());
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(line);
				}
				if ("100".equals(sb.toString())) {
					return true;
				} else
					return false;
			}

			if (param.getNotiUrl().contains("http://")) {
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setReadTimeout(3000);
				conn.setRequestMethod("post");
				conn.setDoOutput(true);
				OutputStream os = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write("orderName=" + param.getOrderName() + "&exorderNo=" + param.getExorderNo() + "&orderPrice="
						+ param.getOrderPrice() + "&mberId=" + param.getMberId() + "&orderStatus=" + param.getStatus()
						+ "&orderMethod=" + param.getOrderType() + "&orderMethodDetail=" + param.getOrderDetail()
						+ "&confmNo=" + param.getConfmNo());
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(line);
				}
				if ("100".equals(sb.toString())) {
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	@Override
	public boolean notiCallHttp(ExternalOrderInputParam param) throws Exception {
		HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
		HttpPost httpPost = new HttpPost(param.getNotiUrl()); // POST 메소드 URL 새성
		try {
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Connection", "keep-alive");
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

			String paramString = "{\"orderName\": \"" + param.getOrderName() + "\"" + ", \"exorderNo\" : \""
					+ param.getExorderNo() + "\"" + ", \"orderPrice\" : \"" + param.getOrderPrice() + "\""
					+ ", \"mberId\" : \"" + param.getMberId() + "\"" + ", \"orderStatus\" : \"" + param.getStatus()
					+ "\"" + ", \"orderMethod\" : \"" + param.getOrderType() + "\"" + ", \"orderMethodDetail\" : \""
					+ param.getOrderDetail() + "\"" + ", \"confmNo\" : \"" + param.getConfmNo() + "\"" + "}";
			httpPost.setEntity(new StringEntity(paramString, "UTF-8")); // json 메시지 입력

			HttpResponse response = client.execute(httpPost);
			String result = "";
			// Response 출력
			HttpEntity entity = response.getEntity();
			String bodyString = EntityUtils.toString(entity);
			if (response.getStatusLine().getStatusCode() == 200 && "100".equals(bodyString)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public void exOrderCancel(ExternalOrderInputParam param) throws Exception {
		param.setStatus("C001");
		exMapper.updateExOrder(param);
	}

	@Override
	public OrderStatusInfo exOrderInfo(HashMap<String, Object> param) throws Exception {
		OrderStatusInfo info= exMapper.selectExorderInfo(param);
		// status 가 내부에서 사용하는것 대외용이 다르므로 변경해준다
		if (info == null) {
			info = new OrderStatusInfo();
			info.setStatus("3010");
		}
		if( info.getStatus() !=null && "AAAA".equals(info.getStatus())) {
			info.setStatus("1000");
		}
		if( info.getStatus() !=null && "C001".equals(info.getStatus())) {
			info.setStatus("2000");
		}
		
		return info;
	}

	@Override
	public void payPreCancel(PaymentReqParam param) throws Exception {
		// 결제이후 취소는 결제사 연동등이 있지만 결제전 취소는 TB_EX_ORDER 하나만 처리하면 된다.
		log.info("결제전 단순 취소");
		
		ExternalOrderInputParam ep = new ExternalOrderInputParam();
		if(param.getExorderNo() == null ) {
			throw new ExorderException("C002");
		}
		
		if(param.getMemberId() == null ) {
			throw new ExorderException("C002");
		}
		
		ep.setMberId(param.getMemberId());
		ep.setExorderNo(param.getExorderNo()  );
		ep.setStatus(param.getStatus());
		exMapper.updateExOrder(ep);
		
	}

	@Override
	public SmsLink selectSmsLinkInfo(long id) throws Exception {		
		SmsLink smsLinkInfo = exMapper.selectSmsLinkInfo(id);
		if(smsLinkInfo==null) {
			throw new ExorderException("SMS01");
		}
		
		if(smsLinkInfo.getSendPeroid() >3 && smsLinkInfo.getStep()==3) {
			throw new ExorderException("SMS07");
		}
		// 매출에서 취소여부확인한다.
		// 판매자가 취소한 경우
		if(smsLinkInfo.getStep()==8) {
			throw new ExorderException("SMS05");
		}
		
		// 판매상품이 정상 인지 확인
		if(exMapper.selectCardCancelCount(smsLinkInfo.getMberCode(),  smsLinkInfo.getRciptNo()) > 0) {
			throw new ExorderException("SMS06");
		}
	
		 if("Y".equals(smsLinkInfo.getPayFinishYn())) { 
			 throw new  ExorderException("SMS04"); 
		 }
		 
		// 복호화부분
		// 핸드폰번호 복호화
		if(smsLinkInfo.getMberMobile() !=null && smsLinkInfo.getMberMobile().trim().length() > 0 ) {
			smsLinkInfo.setMberMobile(cUtil.decrypt(smsLinkInfo.getMberMobile()));			
		}
		// 배송 핸드폰번호
		if(smsLinkInfo.getSmsSendPhone() !=null && smsLinkInfo.getSmsSendPhone().trim().length() > 0 ) {
			smsLinkInfo.setSmsSendPhone( cUtil.decrypt( smsLinkInfo.getSmsSendPhone() ) );
		}
		// 판매상품정보 보정
		String nameList = smsLinkInfo.getItName();
		String countList = smsLinkInfo.getItCount();
		String priceList =smsLinkInfo.getItPrice();
		String infoList = smsLinkInfo.getItAddInfo();
		String urlList = smsLinkInfo.getItDetailUrl();
		if(nameList == null || nameList.trim().length() <1) {
			throw new ExorderException("SMS02");
		}
		if(countList == null || countList.trim().length() <1) {
			throw new ExorderException("SMS02");
		}
		if(priceList == null || priceList.trim().length() <1) {
			throw new ExorderException("SMS02");
		}
		ArrayList<String> arrNameList = new ArrayList<String>();
		for (String item : nameList.split("#")) {
			arrNameList.add(item);
		}
		if(arrNameList.size() !=  smsLinkInfo.getItTotalCnt()) {
			throw new ExorderException("SMS03");
		}
		smsLinkInfo.setNameList(arrNameList);
		
		ArrayList<String> arrCountList = new ArrayList<String>();
		for (String item : countList.split("#")) {
			arrCountList.add(item);
		}
		if(arrCountList.size() !=  smsLinkInfo.getItTotalCnt()) {
			throw new ExorderException("SMS03");
		}
		smsLinkInfo.setCountList(arrCountList);
		
		ArrayList<String> arrPriceList = new ArrayList<String>();
		for (String item : priceList.split("#")) {
			arrPriceList.add(item);
		}
		if(arrPriceList.size() !=  smsLinkInfo.getItTotalCnt()) {
			throw new ExorderException("SMS03");
		}
		
		// 주문정보 호출전 결제 가능한 금액인지 사전검사
		String temp = pCheck.limitPayCheck(smsLinkInfo.getMberCode(), smsLinkInfo.getItTotalAmt() );
		if ( "one".equals(temp)) {
			throw new ExorderException("L001");
		}else if("day".equals(temp)) {
			throw new ExorderException("L002");
		}else if("month".equals(temp)) {
			throw new ExorderException("L003");
		}else if("year".equals(temp)) {
			throw new ExorderException("L004");
		}
		
		smsLinkInfo.setPriceList(arrPriceList);
		ArrayList<String> arrInfoList = new ArrayList<String>();
		for (String item : infoList.split("#")) {
			arrInfoList.add(item);
		}
		smsLinkInfo.setInfoList(arrInfoList);
		ArrayList<String> arrUrlList = new ArrayList<String>();
		if(urlList!=null) {
			for (String item : urlList.split("#")) {
				arrUrlList.add(item);
			}
			smsLinkInfo.setUrlList(arrUrlList);
		}
		return smsLinkInfo;
	}

	@Override
	@Transactional
	public void payment(SmsPayRequest param) {
		// 카드결제가 발생후 오류인 경우 강제 결제 취소를 시켜야 한다.
		OrderErrorType oet = new OrderErrorType();
		oet.setKSNET_PG_IP(KSNET_PG_IP);
		oet.setKSNET_PG_PORT(KSNET_PG_PORT);
		oet.setPKeyInType("K");
		try {
			HashMap<String, Object> tbMberBasis = exMapper.selectTbMberBasis1( Long.parseLong(param.getMberCode() ));
			
			// 판매자의 상태를 점검한다.
			if(tbMberBasis==null) {
				throw new ExorderException("L009");
			}
			
			if("N".equals(tbMberBasis.get("use_at"))) {
				throw new ExorderException("L009");
			}
			
			// 1 .결제 전 검사진행
			// 1.1결제전 결제금액확인
			String temp = pCheck.limitPayCheck(param.getMberCode(), param.getTotAmt());
			if ( "one".equals(temp)) {
				throw new ExorderException("L001");
			}else if("day".equals(temp)) {
				throw new ExorderException("L002");
			}else if("month".equals(temp)) {
				throw new ExorderException("L003");
			}else if("year".equals(temp)) {
				throw new ExorderException("L004");
			}
			
			// 1.2 동일카드체크
			// 카드 뒷자리 4개
			if(param.getCardNumber().length()!=16) throw new ExorderException("L005");
			
			// 스와이프칩인경우 - sms 결제를 단말기기로 결제?
			String temp1 = param.getCardNumber().substring(10, 14);
			if("****".equals(temp1 )) throw new ExorderException("L005");
			
			
			// 동일 금액 동일 카드 번호(뒷네자리) 로 검사한다.
			if(pCheck.sameCarkCkeck(temp1, param.getMberCode(), param.getTotAmt())) {
				throw new ExorderException("L006");
			}
			// 상점아이디
			String storeId =pCheck.getStoreId(tbMberBasis.get("PAY_TYPE").toString());
			oet.setPStoreId(storeId);
			if("".equals(storeId)) throw new ExorderException("L007");
			
			// 영수증번호 생성
			String rciptNo = pCheck.getRciptNo(param.getMberCode());
			param.setRciptNo( Long.valueOf( rciptNo ));
			if("".equals(rciptNo)) throw new ExorderException("C011");
			
			// 사용자 정보 획득
			MemberInfo mberInfo = auMapper.userInfo2(param.getMberCode());
			// 기존 상품정보 획득
			SmsLink smsLinkInfo = exMapper.selectSmsLinkInfo(param.getId());
			
			// 주문번호 생성
			//String orderNo = mberInfo.getUsid() + "_" + rciptNo;
			String orderNo = mberInfo.getUsid();
			// 결제시도 
			Hashtable<String, Object> ht = new Hashtable<>();
			// TrackII 
			String trackII = param.getCardNumber()+ "=" + param.getExpiration();
			// 기존 소스 부분 에서 중간 결제단계 
					
			ht = ksBean.sendCardMsg(
					KSNET_PG_IP, 
					KSNET_PG_PORT, 
					storeId,
					orderNo, // 주문번호
					"", 
					param.getPidNum(), 
					param.getCardEmail(), // 이메일 결제한 사람이 판매한 사람이???
					smsLinkInfo.getItName(), // 상품명
					param.getCardMobilePhone(), // 핸드폰번호
					"K", // pKeyInType X(12) KEY-IN유형(K:직접입력,S:리더기사용입력)
					"1", // pInterestType X( 1) *일반/무이자구분 1:일반 2:무이자
					trackII, // -필수- pTrackII X(40) *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM])
					String.valueOf(param.getInstallment()), 
					String.valueOf(param.getTotAmt()), 
					param.getPasswd(), 
					param.getPidNum());
			
			if(ht==null)throw new ExorderException("C011");
			
			// 성공이후 // 데이터 저장
			String ox = sUtil.getString(ht.get("Status")).trim();
			oet.setPTransactionNo(sUtil.getString(ht.get("TransactionNo")).trim());
			if ("X".equals(ox)) {
				throw new ExorderException("C011");
			}
			//String ox =  "O";
			
			if ("O".equals(ox)) {
				// 배송지 정보를 입력한다. -- 앱과 충돌 안나도록 별도로 저장한다. 사유는 추후 사용될 주문정보 + 배송정보 이다
//				if(param.getRecipient()!=null && param.getRecipient().length() !=0 ) {
//					Destination des = new Destination();
//					des.setMberCode( Integer.parseInt( param.getMberCode() ));
//					des.setRciptNo(param.getRciptNo());
//					des.setRecipient(param.getRecipient());
//					des.setMobilePhone(param.getMobilePhone());
//					des.setAddress(param.getAddrInfo() + " "+ param.getAddrDetailInfo());
//					des.setRecipient( param.getRecipient());
//					des.setMessage(param.getMessage());			
//					exMapper.insertDestination(des);
//				}
				
				// 1. 상태 업데이트 - mber_sms_link 
				param.setFinshYn("Y"); // 스텝이 있는데 ??
				param.setStep(6);
				// 입력전 암호화 -
				// 배송지 고객명
				if (param.getRecipient() != null && !"".equals(param.getRecipient())) {
					param.setRecipient(cUtil.encrypt( param.getRecipient() ));					
				}
				if (param.getMobilePhone() != null && !"".equals(param.getMobilePhone())) {					
					param. setMobilePhone(cUtil.encrypt( param.getMobilePhone() ));
				}
				
				if(exMapper.updateSmsLink(param) <1 ) {
					throw new ExorderException("L008"); // 결제오류처리 
				}
				
				// 결제 정보 입력 -- 결제 정보 쪽이 더 중요한 정보 이므로 이걸 후속 로직으로 옮긴다.
				DecimalFormat df = new DecimalFormat("##0");
				DelngParam delngParam = new DelngParam();
				// delng
				delngParam.setMberCode( Long.parseLong(param.getMberCode())  );
				delngParam.setMberCodeSn(mberInfo.getMberCodeSn());
				delngParam.setRciptNo( String.valueOf(param.getRciptNo()));
				delngParam.setAppCode("LINK");
				delngParam.setConfmNo(sUtil.getString(ht.get("AuthNo")).trim());
				delngParam.setConfmDt(sUtil.getString(ht.get("TradeDate")).trim());
				delngParam.setConfmTime(sUtil.getString(ht.get("TradeTime")).trim());
				delngParam.setSplpc(param.getTotAmt() );
				delngParam.setVat(0); // 부가세 0 일단은
				delngParam.setTrgetMberCode(Long.parseLong(param.getMberCode()) );
				delngParam.setTrgetMberCodeSn(mberInfo.getMberCodeSn());
				delngParam.setTrgetRciptNo(String.valueOf(param.getRciptNo()));
				delngParam.setApprovalConfirm("O");
				delngParam.setDeviceSeqNo(1);
				delngParam.setVanCode("PG_KSNET");
				delngParam.setStoreId(storeId);
				delngParam.setDelngSeCode("CARD_ISSUE"); //현재는 카드결제만 있다.
				delngParam.setGoodNm(smsLinkInfo.getItName());
				delngParam.setDelngPayType( tbMberBasis.get("PAY_TYPE").toString() );
				
				if ("N".equals(tbMberBasis.get("SUGI_CERTIFICATION").toString())) {
					delngParam.setPaymentDevice("SUGI_NORMAL");
				} else if ("Y".equals(tbMberBasis.get("SUGI_CERTIFICATION").toString())) {
					delngParam.setPaymentDevice("SUGI_CERTIFICATION");
				} else
					delngParam.setPaymentDevice("SUGI_NORMAL");
				
				// 누락데이터 주의
				delngParam.setToSwiptStatus("TE");
				float feeRate = Float.valueOf(String.valueOf(tbMberBasis.get("FEE_RATE"))); // 수수료율
				long sellAmt = param.getTotAmt(); // 판매금액 부가세는 0으로
				delngParam.setMberFee(feeRate); // 멤버 수수료 율

				double mberFeeAmt = 0;
				mberFeeAmt = sellAmt * feeRate / 100;
				delngParam.setMberFeeAmt(df.format(mberFeeAmt)); // 멤버 수수료 금액

				// 정산금액
				delngParam.setPayAmt(df.format(sellAmt - sUtil.getDouble(df.format(mberFeeAmt))));
				delngParam.setSmsLinkYn("Y");
				if (exMapper.insertDelng(delngParam) < 1) {
					log.info("DELNG 입력에러");
					log.info( delngParam.toString() );
					throw new ExorderException("L008"); // 매출처리 실패. 관리자에게 문의바랍니다
				}
				
				// DELNG_CREDT 입력
				DelngCredtParam delngCredtParam = new DelngCredtParam();
				delngCredtParam.setMberCode(Long.parseLong(param.getMberCode()) );
				delngCredtParam.setMberCodeSn(mberInfo.getMberCodeSn());
				delngCredtParam.setRciptNo(String.valueOf(param.getRciptNo()));
				delngCredtParam.setCardNo(cUtil.encrypt(sUtil.MarkForCreditCard(param.getCardNumber())));
				delngCredtParam.setInstlmtMonth(  param.getInstallment() );
				delngCredtParam.setIssueCmpnyCode(sUtil.getString(ht.get("IssCode")).trim());
				delngCredtParam.setIssueCmpnyNm(sUtil.getString(ht.get("Message1")).trim());
				delngCredtParam.setPuchasCmpnyCode(sUtil.getString(ht.get("AquCode")).trim());
				delngCredtParam.setPuchasCmpnyNm(sUtil.getString(ht.get("Message1")).trim());
				delngCredtParam.setCdrsrNo(sUtil.getString(ht.get("MerchantNo")).trim());
				delngCredtParam.setCardType("D"); // 확인해보자!
				delngCredtParam.setPgVanGb("P"); // 확인해보자!
				delngCredtParam.setTId(storeId);
				delngCredtParam.setPgRciptNo(sUtil.getString(ht.get("TransactionNo")).trim());//// pg거래번호
				delngCredtParam.setGbInfo("U"); // 유니코아
				delngCredtParam.setVanPgComp("PG_KSNET");
				delngCredtParam.setCardValidNo( cUtil.encrypt( param.getExpiration() ));

				if (exMapper.insertDelngCredt(delngCredtParam) < 1) {
					log.info("DELNG_CREDT 입력에러");
					log.info( delngCredtParam.toString() );
					throw new ExorderException("L008"); // 매출처리 실패. 관리자에게 문의바랍니다
				}
				
				// delng_adi
				DelngAdiParam delngAdiParam = new DelngAdiParam();
				delngAdiParam.setMberCode(Long.parseLong( param.getMberCode()));
				delngAdiParam.setMberCodeSn(mberInfo.getMberCodeSn());
				delngAdiParam.setRciptNo(String.valueOf(param.getRciptNo()));
				delngAdiParam.setAdiCode("PURCHSR_MBTLNUM");
				// 핸드폰 번호 처리 ?? 이전 개발분에는 값을 받는곳이 없음 그냥 빈값 넣음
				// 구매자가 핸드폰 번호입력한경우 핸드폰번호 입력함
				if (param.getCardMobilePhone() != null && !"".equals(param.getCardMobilePhone())) {
					delngAdiParam.setAdiCn(cUtil.encrypt(param.getCardMobilePhone()));
				} else {
					delngAdiParam.setAdiCn("722211d65862dac6ab81668e0544b4e3"); // 핸드폰번호 없으면 공백값이다. 공백값을 입력해야 하는 이유????
				}
				if (exMapper.insertDelngAdi(delngAdiParam) < 1) {
					log.info("delng_adi 입력에러");
					log.info( delngAdiParam.toString() );
					throw new ExorderException("L008"); // 매출 정보등록 실패. 관리자에게 문의바랍니다
				}
				// 결제완료후
				// 바로 정산처리 - 현재는 카드만 !!!!!  if(gubn.equals("JA") && "B".equals(결제판매자pay_type))
				if( "B".equals(tbMberBasis.get("PAY_TYPE").toString() ) ) {
					TblAtmParam tap = new TblAtmParam();
					tap.setMberCode(Long.parseLong(param.getMberCode()));
					tap.setInoutNo(exMapper.selectInoutNo( Long.parseLong(param.getMberCode() ) ));
					tap.setInoutCode("IN_SM");
					tap.setCharge(0);
					tap.setReqResult("OK");
					tap.setBizCode(  tbMberBasis.get("BIZ_CODE").toString() );
					
					
					//feeRate -- 수수료율
					tap.setSalesFeePer( Float.parseFloat(tbMberBasis.get("FEE_RATE").toString()) );
					// d수수료금액
					int tempSalesFeeAmt = (int)Math.ceil((param.getTotAmt() * Float.parseFloat(tbMberBasis.get("FEE_RATE").toString()) /100));
					tap.setSalesFeeAmt( tempSalesFeeAmt );
					// 정산금액
					tap.setReqAmt( param.getTotAmt() - tempSalesFeeAmt );
					tap.setSalesTotAmt(  param.getTotAmt() );
					tap.setSalesRciptNo(rciptNo);
					tap.setSalesDt( sUtil.getString(ht.get("TradeDate")).trim() );
					tap.setSalesTime( sUtil.getString(ht.get("TradeTime")).trim());
					tap.setSalesFeePer( Float.parseFloat( tbMberBasis.get("FEE_RATE").toString()) );
					
					// 현재 잔액 구해오기
					Integer tblBalance = exMapper.selectTblBalance(Long.parseLong(param.getMberCode()) );
					if(tblBalance == null) tblBalance=0;
					tblBalance = (int) (tblBalance + param.getTotAmt() - tempSalesFeeAmt);
					tap.setBalance(tblBalance);
					
					if(exMapper.insertTblAmt(tap) <1) {
						throw new ExorderException("L010");
					}	
				}
			}
			
		} catch (ExorderException e) {
			System.out.println(e.getMessage());
			log.info(e.getLocalizedMessage());
			throw new ExorderException(e.getMessage());

		} catch (Exception e) {
			oet.setMessage("9999");
			throw new ExorderException(oet);
		}
		
	}

	@Override
	public ArrayList<LinkSms> selectLinkSmsInfo(long id) throws Exception {
		LinkSms info = exMapper.selectLinksmsInfo(id);
		if(info==null) {
			throw new ExorderException("SMS01");
		}
		ArrayList<LinkSms> list = new ArrayList<LinkSms>();
		// 전체 상품인지 판단
		if("Y".equals(info.getAllItemYn()) ) {
			list = exMapper.selectLinksmsList(info.getMberCode());
			if(list.size()==0) {
				throw new ExorderException("SMS01");
			}
			// 전화번호 복호화
			for (LinkSms linkSms : list) {
				linkSms.setMberMobile(cUtil.decrypt( info.getMberMobile()  ) );
				linkSms.setAddrYn( info.getAddrYn() );
			}
		}else {
			info.setMberMobile(cUtil.decrypt( info.getMberMobile()  ) );
			list.add(info);
		}
		return list;
	}

	@Override
	public long insertSmsGoods(SmsInsert param) throws Exception {
		if(exMapper.insertMberSmsLinkLink(param) >0) {
			return param.getSmsLinkId();
		}else {
			throw new ExorderException("S001");
		}
	}

	@Override
	public SmsCardPayment selectSmsCardPayment(long id) throws Exception {
		SmsCardPayment info = exMapper.selectSmsCardPayment(id);
		info.setCardNo(cUtil.decrypt( info.getCardNo( )));
		info.setMberMobile(cUtil.decrypt( info.getMberMobile() ));
		info.setAdres(cUtil.decrypt( info.getAdres() ));
		return info;
	}

	@Override
	public HashMap<String, Object> smsPayResultInfo(long id) throws Exception {
		SmsLink smsLinkInfo = exMapper.selectSmsLinkInfo(id);
		String tempStr1 = "";
		String tempStr2 = "";
		String tempStr3 = "";
		String tempStr4 = "";
		String tempStr5 = "";
		// 상품정보
		ArrayList<GoodsInfo> goodsList = new ArrayList<GoodsInfo>();
		// 판매자 정보
		SellerInfo sellerInfo = new SellerInfo();
		sellerInfo.setMberCode( smsLinkInfo.getMberCode());
		sellerInfo.setMberName( smsLinkInfo.getMberName() );
		sellerInfo.setMberMobile( cUtil.decrypt(smsLinkInfo.getMberMobile()) );
		sellerInfo.setCompanyName( smsLinkInfo.getCompanyName());
		sellerInfo.setInstallmentMonths(smsLinkInfo.getInstallmentMonths());
		sellerInfo.setSugiCertification( smsLinkInfo.getSugiCertification());
		sellerInfo.setSmslinkMemo(smsLinkInfo.getSmslinkMemo());
		if (smsLinkInfo.getSmsSendPhone() != null && !"".equals(smsLinkInfo.getSmsSendPhone())) {
			sellerInfo.setCustPhone( cUtil.decrypt(smsLinkInfo.getSmsSendPhone() ));			
		}
		
		// 배송지 정보
		Destination destination =  exMapper.selectOrderDestination( Long.parseLong( smsLinkInfo.getMberCode()) , smsLinkInfo.getRciptNo() );
		// 필요정보 복호화
		if (destination.getRecipient() != null && !"".equals(destination.getRecipient())) {
			destination.setRecipient( cUtil.decrypt(  destination.getRecipient()   )  );
		}
		if (destination.getMobilePhone() != null && !"".equals(destination.getMobilePhone())) {
			destination.setMobilePhone( cUtil.decrypt(  destination.getMobilePhone()   )  );
		}

		// 결제정보
		PaymentInfo pInfo = new PaymentInfo();
		// 현재는 카드만 존재하므로 
		SmsCardPayment info = exMapper.selectSmsCardPayment(id);
		pInfo.setType("card");
		pInfo.setDateStr(info.getConfmDt());
		pInfo.setName(info.getIssueCmpnyNm());
		pInfo.setPaymentNo(cUtil.decrypt(info.getCardNo()));
		pInfo.setRciptNo( info.getRciptNo() );
		pInfo.setConfmNo( info.getConfmNo() );

		HashMap<String, Object> map = new HashMap<>();
		// sms 상품정보/ 판매정보 추출 
		// 상품이름이 있다면 가격 등이 다른 # 포함 정보들이 들어 있다는 가정으로 진행한다.
		// 상품명이 최대 크기 인것으로 가정함
		tempStr1 = smsLinkInfo.getItName().trim();
		tempStr2 = smsLinkInfo.getItPrice().trim();
		tempStr3 = smsLinkInfo.getItCount().trim();
		tempStr4 = smsLinkInfo.getItDetailUrl().trim();
		tempStr5 = smsLinkInfo.getItAddInfo().trim();
		String tempArr1[] = tempStr1.split("#");
		String tempArr2[] = tempStr2.split("#");
		String tempArr3[] = tempStr3.split("#");
		String tempArr4[] = tempStr4.split("#");
		String tempArr5[] = tempStr5.split("#");
		if(tempStr1.length() >0) {
			if(tempArr1.length >0  ) {
				for (int i = 0; i < tempArr1.length; i++) {
					GoodsInfo tempO = new GoodsInfo();
					tempO.setItName(tempArr1[i]);
					if( tempArr2.length > i ) {
						tempO.setItPrice( Integer.parseInt( tempArr2[i]));						
					}
					if( tempArr3.length > i ) {
						tempO.setItCount(Integer.parseInt( tempArr3[i]));						
					}
					if( tempArr4.length > i ) {
						tempO.setItDetailUrl(tempArr4[i]);						
					}
					if( tempArr5.length > i ) {
						tempO.setItAddInfo(tempArr5[i]);						
					}	
					goodsList.add(tempO);
				}		
			}else {
				return map;
			}
		}
		
		map.put("finishYn", smsLinkInfo.getPayFinishYn()); //  결제여부
		map.put("goodsList", goodsList); // 상품정보
		map.put("sellerInfo", sellerInfo); // 판매자정보
		map.put("destination", destination);// 배송지 정보
		map.put("paymentInfo", pInfo);// 결제정보 
		map.put("cardInfo", info);// 결제정보
		
		return map;
	}
}
