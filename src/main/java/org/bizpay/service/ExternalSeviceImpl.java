package org.bizpay.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Hashtable;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import org.bizpay.common.domain.DelngAdiParam;
import org.bizpay.common.domain.DelngCredtParam;
import org.bizpay.common.domain.DelngParam;
import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.external.OrderStatusInfo;
import org.bizpay.common.util.CertUtil;
import org.bizpay.common.util.EncryptUtil;
import org.bizpay.common.util.KSPayMsgBean;
import org.bizpay.common.util.StringUtils;
import org.bizpay.domain.LimitInfo;
import org.bizpay.domain.MemberInfo;
import org.bizpay.domain.OrderErrorType;
import org.bizpay.exception.ExorderException;
import org.bizpay.exception.SqlErrorException;
import org.bizpay.mapper.AccountMapper;
import org.bizpay.mapper.AuthMapper;
import org.bizpay.mapper.ExternalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import lombok.extern.java.Log;

@Log
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

	// 운영배포시 주석 해제있음
	@Override
	@Transactional
	public String insertExOrder(ExternalOrderInputParam param) throws Exception {
		log.info("외부결제연동 결제정보 부분만 입력 delng 아니다");
		// 금액이 숫자인지 확인
		if (!param.getOrderPrice().matches("[+-]?\\d*(\\.\\d+)?")) {
			return "A007";
		}

		// 1. 요청한 값이 정상적인지 검사
		if ("".equals(param.getNextUrl())) {
			return "A002";
		}
		if ("".equals(param.getNotiUrl())) {
			return "A003";
		}
		if ("".equals(param.getExorderNo())) {
			return "A004";
		}
		if ("".equals(param.getMberId())) {
			return "A005";
		}
		if ("".equals(param.getOrderName())) {
			return "A006";
		}
		if (Long.valueOf(param.getOrderPrice()) < 100) {
			return "A007";
		}
		if ("".equals(param.getPkHash())) {
			return "A008";
		}

		// 해시 키값 확인 -- 운영배포시 주석 해제
		if (param.getPkHash() == null || param.getPkHash().length() < 1) {
			return "A008";
		}
		String temp = param.getMberId() + param.getOrderName() + param.getOrderPrice() + param.getExorderNo()
				+ "unicore";
		String hashkey = eUtil.encryptSHA256(temp);
		log.info(param.getPkHash());
		log.info(hashkey);
		if (!hashkey.equals(param.getPkHash())) {
			return "A009";
		}

		// 2. 이전 호출한 외부 주문상태 확인 - biz 의 내용아님
		ExternalOrderInputParam reP = exMapper.selectExOrderNo(param);
		if (reP != null) {
			if ("0000".equals(reP.getStatus())) {
				log.info("이전 주문만 완료후 후속 결제가 안된 경우");
				return String.valueOf(reP.getSeq());
			} else {
				log.info("이전 주문관련 결제등이 발생해버린 경우");
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
		int mberCode = -1;
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
				mberCode = Integer.parseInt(mberChk.get("MBERCODE").toString());
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

			ht = ksBean.sendCardMsg(KSNET_PG_IP, KSNET_PG_PORT, storeId,
					// "2999199999", // 테스트이후 해제함
					param.getKsnetRcipt(), // 주문번호
					"", param.getPidNum(), param.getEmail(), param.getOrderName(), // 상품명
					param.getPhoneNumber(), // 핸드폰번호
					"K", // pKeyInType X(12) KEY-IN유형(K:직접입력,S:리더기사용입력)
					"1", // pInterestType X( 1) *일반/무이자구분 1:일반 2:무이자
					param.getPTrackII(), // -필수- pTrackII X(40) *TrackII(KEY-IN방식의 경우 카드번호=유효기간[YYMM])
					param.getInstallment(), Long.toString(param.getAmount()), param.getPasswd(), param.getCardPNo());

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
				// 큐알이 아닌경우 나중에!!!!!! 수정필요!!!!!!! 
				// 이부분이 원래는 사용자결제 형식에서 가져온 부분인데 외부결제가 엮이면서 문제인 지점으로 보임 
				// 
				if ("N".equals(tbMberBasis.get("SUGI_CERTIFICATION").toString())) {
					delngParam.setPaymentDevice("QR_SUGI_NORMAL");
				} else if ("Y".equals(tbMberBasis.get("SUGI_CERTIFICATION").toString())) {
					delngParam.setPaymentDevice("QR_SUGI_CERTIFICATION");
				} else
					delngParam.setPaymentDevice("QR_SUGI_NORMAL");
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
			System.out.println("*********************");
			System.out.println(oet.toString());
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

		// 결제이후 1일 이상 지났는지 확인
		if (exInfo.getCancelPeriod() > 1) {
			throw new ExorderException("C008");
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
//            	BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
//				String buffer = null;
//				while((buffer = in.readLine())!=null){
//					result += buffer;
//				}
//				in.close();
//				Gson gson=new Gson();
//				HashMap<String,Object> dataMap=gson.fromJson(result,HashMap.class);
//				if("100".equals(dataMap.get("code"))) {
//					return true;
//				}else {
//					return false;
//				}

//                ResponseHandler<String> handler = new BasicResponseHandler();
//                String body = handler.handleResponse(response);
//                System.out.println("[RESPONSE] requestHttpJson() : " + body);
//                // 어떤형태로 보낼지 모르니 일단 그냥 100포함여부로 확인후 수정한다
//                
//                if("100".equals(body)) {
//                	return true;
//                }else return false;
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

}
