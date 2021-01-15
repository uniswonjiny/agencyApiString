package org.bizpay.common.util;
//이전소스에서 이동함
import org.springframework.stereotype.Component;
import java.text.DecimalFormat;
@Component
public class DataFormatUtil {

	public static final int ALIGN_LEFT = 0;
	public static final int ALIGN_RIGHT = 1;
	public static final int ALIGN_CENTER = 2;

	public  String format(long num, String pattern){
		DecimalFormat formatter = new DecimalFormat(pattern);
		return formatter.format(num);
	}

	public  String format(double num, String pattern){
		DecimalFormat formatter = new DecimalFormat(pattern);
		return formatter.format(num);
	}

	public  String format(String paramString, String pattern, int align){
		int length = pattern.length();

    int paramStringLength = 0;	
    	try {
    		paramStringLength = paramString.getBytes("euc-kr").length;
    	}
    	catch (Exception localException) {}
    	if (length <= paramStringLength) {
    		return paramString;
    	}
    	if (align == 0) {
    		for (int i = paramStringLength; i < length; i++) {
    			paramString = paramString + pattern.charAt(i);
    		}
    		return paramString;
    		}
    	if (align == 1)  {
    		int tempPatternLength = length - paramStringLength;
    		String temp = "";

    		for (int i = 0; i < tempPatternLength; i++) {
    			temp = temp + pattern.charAt(i);
    		}
    		return temp + paramString;
    	}if (align == 2){
    		if (paramString.length() > pattern.length()) {
    			return paramString;
    		}
    		int leftMargin = (length - paramString.length()) / 2;
    		int rightMargin = 0;

    		if (leftMargin * 2 == length - paramString.length()) rightMargin = leftMargin;
    		else {
    			rightMargin = leftMargin + 1;
    		}

    		String pattern1 = pattern.substring(0, leftMargin);
    		String pattern2 = pattern.substring(pattern.length() - rightMargin, pattern.length());
    		String pattern3 = pattern1 + paramString + pattern2;

    		return pattern3;
    	}

    	throw new IllegalArgumentException("#align is invalid");
	}
}