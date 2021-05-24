package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExternalOrderInputParam {
	private String nextUrl;
	private String notiUrl;
	private String pkHash;
	private String status;
	private String orderName;
	private long orderPrice;
	private long seq;
	private String exorderNo;
	private String mberId;
	private int mberCode;
	private String mberCodeSn;
	private String orderType;// 결제유형 -- card , naver , kakao m ,mobile
	private String orderDetail;// 결제유형 상세 
	private String confmNo; //승인번호
	private String rciptNo; //영수증번호 -- 승인번호 + 영수증번호 + exorderNo + mberId 로 유니크한 구분값으로 사용한다.
	private String sugiCertification;// 수기결제 추가 인증필요유무
	private String mberName; // 주문주체 외부요청자
	private String createDt; // 요청일자
	private String updateDt; // 수정일자
	private String bigo; // 사유등의 내용추가
	private String email;
	private String mobileNum;
	
}
