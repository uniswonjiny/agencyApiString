package org.bizpay.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.bizpay.common.domain.DelngAdiParam;
import org.bizpay.common.domain.DelngCredtParam;
import org.bizpay.common.domain.DelngParam;
import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.external.OrderStatusInfo;
import org.bizpay.domain.link.Destination;
import org.bizpay.domain.link.LinkSms;
import org.bizpay.domain.link.SellerInfo;
import org.bizpay.domain.link.SmsCardPayment;
import org.bizpay.domain.link.SmsInsert;
import org.bizpay.domain.link.SmsLink;
import org.bizpay.domain.link.SmsPayRequest;

public interface ExternalMapper {
	public int insertExOrder(ExternalOrderInputParam param) throws Exception;
	// 외부 결제정보 확인
	public ExternalOrderInputParam selectOrderInfo(long orderNo) throws Exception;
	// 외부결제 정보 확인 2 외부제공주문번호 로 확인
	public ExternalOrderInputParam selectOrderInfo2(ExternalOrderInputParam param) throws Exception;
	
	// 영수증번호 획득
	//public int getRciptNo(int mberCode ) throws Exception;

	// 사용가능 사용자인지 확인
	public HashMap<String, Object> selectTbBberIdCheck(String mberId) throws Exception;
	//
	public String selectTbMberDetailSn(int mberCode)throws Exception;
	
	// selectTbMberBasis1
	public HashMap<String, Object> selectTbMberBasis1(int mberCode) throws Exception;
	// 영수증번호 획득및 영수증번호 자동 업데이트동시 실행 -- 결제 과련 부분으로 옮기는중!
	public  void propRciptNO(  HashMap<String, Object> pam)throws Exception;
	// delng 입력
	public int insertDelng(DelngParam param)throws Exception;
	// delngCredt 입력
	public int insertDelngCredt(DelngCredtParam param)throws Exception;
	// delng_adi 입력
	public int insertDelngAdi(DelngAdiParam param)throws Exception;
	// 이전 주문 정보 확인
	public ExternalOrderInputParam selectExOrderNo(ExternalOrderInputParam param) throws Exception;
	// 외부 결제정보 수정
	public int updateExOrder(ExternalOrderInputParam param) throws Exception;
	// 결제정보확인 delng
	public DelngParam selectDelngInfo(DelngParam param) throws Exception;
	// 신용카드 결제정보확인
	public DelngCredtParam selectDelngCredt(HashMap<String, Object> map) throws Exception;
	// 부가정보 확인
	public DelngAdiParam selectDelngAdi(DelngAdiParam param)throws Exception;	
	// 결제정보 수정
	public int updateDelng(DelngParam param) throws Exception;
	// 외부 연동 결제정보 조회
	public OrderStatusInfo selectExorderInfo(HashMap<String, Object> param)throws Exception;
	// 입금내역확인
	public Integer selectReqAmt(HashMap<String, Object> param)throws Exception;
	// 출금내역확인
	public ArrayList<HashMap<String, Object> > selectTblAmt(HashMap<String, Object> param)throws Exception;
	// sms 최초 결제요청 정보 가져오기
	public SmsLink selectSmsLinkInfo(long id) throws Exception;
	// sms 결제 정보 수정
	public int updateSmsLink(SmsPayRequest param) throws Exception;
	// LinkSms 결제 정보 - 상품이 특정된경오
	public LinkSms selectLinksmsInfo (long id) throws Exception;
	// link 결제 일때 전체상품인경우 - 상품이 특정되지 않은경우
	public ArrayList<LinkSms> selectLinksmsList (long mberCode) throws Exception;
	// link 결제전 sms 결제 정보 입력용
	public int insertMberSmsLinkLink(SmsInsert param) throws Exception;
	// sms link 결제 카드 결제 정보조회
	public SmsCardPayment selectSmsCardPayment(long id) throws Exception;
	// 배송지 정보 저장
	public int insertDestination(Destination param) throws Exception;
	// sms  배송지 정보
	public Destination selectOrderDestination(int mberCode , int reciptNo ) throws Exception;
}
