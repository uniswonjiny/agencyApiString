package org.bizpay.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Param;
import org.bizpay.common.domain.DelngAdiParam;
import org.bizpay.common.domain.DelngCredtParam;
import org.bizpay.common.domain.DelngParam;
import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.RciptMember;
import org.bizpay.domain.ExternalOrderInfo;

public interface ExternalMapper {
	public int insertExOrder(ExternalOrderInputParam param) throws Exception;
	public ExternalOrderInfo selectOrderInfo(long orderNo) throws Exception;
	// 사용자 이용가능유무 확인
	public int selectMberCnt(String mberId) throws Exception;
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
}
