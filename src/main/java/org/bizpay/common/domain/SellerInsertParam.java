package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SellerInsertParam {
	private String usid;
	private String memberBizeCode;// 로그인한 비즈코드
	private String memberMberCode;//로그인한 사용자코드
	private String indutyId; 
	private String adres;
	private String mberMobile;
	private String bizCode;
	private float feeRate;
	private String mberName;
	private String bankName;
	private String accountNo;
	private String mberJumi;
	private String email;
	private String depositor; // 예금주
	private String payType;
	private double feeDistributor;
	private double feeAgency;
	private double feeDealer;
	private double feeBank;
	private double limitOne;
	private double limitDay;
	private double limitMonth;
	private double limitYear;
	private int installmentMonths;
	private String bizType; // 사업자구분 r개인 사업자
	private String companyName;
	private String bizNum;
	private String trmnlNo;
	private String pgVan;
	private String pgTrmnlNo;
	private String pgSugiTrmnlNo;
	private String mberPhone;
	private String mbtlnum; // 
	private String sugi;
	private String cashTrmnlNo;
	private String setDevice;
	private String smsAutosend; // 결제완료 SMS전송설정. N 설정안함, Y 설정
	private String pgGb;
	private String vanGb;
	private String setSmsAuth;
	private double smsAuthMoney;
	private String bizTypeName;
	private String bizItem;
	private String memo;
	private String historyMemo;
	private String bankCode;
	private String sugiCertification;
	private float unicoreRate;
	private String recommendBizCode;
	private float recommendDealerRate;
	private float codeRate;
	private float oneLimitRate;
	private float lv1Rate;
	private float lv2Rate;
	private float lv3Rate;
	private String documentReg; // 서류제출
	private String setDevice2; // 장비설정2
}
