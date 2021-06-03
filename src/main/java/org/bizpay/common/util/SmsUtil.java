package org.bizpay.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;


import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;



import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
@PropertySource("classpath:sms.properties")
public class SmsUtil {
	@Value("${sms.id}")
	private String smsId;
	
	@Value("${sms.key}")
	private String key;
	
	@Value("${sms.url}")
	private String sms_url;
	
	@Value("${sms.sender}")
	private String sender;

 
	
	// 단문만 1명만이다.
	public boolean sendShortSms(String receiverNumber ,  String msg , String receiverName ) {
		try {
			final String encodingType = "utf-8";
			final String boundary = "____boundary____";
			Gson gson=new Gson();
			HashMap<String, String> sms = new HashMap<String, String>();
			System.out.println("smsIdsmsId : " + smsId);
			sms.put("user_id", smsId); // SMS 아이디
			//sms.put("user_id", "unicore2020"); // SMS 아이디
			sms.put("key", key); //인증키
			//sms.put("key", "4dwg8f36zlj7uyu2jio6o7jhz2b5pwei"); //인증키
			sms.put("msg", msg); // 메세지 내용
			sms.put("receiver", receiverNumber); // 수신번호
			sms.put("destination", receiverNumber+"|"+receiverName); // 수신인 %고객명% 치환
			sms.put("msg_type", "SMS");
			//sms.put("destination", "01039977736|송원진"); // 수신인 %고객명% 치환
			sms.put("sender", sender); // 발신번호
			//sms.put("title", "16000174"); // 발신번호
			//sms.put("sender", "16000174"); // 발신번호
			//sms.put("rdate", ""); // 예약일자 - 20161004 : 2016-10-04일기준
			//sms.put("rtime", ""); // 예약시간 - 1930 : 오후 7시30분
			//sms.put("testmode_yn", "N"); // Y 인경우 실제문자 전송X , 자동취소(환불) 처리
			//sms.put("title", ""); //  LMS, MMS 제목 (미입력시 본문중 44Byte 또는 엔터 구분자 첫라인)
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setBoundary(boundary);
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.setCharset(Charset.forName(encodingType));
			for(Iterator<String> i = sms.keySet().iterator(); i.hasNext();){
				String key = i.next();
				builder.addTextBody(key, sms.get(key)
						, ContentType.create("Multipart/related", encodingType));
			}
			HttpEntity entity = builder.build();
			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(sms_url);
			post.setEntity(entity);
			HttpResponse res = client.execute(post);
			String result = "";
			if(res != null){
				BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), encodingType));
				String buffer = null;
				while((buffer = in.readLine())!=null){
					result += buffer;
				}
				in.close();
			}
			HashMap<String,Object> dataMap=gson.fromJson(result,HashMap.class);
			System.out.println(dataMap.toString());
			if("1".equals(dataMap.get("result_code"))) return true;
			else return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

}
