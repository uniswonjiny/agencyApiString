package org.bizpay.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;
@Component
public class TimeUtil {
	// 포맷지정 날짜 형태 변형
	public String sdfA(String dt , String format){
		try {
			SimpleDateFormat sdfType=new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat tempSdf = new SimpleDateFormat(format);
			return tempSdf.format(sdfType.parse(dt));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
		
}
