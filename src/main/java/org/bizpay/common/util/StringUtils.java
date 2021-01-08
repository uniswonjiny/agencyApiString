package org.bizpay.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {
	/**
	 * <pre>
	 * 문자열에서 사용하는 특수문자들의 역할을 제거한다.
	 * </pre>
	 * 
	 * @param sourceString
	 *            변환에 사용할 문자열
	 * @return 변환된 문자열
	 */
	public  String escapeSpecialChars(String sourceString) {
		sourceString = sourceString.trim();

		sourceString = sourceString.replaceAll("\r", "\\\\r");
		sourceString = sourceString.replaceAll("\n", "\\\\n");

		return sourceString;
	}

	/**
	 * <pre>
	 * 기능이 제거된 특수문자의 기능을 복원한다.
	 * </pre>
	 * 
	 * @param sourceString
	 *            변환에 사용할 문자열
	 * @return 변환된 문자열
	 */
	public  String unescapeSpecialChars(String sourceString) {
		sourceString = sourceString.trim();
		sourceString = sourceString.replaceAll("\\\\r", "\r");
		sourceString = sourceString.replaceAll("\\\\n", "\n");
		sourceString = sourceString.replaceAll("\\r", "\r");
		sourceString = sourceString.replaceAll("\\n", "\n");

		return sourceString;
	}
	
	/**
	 * 문자열이 한글인지 검사한다.
	 * 
	 * @param string
	 *            검사할 문자열
	 * @return 한글이 아니면 false를 리턴
	 */
	public  boolean isHangul(String string) {
		int strLen = string.length();
		for (int i = 0; i < strLen; i++) {
			char c = string.charAt(i);
			if (c != ' ' && Character.getType(c) != 5) {
				return false;
			}
		}
		return true;
	}
	
	public  boolean isHangulV2(String string) {
		int strLen = string.length();
		for (int i = 0; i < strLen; i++) {
			if(Character.getType(string.charAt(i))== 5) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <pre>
	 * 객체(String 또는 Integer)에서 String 값을 조회하기
	 * </pre>
	 * 
	 * @param obj
	 *            변환에 사용할 객체
	 * @return 변환된 문자열
	 */
	public  String getString(Object obj) {
		if (obj == null)
			return "";

		if (String.class.isInstance(obj)) {
			String tmp = (String) obj;
			if (isNull(tmp)) {
				return "";
			}
			return tmp;
		} else if (Float.class.isInstance(obj)) {
			return "" + ((Float) obj).floatValue();
		} else if (Integer.class.isInstance(obj)) {
			return "" + ((Integer) obj).intValue();
		} else {
			return "" + obj;
		}
	}

	/**
	 * <pre>
	 * 객체(String 또는 Integer)에서 String 값을 조회하기
	 * </pre>
	 * 
	 * @param obj
	 *            변환에 사용할 객체 기본값 객체의 기본값.
	 * @return 변환된 문자열
	 */
	public  String getString(Object obj, String 기본값) {
		if(null == obj) return 기본값;
		String tmp;
		try {
			tmp = getString(obj);
			if ("".equals(tmp)) {
				return 기본값;
			}
		} catch (Exception e) {
			tmp = 기본값;
		}
		
		return tmp;
	}

	/**
	 * 문자열이 유효 (null이 아니고 공백이 아닐 경우)한지 검사한다.
	 * 
	 * @param src
	 *            검사 할 문자열
	 * @return 문자열 유효 여부
	 */
	public  boolean isNull(String src) {
		return src == null || src.trim().length() == 0
				|| "null".equalsIgnoreCase(src.trim());
	}

	public  String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				writer.flush();
			} catch (Exception e) {
				return "";
			} finally {
				try {
					is.close();
				} catch (Exception e2) {
				}
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	// 객체(String 또는 Integer)에서 integer 값을 조회하기
	public  int getInt(Object obj) {
		try {
			if (obj == null)
				return 0;

			if (String.class.isInstance(obj)) {
				if (((String) obj).trim().length() == 0)
					return 0;
				String tmp = (String) obj;

				return Integer.parseInt(tmp.replaceAll(",", ""));
			} else if (Float.class.isInstance(obj)) {
				return (int) (((Float) obj).floatValue());
			} else if (Double.class.isInstance(obj)) {
				return (int) (((Double) obj).doubleValue());
			} else if (Long.class.isInstance(obj)) {
				return (int) (((Long) obj).longValue());
			} else {
				return ((Integer) obj).intValue();
			}
		} catch (Exception e) {
			return 0;
		}
	}
	
	public  Double getDouble(Object obj) {
		try {
			if (obj == null)
				return Double.parseDouble("0");
			
			if (String.class.isInstance(obj)) {
				if (((String) obj).trim().length() == 0)
					return Double.parseDouble("0");
				String tmp = (String) obj;
				
				return Double.parseDouble(tmp.replaceAll(",", ""));
			} else if (Float.class.isInstance(obj)) {
				return Double.parseDouble("" + ((Float) obj).floatValue());
			} else if (Double.class.isInstance(obj)) {
				return ((Double) obj).doubleValue();
			} else {
				return Double.parseDouble(getString(obj));
			}
		} catch (Exception e) {
			return Double.parseDouble("0");
		}
	}
	
	public  float getFloat(Object obj) {
		try {
			if (obj == null)
				return 0;

			if (String.class.isInstance(obj)) {
				if (((String) obj).trim().length() == 0)
					return 0;
				String tmp = (String) obj;

				return Float.parseFloat(tmp.replaceAll(",", ""));
			} else {
				return Float.parseFloat(obj.toString());
			}
		} catch (Exception e) {
			return 0;
		}
	}
	
	public  long getLong(String str) {
		if (str == null || str.length() == 0)
			return 0;

		try {
			if (str.indexOf(".") == -1) {
				return Long.parseLong(str);
			} else {
				return 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	public  String getDate() {
		String date;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		date = mYear + "";
		date += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		date += (mDay + "").length() == 1 ? "0" + mDay : mDay;

		return date;
	}

	public  String getDate1() {
		String date;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		date = mYear + "";
		date += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		date += (mDay + "").length() == 1 ? "0" + mDay : mDay;

		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);
	}
	
	public  String getDateNTime() {
		String date;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);

		date = mYear + "";
		date += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		date += (mDay + "").length() == 1 ? "0" + mDay : mDay;
		
		date += " ";
		
		date += (mHour + "").length() == 1 ? "0" + mHour : mHour;
		date += ":";
		date += (mMinute + "").length() == 1 ? "0" + mMinute : mMinute;
		date += ":";
		date += (mSecond + "").length() == 1 ? "0" + mSecond : mSecond;
		return date;
	}
	
	public  String getDateNTime02() {
		String date;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);

		date = mYear + "";
		date += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		date += (mDay + "").length() == 1 ? "0" + mDay : mDay;
		
		date += "_";
		
		date += (mHour + "").length() == 1 ? "0" + mHour : mHour;
//		date += ":";
		date += (mMinute + "").length() == 1 ? "0" + mMinute : mMinute;
//		date += ":";
		date += (mSecond + "").length() == 1 ? "0" + mSecond : mSecond;
		return date;
	}
	
	public  String getDateNTime03() {
		String date;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);

		date = mYear + "";
		date += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		date += (mDay + "").length() == 1 ? "0" + mDay : mDay;
		
//		date += "_";
		
		date += (mHour + "").length() == 1 ? "0" + mHour : mHour;
//		date += ":";
		date += (mMinute + "").length() == 1 ? "0" + mMinute : mMinute;
//		date += ":";
		date += (mSecond + "").length() == 1 ? "0" + mSecond : mSecond;
		return date;
	}
	
	public  String getDateNTimeAfterMinute(int afterMinute) {
		String date;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, afterMinute);
		
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);

		date = mYear + "";
		date += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		date += (mDay + "").length() == 1 ? "0" + mDay : mDay;
		
//		date += " ";
		
		date += (mHour + "").length() == 1 ? "0" + mHour : mHour;
//		date += ":";
		date += (mMinute + "").length() == 1 ? "0" + mMinute : mMinute;
//		date += ":";
		date += (mSecond + "").length() == 1 ? "0" + mSecond : mSecond;
		
		return date;//YYYYMMDDHHMMSS
	}
	
	/*public  String getTime() {
		String date = "";
		Calendar c = Calendar.getInstance();
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);
		
		date += (mHour + "").length() == 1 ? "0" + mHour : mHour;
		date += ":";
		date += (mMinute + "").length() == 1 ? "0" + mMinute : mMinute;
		date += ":";
		date += (mSecond + "").length() == 1 ? "0" + mSecond : mSecond;
		return date;
	}*/
	
	public  String getTimeV2() {
		String date = "";
		Calendar c = Calendar.getInstance();
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);
		
		date += (mHour + "").length() == 1 ? "0" + mHour : mHour;
		date += (mMinute + "").length() == 1 ? "0" + mMinute : mMinute;
		date += (mSecond + "").length() == 1 ? "0" + mSecond : mSecond;
		return date;
	}

	/**
	 * 일자 형태로 변환한다.
	 * 
	 * @param string
	 *            일자 적용을 위한 문자열
	 * @param shortYear
	 *            2자리 연도 여부
	 * @return 일자 적용이 된 문자열
	 */
	public  String makeDate(String string, boolean shortYear) {
		String result = string;
		if (string.length() == 8) {
			result = shortYear ? string.substring(2, 4) : string
					.substring(0, 4);
			result += "-";
			result += string.substring(4, 6);
			result += "-";
			result += string.substring(6, 8);
		} else if (string.length() == 16) {
			return makeDate(string.substring(0, 8), false);
		} else if (string.length() == 6) {
			return makeDate("20" + string.substring(0, 6), false);
		}
		return result;
	}
	/**
	 * 일자 형태로 변환한다.
	 * 
	 * @param string
	 *            일자 적용을 위한 문자열
	 * @param shortYear
	 *            2자리 연도 여부
	 * @return 일자 적용이 된 문자열
	 */
	public  String makeDate02(String string, boolean shortYear) {
		String result = string;
		if (string.length() == 8) {
			result = shortYear ? string.substring(2, 4) : string
					.substring(0, 4);
			result += "/";
			result += string.substring(4, 6);
			result += "/";
			result += string.substring(6, 8);
		} else if (string.length() == 16) {
			return makeDate(string.substring(0, 8), false);
		} else if (string.length() == 6) {
			return makeDate("20" + string.substring(0, 6), false);
		}
		return result;
	}
	/**
	 * 일자 형태로 변환한다.
	 * 
	 * @param string
	 *            14자리 일자 적용을 위한 문자열
	 * @return 일자 적용이 된 문자열
	 */
	public  String makeDate02(String string) {
		String result = string;
		result = string.substring(0, 4);
		result += "-";
		result += string.substring(4, 6);
		result += "-";
		result += string.substring(6, 8);
		result += " ";
		result += string.substring(8, 10);
		result += ":";
		result += string.substring(10, 12);
		result += ":";
		result += string.substring(12, 14);

		return result;
	}

	public  String getStringUrlEncode(String str){
		String result = URLEncoder.encode(str);
		return result;
	}
	
	public  String makeLongStr(String string) {
		if(null == string) return "0";
		
		try {
			if (string.indexOf(".") >= 0) {
				double value = Double.parseDouble(string);
				String strValue = String.format("%.0f", value);
				long longValue = Long.parseLong(strValue);
				return longValue + "";
			}
			return string;
		} catch (Exception e) {
			return "0";
		}
	}
	
	public  String makeStringWithComma(int value) {
		try {
			DecimalFormat format = new DecimalFormat("#,###,###,###,##0");
			String tmpStr = format.format(value);

			return tmpStr;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "0";
	}

	public  String makeStringWithComma(long value) {
		try {
			DecimalFormat format = new DecimalFormat("#,###,###,###,##0");
			String tmpStr = format.format(value);

			return tmpStr;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "0";
	}
	
	public  String makeStringWithComma(double value) {
		try {
			DecimalFormat format = new DecimalFormat("#,###,###,###,##0");
			String tmpStr = format.format(value);

			return tmpStr;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "0";
	}
	
	/**
	 * 문자열에 통화적용을 위해 컴마를 표기한다.
	 * 
	 * @param obj
	 *            통화적용을 위한 obj
	 * @return 통화적용이 된 문자열
	 */
	public  String 금액표기(Object obj) {
		if (obj != null)
			return makeStringWithComma(getString(obj, "0"));
		else
			return "0";
	}
	/**
	 * 문자열에 통화적용을 위해 컴마를 표기한다.
	 * 
	 * @param obj
	 *            통화적용을 위한 obj
	 * @param defStr
	 *            기본값
	 * @return 통화적용이 된 문자열
	 */
	public  String 금액표기(Object obj, String defStr) {
		if (obj != null)
			return makeStringWithComma(getString(obj, defStr));
		else
			return "0";
	}
	
	/**
	 * 문자열에 통화적용을 위해 컴마를 표기한다.
	 * 
	 * @param string
	 *            통화적용을 위한 문자열
	 * @return 통화적용이 된 문자열
	 */
	public  String makeStringWithComma(String string) {
		if (string != null)
			return makeStringWithComma(safeString(string.trim()), false);
		else
			return "0";
	}

	/**
	 * 문자열에 통화적용을 위해 컴마를 표기하고 '원'을 붙여 반환한다.
	 * 
	 * @param string
	 *            통화적용을 위한 문자열
	 * @return 통화적용이 된 문자열
	 */
	public  String makeStringWithCommaWithSimbol(String string) {
		String tmpStr = makeStringWithComma(string);
		if (tmpStr.length() == 0) {
			return "0 원";
		} else {
			return tmpStr + " 원";
		}
	}

	/**
	 * 문자열에 통화적용을 위해 컴마를 표기한다.
	 * 
	 * @param string
	 *            통화적용을 위한 문자열
	 * @param ignoreZero
	 *            값이 0일 경우 공백을 리턴한다.
	 * @return 통화적용이 된 문자열
	 */
	public  String makeStringWithComma(String string, boolean ignoreZero) {
		if (string.length() == 0) {
			return "";
		}
		
		string = string.replace(",", "");
		
		try {
			if (string.indexOf(".") >= 0) {
				double value = Double.parseDouble(string);
				if (ignoreZero && value == 0) {
					return "";
				}
				String strValue = String.format("%.0f", value);
				long longValue = Long.parseLong(strValue);
				DecimalFormat format = new DecimalFormat("#,###,###,###,##0");
				return format.format(longValue);
			} else {
				long value = Long.parseLong(string);
				if (ignoreZero && value == 0) {
					return "";
				}
				DecimalFormat format = new DecimalFormat("#,###,###,###,##0");
				return format.format(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * 문자열이 유효(null이 아니고 공백이 아닐 경우)한지 검사한다.
	 * 
	 * @param src
	 *            검사 할 문자열
	 * @return 문자열 유효 여부
	 */
	public  boolean isEmpty(String src) {
		return src == null || src.length() == 0;
	}

	public  boolean isEmptys(String[] src) {
		if (src == null || src.length == 0) {
			return true;
		}

		int strLength = src.length;
		for (int i = 0; i < strLength; i++) {
			if (isEmpty(src[i])) {
				return true;
			}
		}

		return false;
	}
	
	public  String safeString(String str) {
		if (str != null)
			return isEmpty(str) ? "" : str;
		else
			return "";
	}
	
	public  String getAfter3Day(){
		String today;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		
		today = mYear + "";
		today += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		today += (mDay + "").length() == 1 ? "0" + mDay : mDay;

		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date date;
		Date after3 = new Date();
		String dateString = null;

		try {
			date = format.parse(today.substring(0, 4) + "-"
					+ today.substring(4, 6) + "-" + today.substring(6, 8));
			
			after3.setTime(date.getTime() + (3 * 1000 * 60 * 60 * 24)); 

			java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			dateString = format1.format(after3);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateString;
	}
	
	public  String getBeforeNSecond(String baseDate, int second){
		baseDate = baseDate.replace(" ", "");
		
		if(baseDate.length() < 14){
			return null;
		}
		
		int 년, 월, 일, 시, 분, 초;
		년 = getInt(baseDate.substring(0, 4));
		월 = getInt(baseDate.substring(4, 6)) - 1;
		일 = getInt(baseDate.substring(6, 8));
		시 = getInt(baseDate.substring(8, 10));
		분 = getInt(baseDate.substring(10, 12));
		초 = getInt(baseDate.substring(12, 14));
		
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.YEAR, 년);
		cal.set(Calendar.MONTH, 월);
		cal.set(Calendar.DATE, 일);
		cal.set(Calendar.HOUR_OF_DAY, 시);
		cal.set(Calendar.MINUTE, 분);
		cal.set(Calendar.SECOND, 초);
		
		cal.add(Calendar.SECOND, -second);
		
		년 = cal.get(Calendar.YEAR);
		월 = cal.get(Calendar.MONTH) + 1;
		일 = cal.get(Calendar.DATE);
		시 = cal.get(Calendar.HOUR_OF_DAY);
		분 = cal.get(Calendar.MINUTE);
		초 = cal.get(Calendar.SECOND);
		
		String retDate = "" + 년;
		retDate += (월 + "").length() == 1 ? "0" + 월 : 월;
		retDate += (일 + "").length() == 1 ? "0" + 일 : 일;
		retDate += (시 + "").length() == 1 ? "0" + 시 : 시;
		retDate += (분 + "").length() == 1 ? "0" + 분 : 분;
		retDate += (초 + "").length() == 1 ? "0" + 초 : 초;
		
		return retDate;//"" + 년 + 월 + 일 + 시 + 분 + 초;
	}
	
	public  String get어제날짜(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		Calendar c1 = Calendar.getInstance(); 
		c1.add(Calendar.DATE, -1); // 오늘날짜로부터 -1 
		String yesterday = sdf.format(c1.getTime()); // String으로 저장 
		return yesterday;
	}
	
	public  String get그제날짜(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		Calendar c1 = Calendar.getInstance(); 
		c1.add(Calendar.DATE, -2); // 오늘날짜로부터 -2 
		String yesterday = sdf.format(c1.getTime()); // String으로 저장 
		return yesterday;
	}
	
	public  String getPrev5Day(){
		String today;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		
		today = mYear + "";
		today += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		today += (mDay + "").length() == 1 ? "0" + mDay : mDay;
		
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date date;
		Date yesterday = new Date();
		String dateString = null;
		
		try {
			date = format.parse(today.substring(0, 4) + "-"
					+ today.substring(4, 6) + "-" + today.substring(6, 8));
			
			if (date == null)
				throw new IllegalStateException("today is null");
			int 달별마지막일 = 30;
			int 빼야할날 = 5;
			if(mMonth + 1 == 1
					|| mMonth + 1 == 2
					|| mMonth + 1 == 4
					|| mMonth + 1 == 6
					|| mMonth + 1 == 9
					|| mMonth + 1 == 11
					){
				달별마지막일 = 31;
				빼야할날 = 4;
			}else if(mMonth + 1 == 3){
				int year = mYear;
				if( ( (year % 4 == 0) && (year % 100 != 0) ) || (year % 400 == 0) ) {
					//System.out.println("윤년이다!!");
					달별마지막일 = 29;
					빼야할날 = 6;
				}else{
					달별마지막일 = 28;
					빼야할날 = 7;
				}
			}
			if(mDay > 5) yesterday.setTime(date.getTime() - (5 * 1000 * 60 * 60 * 24)); 
			else {
				yesterday.setTime(date.getTime() - (((long) 빼야할날 * 1000 * 60 * 60 * 24)));
			}
			
			java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			dateString = format1.format(yesterday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateString;
	}
	
	public  String getPrev15Day(){
		String today;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		
		today = mYear + "";
		today += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		today += (mDay + "").length() == 1 ? "0" + mDay : mDay;
		
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date date;
		Date yesterday = new Date();
		String dateString = null;
		
		try {
			date = format.parse(today.substring(0, 4) + "-"
					+ today.substring(4, 6) + "-" + today.substring(6, 8));
			
			if (date == null)
				throw new IllegalStateException("today is null");
			int 달별마지막일 = 30;
			int 빼야할날 = 15;
			if(mMonth + 1 == 1
					|| mMonth + 1 == 2
					|| mMonth + 1 == 4
					|| mMonth + 1 == 6
					|| mMonth + 1 == 9
					|| mMonth + 1 == 11
					){
				달별마지막일 = 31;
				빼야할날 = 14;
			}else if(mMonth + 1 == 3){
				int year = mYear;
				if( ( (year % 4 == 0) && (year % 100 != 0) ) || (year % 400 == 0) ) {
					//System.out.println("윤년이다!!");
					달별마지막일 = 29;
					빼야할날 = 16;
				}else{
					달별마지막일 = 28;
					빼야할날 = 17;
				}
			}
			if(mDay > 15) yesterday.setTime(date.getTime() - (15 * 1000 * 60 * 60 * 24)); 
			else {
				yesterday.setTime(date.getTime() - (((long) 빼야할날 * 1000 * 60 * 60 * 24)));
			}
			
			java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			dateString = format1.format(yesterday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return dateString;
	}
	
	public  String getPrevMonth() {

		String today;
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		today = mYear + "";
		today += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		today += (mDay + "").length() == 1 ? "0" + mDay : mDay;

		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
		java.util.Date date;
		Date yesterday = new Date();
		String dateString = null;

		try {
			date = format.parse(today.substring(0, 4) + "-"
					+ today.substring(4, 6) + "-" + today.substring(6, 8));
			if (date == null)
				throw new IllegalStateException("today is null");
			int 달별마지막일 = 30;
			if(mMonth + 1 == 1
					|| mMonth + 1 == 2
					|| mMonth + 1 == 4
					|| mMonth + 1 == 6
					|| mMonth + 1 == 9
					|| mMonth + 1 == 11
					){
				달별마지막일 = 31;
			}else if(mMonth + 1 == 3){
				int year = mYear;
				if( ( (year % 4 == 0) && (year % 100 != 0) ) || (year % 400 == 0) ) {
				    //System.out.println("윤년이다!!");
					달별마지막일 = 29;
				}else{
					달별마지막일 = 28;
				}
			}
			yesterday.setTime(date.getTime() - ((long) 달별마지막일 * 1000 * 60 * 60 * 24));

			java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			dateString = format1.format(yesterday);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateString;
	}
	
	public  String getPrevMonth(int prevMonth) {
		String date;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -prevMonth);
		
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		date = mYear + "";
		date += ((mMonth + 1) + "").length() == 1 ? "0" + (mMonth + 1)
				: (mMonth + 1);
		date += (mDay + "").length() == 1 ? "0" + mDay : mDay;

		return date;
	}
	
	
	public  String getLastday_inMonth(String year, String month) {
		int mYear = Integer.valueOf(year);
		int mMonth = Integer.valueOf(month);

		String dateString = "";

		try {
			int 달별마지막일 = 31;
			if (mMonth == 4 || mMonth == 6 || mMonth == 9 || mMonth == 11) {
				달별마지막일 = 30;
			} else if (mMonth == 2) {
				if (((mYear % 4 == 0) && (mYear % 100 != 0))
						|| (mYear % 400 == 0)) {
					달별마지막일 = 29;
				} else {
					달별마지막일 = 28;
				}
			}
			dateString += 달별마지막일;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dateString;
	}

	public  String getDashedTelNo(String telNo) {
		if(telNo.length() == 9)
			telNo = telNo.replaceFirst("(\\d{2})(\\d{3})(\\d+)", "$1-$2-$3");
		else if(telNo.length() == 10) {
			if(telNo.charAt(0) == '0' && telNo.charAt(1) == '2')
				telNo = telNo.replaceFirst("(\\d{2})(\\d{4})(\\d+)", "$1-$2-$3");
			else 
//			if(telNo.charAt(1) == '1')
				telNo = telNo.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
//			else
//				telNo = telNo.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
		} else if(telNo.length() == 11)
			telNo = telNo.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3");
		return telNo;
	}
	
	public  String getDashedJuminNo(String juminNo) {
		juminNo.replace("-", "");
		juminNo.replace(" ", "");
		
		if(juminNo.length() == 14)
			juminNo = juminNo.replaceFirst("(\\d{7})(\\d+)", "$1-$2");
		return juminNo;
	}
	
	public  boolean isTelNo(String telno) {
		final String regex = "^((01[0|1|6|9|7])|(02)|(0\\d{2}))[-](\\d{3}|\\d{4})[-](\\d{4})$";
		return telno.matches(regex);
	}
	
	public  boolean isTelNo(String telno1, String telno2, String telno3) {
		return isTelNo(telno1 + "-" + telno2 + "-" + telno3);
	}
	
	public  int StrHexToInt(String str) {
		byte[] b2 = new byte[str.length()];
		int temp = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.substring(i, i + 1).equals("A")
					|| str.substring(i, i + 1).equals("a"))
				b2[i] = (byte) 0x0A;
			else if (str.substring(i, i + 1).equals("B")
					|| str.substring(i, i + 1).equals("b"))
				b2[i] = (byte) 0x0B;
			else if (str.substring(i, i + 1).equals("C")
					|| str.substring(i, i + 1).equals("c"))
				b2[i] = (byte) 0x0C;
			else if (str.substring(i, i + 1).equals("D")
					|| str.substring(i, i + 1).equals("d"))
				b2[i] = (byte) 0x0D;
			else if (str.substring(i, i + 1).equals("E")
					|| str.substring(i, i + 1).equals("e"))
				b2[i] = (byte) 0x0E;
			else if (str.substring(i, i + 1).equals("F")
					|| str.substring(i, i + 1).equals("f"))
				b2[i] = (byte) 0x0F;
			else
				b2[i] = (byte) (Integer.parseInt(str.substring(i, i + 1)) & 0x0F);
			
			int b = 4 * (str.length() - 1 - i);
			int a =  b2[i] << b;
			temp = temp + a;
		}
		return temp;
	}
	
	/**
	 * 화면표기할 내용.
	 * @param 표기하려는내용 숫자,하이픈,=의 정보만 허용된다.
	 * @return 변환된내용. 잘못된 값일경우 그대로 리턴시킨다.
	 */
	public  String MarkForCreditCard(String 표기하려는내용){
		String temp=표기하려는내용;
		temp=nvl(temp,"");
		temp=temp.replace("-","");
		temp=temp.split("=")[0];
		
		int 자리수=temp.length();
		
		if(자리수==16){ //(VISA/MASTER)
			String gap1=temp.substring(0,4);
			String gap2=temp.substring(4,6);
			String gap3=temp.substring(12,16);
			
			return gap1+"-"+gap2+"**-****-"+gap3;
			
		}else if(자리수==15){ //(AMEX)
			
			String gap1=temp.substring(0,4);
			String gap2=temp.substring(4,6);
			String gap3=temp.substring(11,15);
			
			return gap1+"-"+gap2+"***-*"+gap3;
		}else if(자리수==14){ //(다이너스)
			String gap1=temp.substring(0,4);
			String gap2=temp.substring(4,6);
			String gap3=temp.substring(12,14);
			
			return gap1+"-"+gap2+"**-****-"+gap3; // ex)3616-53**-****-87
		}else {
			return 표기하려는내용; //그대로리턴..
		}
	}
	
	public  String MarkForBankNumber(String 표기하려는내용){
		String temp=표기하려는내용;
		temp=nvl(temp,"");
		temp=temp.replace("-","");

		int 자리수=temp.length();
		
		if(자리수 <= 7){
			return 표기하려는내용;
		}
		
		String gap1=temp.substring(0,3);
		String gap2=temp.substring(7,자리수);
		
		return gap1+"****"+gap2;
	}
	
	public  String nvl(String source, String defaultValue) {
        if(source == null)
            return defaultValue;
        else
            return source;
    }
	
	//nCalcVal  - 처리할 값(소수점 이하 데이터 포함)
	//nDigit    - 연산 기준 자릿수(오라클의 ROUND함수 자릿수 기준)
	//           - 2:십단위, -1:원단위, 0:소수점 1자리
	//             1:소수점 2자리, 2:소수점 3자리, 3:소수점 4자리, 4:소수점 5자리 처리
	public  Double 반올림(int nDigit, Double nCalcVal ){
		if (nDigit < 0) {
			nDigit = - (nDigit);
			nCalcVal = Math.round(nCalcVal / Math.pow(10, nDigit)) * Math.pow(10, nDigit);
		} else {
			nCalcVal = Math.round(nCalcVal * Math.pow(10, nDigit)) / Math.pow(10, nDigit);
		}
		
		return nCalcVal;
	}
	
	//nCalcVal  - 처리할 값(소수점 이하 데이터 포함)
	//nDigit    - 연산 기준 자릿수(오라클의 ROUND함수 자릿수 기준)
	//           - 2:십단위, -1:원단위, 0:소수점 1자리
	//             1:소수점 2자리, 2:소수점 3자리, 3:소수점 4자리, 4:소수점 5자리 처리
	public  Double 절삭(int nDigit, Double nCalcVal ){
		if (nDigit < 0) {
			nDigit = -(nDigit);
			nCalcVal = Math.floor(nCalcVal / Math.pow(10, nDigit)) * Math.pow(10, nDigit);
		} else {
			nCalcVal = Math.floor(nCalcVal * Math.pow(10, nDigit)) / Math.pow(10, nDigit);
		}
		
		return nCalcVal;
	}
	
	public  String getTimeStamp() {

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyy-MM-dd(hh:mm:ss)";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}
	
	public  boolean isPhoneNumber(String inputStr){
		if(inputStr.startsWith("010")||
			inputStr.startsWith("011")||
			inputStr.startsWith("016")||
			inputStr.startsWith("017")||
			inputStr.startsWith("018")||
			inputStr.startsWith("019")){ //전화번호로 간주.
			return true;
		}
		
		return false;
	}
	
	public  int getGapDay(String startDay) {
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		
		startCal.set(getInt(startDay.substring(0,4))
				, getInt(startDay.substring(4,6)) - 1
				, getInt(startDay.substring(6,8))
				);
		
		long start = startCal.getTimeInMillis();
		long end = endCal.getTimeInMillis();
		long gapTime = (end - start) / 1000;
		
		long hour = gapTime / 60 / 60;
		long day = hour / 24;
		
		int retVal = getInt(day);
		
		return retVal;
	}
	
	
	//======================================
	// byte <-> string간 변경 함수
	//======================================
	
	// 한자리숫자시 앞에 '0' 넣어주기
	public  String pad(int c)
	{
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	
	//해당하는 문자열(str1)에 대해서 입력된 길이만큼 부족한 길이를 왼쪽부터 지정된 문자열(str2)로 채운다.
	public  String leftPad(String str1, String str2, int size)
	{
		int len = str1.length();
		for (int i = 0; i < size - len; i++)
		{
			str1 = str2 + str1;
		}
		
		return str1;
	}
	

	//해당하는 문자열(str1)에 대해서 입력된 길이만큼 부족한 길이를 오른쪽으로 지정된 문자열(str2)로 채운다.
	public  String rightPad(String str1, String str2, int size)
	{
		int len = str1.length();
		for (int i = 0; i < size - len; i++)
		{
			str1 = str1 + str2;
		}
		
		return str1;
	}

	String byteToString(byte _buf[])
    {
        return byteToString(_buf, 0, _buf.length);
    }

    String byteToString(byte _buf[], int _idx, int _len)
    {
    	String m_strUnicode = "ksc5601";
    	StringBuffer m_sbErrorMsg = new StringBuffer();
        
        String str = null;
        if(_buf == null)
            return str;
        try
        {
            str = new String(_buf, _idx, _len, m_strUnicode);
        }
        catch(UnsupportedEncodingException uee)
        {
            m_sbErrorMsg.append((new StringBuilder("\uC774 \uC7A5\uCE58\uC5D0\uC11C ")).append(uee.getMessage()).append(" \uC778\uCF54\uB529\uC740 \uC9C0\uC6D0\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4.\n").toString());
            return null;
        }
        return str;
    }

	byte[] stringToByte(String _str)
    {
		String m_strUnicode = "ksc5601";
    	StringBuffer m_sbErrorMsg = new StringBuffer();
    	
        byte buf[] = (byte[])null;
        try
        {
            buf = _str.getBytes(m_strUnicode);
        }
        catch(UnsupportedEncodingException uee)
        {
            m_sbErrorMsg.append((new StringBuilder("\uC774 \uC7A5\uCE58\uC5D0\uC11C ")).append(uee.getMessage()).append(" \uC778\uCF54\uB529\uC740 \uC9C0\uC6D0\uD558\uC9C0 \uC54A\uC2B5\uB2C8\uB2E4.\n").toString());
            return null;
        }
        return buf;
    }
	
	public  String getDeviceName(String setDeviceName)
	{
		String retVal = setDeviceName;
		
		if(setDeviceName.equals("SUGI_NORMAL")){
			retVal = "수기_비인증";
		}
		else if(setDeviceName.equals("SUGI_CERTIFICATION")){
			retVal = "수기_인증";
		}
		else if(setDeviceName.equals("500")){
			retVal = "SC500";
		}
		else if(setDeviceName.equals("500B")){
			retVal = "SC500B";
		}
		else if(setDeviceName.equals("7201")){
			retVal = "다날(LC7201)";
		}
		else if(setDeviceName.equals("7602")){
			retVal = "다날(LC7602)";
		}
		else if(setDeviceName.equals("7203")){
			retVal = "다날(LC7203)";
		}
		else if(setDeviceName.equals("7211")){
			retVal = "다날(LC7211)";
		}
		else if(setDeviceName.equals("DANAL")){
			retVal = "다날";
		}
		else if(setDeviceName.equals("Q2")){
			retVal = "Q2";
		}
		
		return retVal;
	}
	
	private  String[] BANKNAME = {
		"국민",//1
		"기업",//2
		"농협",//3
		"신한",//4 조흥
		"조흥",//4-1 조흥
		"우체국",//5
		"SC",//6
		"KEB",//7 외환
		"하나",//7-1 외환
		"KEB하나",//7-2 외환
		"외환",//7-3 외환
		"씨티",//8 한미
		"한미",//8-1 한미
		"우리",//9
		"경남",//10
		"광주",//11
		"대구",//12
		"도이치",//13
		"부산",//14
		"산업",//15
		"수협",//16
		"전북",//17
		"제주",//18
		"새마을금고",//19
		"신용협동조합",//20
		"HSBC",//21
		"상호저축은행중앙회",//22
		"BOA",//23
		"제이피모간체이스",//24
		"카카오뱅크",//25
		"케이뱅크"//26
	};
	
	private  String[] BANKCODE = {
		"004",//1 국민
		"003",//2 기업
		"011",//3 농협
		"088",//4 신한,구조흥
		"088",//4-1 구조흥
		"071",//5 우체국
		"023",//6 SC
		"081",//7 KEB하나,구외환
		"081",//7-1 KEB하나,구외환
		"081",//7-2 KEB하나,구외환
		"081",//7-3 KEB하나,구외환
		"027",//8 씨티,구한미
		"027",//8-1 구한미
		"020",//9 우리
		"039",//10 경남
		"034",//11 광주
		"031",//12 대구
		"055",//13 도이치
		"032",//14 부산
		"002",//15 산업
		"007",//16 수협
		"037",//17 전북
		"035",//18 제주
		"045",//19 새마을금고
		"048",//20 신용협동조합
		"054",//21 HSBC
		"050",//22 상호저축은행중앙회
		"060",//23 BOA
		"057",//24 제이피모간체이스
		"090",//25 카카오뱅크
		"089"//26 케이뱅크
	};
	
	public  String getBankCode(String bank_name){
		String bank_code = "";
		
		for(int i=0; i<BANKNAME.length; i++){
			if(bank_name.contains(BANKNAME[i])){
				return BANKCODE[i];
			}
		}
		
		return bank_code;
	}
	
	public  String getBefore2MinuteTime(){
		String date = "";
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -2);
		
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMinute = c.get(Calendar.MINUTE);
		int mSecond = c.get(Calendar.SECOND);
		
		date += (mHour + "").length() == 1 ? "0" + mHour : mHour;
		date += (mMinute + "").length() == 1 ? "0" + mMinute : mMinute;
		date += (mSecond + "").length() == 1 ? "0" + mSecond : mSecond;
		return date;
	}
	
	public  boolean is토일월10시이전(){
		String 오늘날짜 = getDate();
		System.out.println("오늘날짜 : " + 오늘날짜);
		
		if(is토요일() || is일요일() || is월요일10시이전()){
			return true;
		}
		
		return false;
	}
	
	public  boolean is토요일(){
		Calendar c = Calendar.getInstance();
		
		int 요일 = c.get(Calendar.DAY_OF_WEEK); // 일요일 = 1
		System.out.println("요일 : " + 요일);
		
		if(7 == 요일){
			System.out.println("-오늘은 토요일-");
			return true;
		}
		
		return false;
	}
	public  boolean is일요일(){
		Calendar c = Calendar.getInstance();
		
		int 요일 = c.get(Calendar.DAY_OF_WEEK); // 일요일 = 1
		System.out.println("요일 : " + 요일);
		
		if(1 == 요일){
			System.out.println("-오늘은 일요일-");
			return true;
		}
		
		return false;
	}
	
	public  boolean is월요일10시이전(){
		Calendar c = Calendar.getInstance();
		
		int 요일 = c.get(Calendar.DAY_OF_WEEK); // 일요일 = 1
		System.out.println("요일 : " + 요일);
		
		if(2 == 요일){
			System.out.println("-오늘은 월요일-");
			//10시 이전인가?
			int mHour = c.get(Calendar.HOUR_OF_DAY);
			if(10 > mHour) {
				System.out.println("-10시이전-");
				return true;
			}
		}
		
		return false;
	}
}
