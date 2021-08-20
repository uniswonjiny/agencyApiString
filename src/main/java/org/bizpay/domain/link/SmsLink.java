package org.bizpay.domain.link;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsLink {
	private String mberCode;
  	private String mberName;
  	private String companyName;
  	private String mberMobile;
  	private String itName;
  	private String itAddInfo;
  	private String itDetailUrl;
  	private int rciptNo;
  	private int itTotalAmt ;
  	private int itTotalCnt;
  	private String smslinkMemo;
  	private String itPrice;
  	private String itCount;
  	private int installmentMonths;
  	private String sugiCertification;
  	private String payFinishYn;
  	private String addrYn;
  	private int step;
  	private String smsSendPhone;
  	private ArrayList<String> nameList;
  	private ArrayList<String> countList;
  	private ArrayList<String> priceList;
  	private ArrayList<String> infoList;
  	private ArrayList<String> urlList;
}
