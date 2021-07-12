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
		String nameList = "상품1#상품2#상품3";
		ArrayList<String> arrNameList = new ArrayList<String>();
	for (String item : nameList.split("#")) {
		arrNameList.add(item);
		}
		System.out.println(arrNameList.toString());
		
		
//		Hashtable xht = ksBean.sendCardCancelMsg(
//				KSNET_PG_IP, 				// -필수- ipaddr  X(15)   *KSNET_IP(개발:210.181.28.116, 운영:210.181.28.137)  
//				KSNET_PG_PORT,			// -필수- port   9( 5)   *KSNET_PORT(21001)  
//				"2552500002", 								// -필수- pStoreId  X(10)   *상점아이디(개발:2999199999, 운영:?)  
//				"K", 							// -필수- pKeyInType   X(12)  KEY-IN유형(K:직접입력,S:리더기사용입력)  
//				"178270066440"						// -필수- pTransactionNo  X( 1)  *거래번호(승인응답시의 KEY:1로시작되는 12자리숫자)  
//			);
//		
//		System.out.println(xht.toString());
//		ExternalOrderInputParam param = new ExternalOrderInputParam();
//		
//		//smsUtil.sendShortSms("01039977736", "noti 서버오류"+"\n주문번호 : A4123875157845487\n주문영:피시방A세트포인트구매", "큐알거래");
//		
//		String temp= "B0002A1777필수옵션 1개50060b737bf0f3ec733b804c8f7unicore";
//		
//		String temm = eUtil.encryptSHA256(temp);
//		
//		System.out.println(temm);
				
//		param.setConfmNo("00314439");
//		param.setExorderNo("exS0007");
//		param.setMberId("B0002A1777");
//		exSever.payCancel(param);
//		ExternalOrderInputParam exInfo = emap.selectOrderInfo2(param);
//		System.out.println(exInfo.toString());
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
		
//		PaymentReqParam ppp = new PaymentReqParam();
//		ppp.setAmount(500);
//		ppp.setCardNo("6243760025280317");
//		ppp.setGoodsName("큐알테스트4쫄면");
//		ppp.setInstallment("00");
//		ppp.setMemberId("B0002A1777"); // 유니코아 송원진
//		ppp.setEmail("aaa@bbb.com");
//		ppp.setPhoneNumber("01039977736");
//		ppp.setExpiration("2507");
//		ppp.setPasswd("99");
//		ppp.setPidNum("810204");// 주민번호
//		exSever.payRequest(ppp);
		
//		String aaaa = eUtil.encryptSHA256("상품테스트5000");
//		System.out.println(aaaa);
		//ExternalOrderInputParam param = new ExternalOrderInputParam();
		
//		exSever.payCancel(param);
//		boolean falf = smsUtil.sendShortSms("01039977736", "테스트중", "이름");
//		System.out.println(falf);
		
		//smsUtil.mailSender();
	}
}
