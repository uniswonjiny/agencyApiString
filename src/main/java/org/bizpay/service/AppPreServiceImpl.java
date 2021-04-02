package org.bizpay.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.bizpay.common.util.CertUtil;
import org.bizpay.exception.AppPreException;
import org.bizpay.mapper.AppPreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
@Service
public class AppPreServiceImpl implements AppPreService {
	@Autowired
	Gson gson;
	
	@Autowired
	AppPreMapper apMapper;
	
	@Autowired
	CertUtil cUtil;

	@Override
	public  HashMap<String, Object>  login(HttpServletRequest req) throws AppPreException , Exception {
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("status","OK");
		try {
			
			String key = req.getParameter("key");
			String usid = req.getParameter("induty_id");
			String password = req.getParameter("password");
			if(key == null || usid == null || password == null ) {
				throw new AppPreException("#필수정보누락", "LOGIN_FAIL");
			}
			
			key = key.trim();
			usid = usid.trim();
			password = password.trim();
			
			if(key.length() <1l || usid.length() <1 || password.length() <1 ) {
				throw new AppPreException("#필수정보누락", "LOGIN_FAIL");
			}
			
			key = new String(key.getBytes("iso-8859-1"),"utf-8");
			HashMap<String, String> map= gson.fromJson(cUtil.decrypt(  key  ), HashMap.class);
			
			String mberCode=map.get("mberCode");
			String mberCodeSn=map.get("mberCodeSn");
			String appCode=map.get("appCode");
			
			// 앱정보 조회
			if(apMapper.selectAppCodeCount(appCode ) >0) {
				 HashMap<String , String> map1 = apMapper.selectMber(usid);
				 if(map1.size() <1) {
					 throw new AppPreException("존재하지 않는 판매자ID입니다.", "LOGIN_FAIL");
				 }
				 if(!"Y".equals(map1.get("USE_AT"))) {
					 throw new AppPreException("서비스이용이 제한되었습니다", "LOGIN_FAIL");
				 }
				 
				 String codedPassword = map1.get("PASSWORD");
				 codedPassword = cUtil.decrypt(codedPassword);
				 if(!codedPassword.equals( password) ) {
					 throw new AppPreException("암호가 맞지 않습니다", "LOGIN_FAIL");
				 }
				 
				 retMap.put("installment",apMapper.selectInstallment(mberCode));
				 
				 String bizCode= apMapper.getBizCode(mberCode);
				 HashMap<String , Object> bizCodeMap = apMapper.selectTbBiz(bizCode);
				 HashMap<String , Object> mberBasisMap = apMapper.selectMberBasis(mberCode);
				 HashMap<String , Object> trUnicoreMap = apMapper.trUnicore();
				 retMap.put("pg_sugi_trmnl_no", mberBasisMap.get("PG_SUGI_TRMNL_NO"));
				 retMap.put("sugi", mberBasisMap.get("SUGI"));
				 retMap.put("sugi_certification", mberBasisMap.get("SUGI_CERTIFICATION"));
				 retMap.put("cash_trmnl_no", mberBasisMap.get("CASH_TRMNL_NO"));
				 if("0000092".equals(bizCode) && "".equals(mberBasisMap.get("SET_DEVICE"))) {
					 retMap.put("set_device", "CB" );
				 } else retMap.put("set_device", mberBasisMap.get("SET_DEVICE"));
				 retMap.put("sms_autosend", mberBasisMap.get("SMS_AUTOSEND"));
				 retMap.put("set_sms_auth", mberBasisMap.get("SET_SMS_AUTH"));
				 retMap.put("sms_auth_money", mberBasisMap.get("SMS_AUTH_MONEY"));
				 retMap.put("pay_type", mberBasisMap.get("PAY_TYPE"));
				 retMap.put("calculate_type", mberBasisMap.get("CALCULATE_TYPE"));
				 retMap.put("account_no", cUtil.decrypt((String)mberBasisMap.get("ACCOUNT_NO")));
				 retMap.put("bank_name", mberBasisMap.get("BANK_NAME"));
				 retMap.put("bank_code", mberBasisMap.get("BANK_CODE"));
				 retMap.put("depositor", mberBasisMap.get("DEPOSITOR"));
				 retMap.put("limit_one", mberBasisMap.get("LIMIT_ONE"));
				 retMap.put("limit_day", mberBasisMap.get("LIMIT_DAY"));
				 retMap.put("limit_month", mberBasisMap.get("LIMIT_MONTH"));
				 retMap.put("limit_year", mberBasisMap.get("LIMIT_YEAR"));
				 
				 // trmnl_no 구분지점
				 if( !"".equals(mberBasisMap.get("TRMNL_NO")) ) {
					 retMap.put("trmnl_no", mberBasisMap.get("TRMNL_NO"));
					 retMap.put("pg_van", mberBasisMap.get("PG_VAN"));
					 retMap.put("van_gb", mberBasisMap.get("VAN_GB"));
				 }else {
					 retMap.put("trmnl_no", bizCodeMap.get("TRMNL_NO"));
					 if(!"".equals( bizCodeMap.get("TRMNL_NO")) ) {
						 retMap.put("pg_van", bizCodeMap.get("PG_VAN"));
						 retMap.put("van_gb", bizCodeMap.get("VAN_GB")); 
					 }else {
						 retMap.put("pg_van", trUnicoreMap.get("PG_VAN"));
						 retMap.put("van_gb", trUnicoreMap.get("VAN_GB"));
					 }
				 }
				 // pg_trmnl_no 구분지점 --- mberPGTrmnlNo
				 if( !"".equals(mberBasisMap.get("PG_TRMNL_NO")) ) {
					 retMap.put("pg_trmnl_no", mberBasisMap.get("PG_TRMNL_NO"));
					 // retMap.put("pg_van", mberBasisMap.get("pg_van"));mberBasisMap 이미 쿼리에서 널처리함으로 불필요함 위에서 이미입력됨
					 retMap.put("van_gb", mberBasisMap.get("VAN_GB")); 
				 }else {
					 if("".equals(bizCodeMap.get("PG_TRMNL_NO"))){
						 retMap.put("pg_trmnl_no", trUnicoreMap.get("PG_TRMNL_NO"));
						 retMap.put("pg_gb", trUnicoreMap.get("PG_GB"));
					 }else {
						 retMap.put("pg_van", bizCodeMap.get("PG_VAN"));
						 retMap.put("pg_gb", bizCodeMap.get("PG_GB"));
					 }
				 }
				 // 알수 없는 값을 덮어버리는 코드는 삭제 처리함 원 소스와비교  van_gb   pg_gb 	
			}else {
				throw new AppPreException("BizPay이용 불가능한 앱입니다" , "LOGIN_FAIL");
			}
			return retMap;
		} catch (AppPreException e) {
			System.err.println(e.getMessage());
			throw new AppPreException(e.getMessage() , e.getCode());
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new AppPreException("로그인시스템에 문제가 있습니다" , "LOGIN_FAIL");
		}
	}
}
