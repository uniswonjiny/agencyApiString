package org.bizpay;



import org.bizpay.common.util.JwtUtil;
import org.bizpay.service.AuthService;
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
	
	@Test
	void mainTest() throws Exception {

		System.out.println("=======================================");
		String retStr = service.getJwtKey("testId");
		System.out.println(retStr);
		System.out.println("=========================================");
		service.parsedKey("eyJ0eXAiOiJqd3QgIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJiaXpwYXktYXV0aC1pc3N1ZXIiLCJhdWQiOiJiaXpwYXktYXBwIiwic3ViIjoidGVzdElkIiwiZXhwIjoxNjAwNDA4MTYwfQ.XpYGHr6QDH_GvIDCGh395VC4IwhfGhA9QdQd9uDys2U");
		
	}
}
