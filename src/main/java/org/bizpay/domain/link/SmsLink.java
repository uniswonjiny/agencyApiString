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
  	private String mberMobile;
  	private String itName;
  	private String itAddInfo;
  	private String itDetailUrl;
  	private int itTotalAmt ;
  	private int itTotalCnt;
  	private String smslinkMemo;
  	private String itPrice;
  	private String itCount;
  	private int installmentMonths;
  	private String sugiCertification;
  	private String payFinishYn;
  	private ArrayList<String> nameList;
  	private ArrayList<String> countList;
  	private ArrayList<String> priceList;
  	private ArrayList<String> infoList;
  	private ArrayList<String> urlList;
}
