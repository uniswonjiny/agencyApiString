package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LinkSms {
	private String itId;
	private String itName;
	private int itemCount; // 아이템 개수
	private String itAddInfo;
	private long itPrice;
	private long itemAmt; // 현상품 전체 개격
	private int mberCode;
	private String itDetailUrl; 
  	private String mberName;
  	private String mberMobile;
  	private String companyName;
  	private int installmentMonths;
  	private String sugiCertification;
  	private String allItemYn;
}
