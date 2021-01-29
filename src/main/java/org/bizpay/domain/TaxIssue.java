package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaxIssue {
	private String adres;// "서울시 구로구 디지털로30길28 1218호"
	private String bizNum;// "820-81-00121"
	private double cardFee;// "8,250"
	private String cnt;// "1"
	private String companyName;// "주)유니코아"
	private String email;// "ef4ecb823f3cd3cc3d2d5fcb365ff420"
	private double feeErn;// "22,000"
	private String mberJumi;// "710328"
	private String mberName;// "유니코아"
	private double splpc;// "550,000"
	private double splpc1;// "27,500"
	private double tot;// "30,250"
	private double totFee;// "30,250"
	private String usid;// "AW1721911"
	private double vat;// "2,750"
}
