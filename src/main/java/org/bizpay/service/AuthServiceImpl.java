package org.bizpay.service;

import java.util.List;

import org.bizpay.common.domain.LoginParam;
import org.bizpay.common.util.CertUtil;
import org.bizpay.domain.BizInfo;
import org.bizpay.domain.DealerInfo;
import org.bizpay.domain.MemberInfo;
import org.bizpay.exception.AuthErrorException;
import org.bizpay.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
@Log
@PropertySource("classpath:auth.properties")
@Service
public class AuthServiceImpl implements AuthService {
	@Value("${jwt.secret}")
	private String auth;
	
	@Autowired
	AuthMapper aMapper;
	
	@Autowired
	CertUtil cert;
	
	@Override
	public String loginKey(String userId, String password) throws Exception {
		log.info("로그인및 인증키생성 : " + auth);
		
		return null;
	}

	@Override
	public BizInfo bizInfo(String bizCode) throws Exception {
		log.info("사업자 정보 추출");
		return aMapper.bizInfo(bizCode);
	}

	@Override
	public List<DealerInfo> dealerList(String bizCode) throws Exception {
		log.info("딜러정보 목록 추출");
		return aMapper.dealerList(bizCode);
	}

	@SuppressWarnings("unused")
	@Override
	public MemberInfo loginConfirm(LoginParam param) throws Exception {
		MemberInfo info =  aMapper.userInfo(param.getUserId());
		if(info == null ) {	
			
			throw new AuthErrorException("id");
		}
		
		String comparePassWord = cert.decrypt(info.getPassword());
		if(!comparePassWord.equals(param.getPassword() ) ) {
			throw new AuthErrorException("password");
		}
		
		if("Y".equals(info.getUseAt())) {
			if("AUTHOR_MNGR".equals(info.getAuthorCode()) ||  "AUTHOR_DEALER".equals(info.getAuthorCode()) ) {
				 return info;
			}else {
				throw new AuthErrorException("grade");
			}
		}else {
			throw new AuthErrorException("no");
		}

	}

	@Override
	public MemberInfo memberInfo(String userId) throws Exception {
		return  aMapper.userInfo(userId);
	}

}
