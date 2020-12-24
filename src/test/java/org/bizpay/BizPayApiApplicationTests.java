package org.bizpay;

import org.bizpay.common.domain.InqireMberParam;
import org.bizpay.common.util.JwtUtil;
import org.bizpay.service.BPWService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BizPayApiApplicationTests {
	@Autowired
	BPWService bservice;
	@Autowired
	JwtUtil ju;
	@Test
	void contextLoads() {
		
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
