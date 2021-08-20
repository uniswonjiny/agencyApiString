package org.bizpay.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.external.OrderStatusInfo;
import org.bizpay.domain.link.LinkSms;
import org.bizpay.domain.link.SmsCardPayment;
import org.bizpay.domain.link.SmsInsert;
import org.bizpay.domain.link.SmsLink;
import org.bizpay.domain.link.SmsPayRequest;

public interface ExternalService {
	public String insertExOrder(ExternalOrderInputParam param) throws Exception;
	public ExternalOrderInputParam selectOrderInfo(long orderNo) throws Exception;
	// 결제전 취소
	public void payPreCancel(PaymentReqParam param)throws Exception;
	public ExternalOrderInputParam payRequest(PaymentReqParam param)throws Exception;
	public void payCancel(ExternalOrderInputParam param)throws Exception;
	// 노티서버 호출 파라미터 버퍼로 스트링으로 보내는 예전방식
	public boolean notiCallParam(ExternalOrderInputParam param)throws Exception;
	// 노티서버 서버 to 서버 http 통신방식
	public boolean notiCallHttp(ExternalOrderInputParam param)throws Exception;
	// 결제되지 않는 정보 취소 처리하기
	public void exOrderCancel(ExternalOrderInputParam param) throws Exception;
	// 외부결제 정보를 조회한다. 단건
	public OrderStatusInfo exOrderInfo(HashMap<String, Object> param )throws Exception;
	// sms 결제정보 조회및 데이터 규격변경
	public SmsLink selectSmsLinkInfo(long id) throws Exception;
	// sms 결제하기
	public void payment(SmsPayRequest param);
	// link상품조회
	public ArrayList<LinkSms> selectLinkSmsInfo(long id) throws Exception;
	// sms 상품 입력
	public long insertSmsGoods(SmsInsert param) throws Exception;
	// sms link 카드 결제 정보
	public SmsCardPayment selectSmsCardPayment(long id) throws Exception;
	// sms 결제완료 정보
	public HashMap<String, Object> smsPayResultInfo(long id) throws Exception;
	
	
	// 전체 상품조회
	
	// 결제취소
	
	// 신용카드 결제처리
	
	
	
}
