package org.bizpay;



import java.util.HashMap;

import org.bizpay.common.domain.ExternalOrderInputParam;
import org.bizpay.common.domain.PaymentReqParam;
import org.bizpay.common.domain.RciptMember;
import org.bizpay.common.util.JwtUtil;
import org.bizpay.domain.ExternalOrderInfo;
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
	ExternalMapper emap;
	
	@Autowired
	ExternalService exSever;
	
	@Test
	void mainTest() throws Exception {
		// 시전 주문 입력
//		ExternalOrderInputParam param = new ExternalOrderInputParam();
//		param.setMberId("unicore");
//		param.setOrderName("큐알테스트2-쫄면");
//		param.setOrderPrice(8120);
//		
//		System.out.println("***********************입력전*******************************************");
//		System.out.println(param.toString());
//		exSever.insertExOrder(param);
//		System.out.println("***********************입력후*****************************************");
//		System.out.println(param.toString()); // 21 // 22
		
		// 이력후 주문 정보추출
//		ExternalOrderInfo info1 = exSever.selectOrderInfo(22);
//		System.out.println(info1.toString());
		
		// 수기결제 요청
		
		PaymentReqParam ppp = new PaymentReqParam();
		ppp.setAmount(500);
		ppp.setCardNo("6243760025280317");
		ppp.setGoodsName("큐알테스트4쫄면");
		ppp.setInstallment("00");
		ppp.setMemberId("B0002A1777"); // 유니코아 송원진
		ppp.setEmail("aaa@bbb.com");
		ppp.setPhoneNumber("01039977736");
		ppp.setExpiration("2507");
		ppp.setPasswd("99");
		ppp.setPidNum("810204");// 주민번호
		exSever.payRequest(ppp);
	}
}
