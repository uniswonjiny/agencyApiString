package org.bizpay.common.util;
// 결제 관련 공통 체크사항

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bizpay.domain.LimitInfo;
import org.bizpay.domain.common.SameCardList;
import org.bizpay.exception.ExorderException;
import org.bizpay.mapper.AccountMapper;
import org.bizpay.mapper.ExternalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayCkeck {
	@Autowired
	AccountMapper acMapper;
	
	@Autowired
	ExternalMapper exMapper;
	
	@Autowired 
	CertUtil cUtil;
	
	// 결제금액 제한 확인
	public String limitPayCheck(String mberCode , long price) {
		String retStr = "";
		try {
			LimitInfo limiInfo = acMapper.limitInfo(mberCode);
			// 1회 제한 금액오류
			if (limiInfo.getLimitOne() < price ) {
				retStr = "one";
			}
			// 1일 제한 금액
			if (limiInfo.getLimitDay() < price + limiInfo.getSumDay()) {
				retStr = "day";
			}
			// 1달 제한 금액
			if (limiInfo.getLimitMonth() < price + limiInfo.getSumMonth()) {
				retStr = "month";
			}
			// 1년 제한 금액
			if (limiInfo.getLimitYear() <price  + limiInfo.getSumYear()) {
				retStr = "year";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		}
		return retStr;
	}
	
	// 동일 카드 결제 체크 true 가 동일카드다! cardNumer 은 뒷자리 4개인걸로 함
	// 가격은 쿼리에서 조회하므로 별도 로직 필요없음
	public boolean sameCarkCkeck(String cardNumer , String mberCode , long price) {
		// 10 원 미만 패스
		if(price <10) return false;
		
		// 카드검사용 데이터 설정
		SameCardList samCard = new SameCardList();
		// 오늘날짜 00년00월00일 - yyMMdd
		SimpleDateFormat sdfYYYYMMDD=new SimpleDateFormat("yyMMdd");
		String yymmdd=sdfYYYYMMDD.format(new Date());
		samCard.setDateStr(yymmdd);
		samCard.setMberCode(mberCode);
		samCard.setPrice(price);
		try {
			List<String> list = acMapper.sameCardList(samCard);
			for (String str : list) {
				str =  cUtil.decrypt(str);
				if(str.length() !=14 ) continue;
				str = str.substring(10, 14);
				if(str.equals(cardNumer)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	// 영수증번호
	public String getRciptNo(String mberCode) {
		HashMap<String, Object> tempMap = new HashMap<>();
		tempMap.put("mber_code", mberCode);
		tempMap.put("rcipt_no", "");
		try {
			acMapper.propRciptNO(tempMap);
			return (String) tempMap.get("rcipt_no");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}	
	}
	
	// 상점아이디 가져오기
	public String getStoreId(int mberCode) {
		String storeId = "";
		try {
			HashMap<String, Object> tbMberBasis = exMapper.selectTbMberBasis1(mberCode);
			if(tbMberBasis==null) storeId = "";
			else {
				if ("T".equals(tbMberBasis.get("PAY_TYPE"))) {
					storeId = "2041700460";
				} else if ("B".equals(tbMberBasis.get("PAY_TYPE"))) {
					storeId = "2002307048";
				} else if ("N".equals(tbMberBasis.get("PAY_TYPE"))) {
					storeId = "2552500002";
				} else {
					storeId = "";// 결제처리 실패. 결제 상점설정에 문제가 있습니다
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return storeId;
	}
	
	public String getStoreId(String payType) {
		String storeId = "";
		if ("T".equals(payType)) {
			storeId = "2041700460";
		} else if ("B".equals(payType)) {
			storeId = "2002307048";
		} else if ("N".equals(payType)) {
			storeId = "2552500002";
		} else {
			storeId = "";// 결제처리 실패. 결제 상점설정에 문제가 있습니다
		}		
		return storeId;
	}
	
}
