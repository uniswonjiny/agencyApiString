package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 가맹비수입용
@Getter
@Setter
@ToString
public class AgencySales3 {
	public String bizCode;
	public String bprprr;
	public String createDt;
	public String dealerCmpnm;
	public double dealerJoinAmt;
	public String trgetCmpnm;
	public String agentCmpnm;
	public double agentJoinAmt;
	public String trgetBizCode;
	public String recommendBizCode;
	public double tot; // 가맹비수익
	public String bizType;
}
