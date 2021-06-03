package org.bizpay.mapper;

import java.util.HashMap;

import org.bizpay.common.domain.DelngAdiParam;
import org.bizpay.common.domain.DelngCredtParam;
import org.bizpay.common.domain.DelngParam;
import org.bizpay.common.domain.ExternalOrderInputParam;

public interface ExternalMapper {
	public int insertExOrder(ExternalOrderInputParam param) throws Exception;
	// 외부 결제정보 확인
	public ExternalOrderInputParam selectOrderInfo(long orderNo) throws Exception;
	// 외부결제 정보 확인 2 외부제공주문번호 로 확인
	public ExternalOrderInputParam selectOrderInfo2(ExternalOrderInputParam param) throws Exception;
	
	// 영수증번호 획득
	public int getRciptNo(int mberCode ) throws Exception;

	// 사용가능 사용자인지 확인
	public HashMap<String, Object> selectTbBberIdCheck(String mberId) throws Exception;
	//
	public String selectTbMberDetailSn(int mberCode)throws Exception;
	
	// selectTbMberBasis1
	public HashMap<String, Object> selectTbMberBasis1(int mberCode) throws Exception;
	// 영수증번호 획득및 영수증번호 자동 업데이트동시 실행
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
	public HashMap<String, Object>selectExorderInfo(HashMap<String, Object> param)throws Exception;

	
}
