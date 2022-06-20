package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TblAtmParam {
	private long mberCode;
	private long inoutNo;
	private String inoutCode;
	private long reqAmt;
	private int balance;
	private String reqDt;
	private String reqTime;
	private int charge;
	private String reqResult;
	private String bank;
	private String account;
	private String depositor;
	private String bigo;
	private String bizCode;
	private long salesTotAmt;
	private float salesFeePer;
	private int salesFeeAmt;
	private String salesDt;
	private String salesTime;
	private String salesRciptNo;
}
