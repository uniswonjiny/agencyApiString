package org.bizpay.service;

import java.util.HashMap;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.external.OrderStatusInfo;

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
	
}
