package org.bizpay;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.RciptMember;
import org.bizpay.common.util.EncryptUtil;
import org.bizpay.common.util.JwtUtil;
import org.bizpay.common.util.KSPayMsgBean;
import org.bizpay.common.util.SmsUtil;
import org.bizpay.mapper.ExternalMapper;
import org.bizpay.service.AuthService;
import org.bizpay.service.ExternalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
public class PTest {
	@Autowired
	JwtUtil service;
	
	@Autowired
	SmsUtil smsUtil;
	
	@Autowired
	ExternalMapper emap;
	
	@Autowired
	ExternalService exSever;
	
	@Autowired
	EncryptUtil eUtil;
	
	@Autowired
	KSPayMsgBean ksBean;
	public static final String KSNET_PG_IP = "210.181.28.137";//"210.181.28.137";	//-필수- ipaddr X(15)   *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)
	public static final int KSNET_PG_PORT = 21001;		
	
	@Test
	void mainTest() throws Exception {
		
		String str = smsUtil.getShortUrl("https://play.google.com/store/apps/details?id=kr.co.linkapp.swipe.bizpay");
		System.out.println(str);
	
	}
}
