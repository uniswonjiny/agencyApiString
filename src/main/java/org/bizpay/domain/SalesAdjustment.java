package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalesAdjustment {
	public String cancelProc;
	public String cardDeleteYn; // Y N 
	public String cardDeleteNm; // 정상 취소 여부 -- 화면에서 처리하도록 변경예정 무의미한 값
	public String cmpnm; 
	public String confmdt;
	public int confmNo;
	public String delngDt;
	public String delngGb; // 판매구분
	public String delngSeCode;
	public double feeRate;
	public int mberCode;
	public String mberName;
	public String payType;
	public double tot;
	public String usid;
	public double fee;
	public double pay;
	public String bigo;
	public String cancelMber;
	public String cancelDt;
	
	public int mberCodeSn;
	public int rciptNo;
	public String pgRciptNo; // 원거래번호
	public String tId;//결제시터미널id
	
}
