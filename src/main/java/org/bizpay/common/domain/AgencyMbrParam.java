package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AgencyMbrParam {
	// mber
	private String bizCode;
	private String usid;
	private String password;
	private String useAt;
	private String authorCode;
	// mber_detail
	private int mberCode;
	private int mberCodeSn;
	// mber_hist2
	private int sn;
	private String histCode;
	private String histCn;	
	private String creatMberCode;
}
