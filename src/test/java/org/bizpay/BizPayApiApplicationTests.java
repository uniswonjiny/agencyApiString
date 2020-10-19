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
			String key = ju.getJwtKey("unicoure");
			System.out.println("********************************************************************************************");
			System.out.println(key);
			System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////");
			boolean ffff = ju.parsedKey("eyJ0eXAiOiJqd3QgIiwiYWxnIjoiSFMyNTYifQ.eyJpc3MiOiJiaXpwYXktYXV0aC1pc3N1ZXIiLCJhdWQiOiJiaXpwYXktYXBwIiwic3ViIjoidW5pY291cmUiLCJleHAiOjE2MDEyNzE4ODh9.3_OW2paOxpBh9SBQ7xHdci9dWQYkMERqZj5Qb7imNCs");
			System.out.println(ffff);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
