package org.bizpay.service;

import java.util.List;

import org.bizpay.domain.BizInfo;
import org.bizpay.domain.DealerInfo;
import org.bizpay.domain.MemberInfo;

public interface AuthService {
	// 로그인성공유무및 인증키생성
	public String loginKey(String userId , String password) throws Exception;
	// 사업자정보
	public BizInfo bizInfo(String bizCode) throws Exception;
	// 딜러정보
	public List<DealerInfo> dealerList(String bizCode) throws Exception;
	// 단순 로그인 확인
	public MemberInfo loginConfirm(String userId, String password) throws Exception;
	// 사용자 정보확인
	public MemberInfo memberInfo(String userId) throws Exception;
	// 사용자 Id 확인
	public int memberIdChk(String userId) throws Exception;
	// 사업자 번호 확인
	public int biznoChk(String bizno) throws Exception;
}
