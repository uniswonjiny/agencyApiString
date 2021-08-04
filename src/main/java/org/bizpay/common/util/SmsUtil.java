package org.bizpay.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
	
	@Value("${naver.short.clientId}")
	private String shortClientId;
	
	@Value("${naver.short.clientSecret}")
	private String shortClientSecret;
	
	@Value("${naver.short.url}")
	private String shortUrl;
	
	// short url 획득
	public String getShortUrl(String apiUrl) {
		try {
			Map<String, String> requestHeaders = new HashMap<>();
			requestHeaders.put("X-Naver-Client-Id", shortClientId);
	        requestHeaders.put("X-Naver-Client-Secret", shortClientSecret);
	        
			URL url = new URL(shortUrl+apiUrl);
			HttpURLConnection con =  (HttpURLConnection)url.openConnection();
			for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
	        
			int responseCode = con.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				InputStreamReader streamReader = new InputStreamReader(con.getInputStream());
				BufferedReader lineReader = new BufferedReader(streamReader);
				StringBuilder responseBody = new StringBuilder();
				String line;
		        while ((line = lineReader.readLine()) != null) {
		        	responseBody.append(line);
		        }
		 
		        JsonElement je = JsonParser.parseString( responseBody.toString() ) ;
		        return je.getAsJsonObject().get("result").getAsJsonObject().get("url").toString();
            } 
		} catch (Exception e) {
			// 에러나면 그냥 없음
			System.err.println(e);
			return "";
		}
		return apiUrl;

	}

	public boolean sendShortSms(String receiverNumber , String title, String msg , String receiverName ) {
		try {
			final String encodingType = "utf-8";
			final String boundary = "____boundary____";
			Gson gson=new Gson();
			HashMap<String, String> sms = new HashMap<String, String>();
			System.out.println("smsIdsmsId : " + smsId);
			sms.put("user_id", smsId); // SMS 아이디
			sms.put("key", key); //인증키
			sms.put("msg", msg); // 메세지 내용
			sms.put("title", title); //  제목
			sms.put("receiver", receiverNumber); // 수신번호
			sms.put("destination", receiverNumber+"|"+receiverName); // 수신인 %고객명% 치환
			sms.put("msg_type", "SMS");
			sms.put("sender", sender); // 발신번호
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
			sms.put("msg", msg); // 메세지 내용
			sms.put("receiver", receiverNumber); // 수신번호
			sms.put("destination", receiverNumber+"|"+receiverName); // 수신인 %고객명% 치환
			sms.put("msg_type", "SMS");
			sms.put("sender", sender); // 발신번호
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
