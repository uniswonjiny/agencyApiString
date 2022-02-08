package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DelngCredtParam {
	private long mberCode;
    private String mberCodeSn;
    private String rciptNo;
    private String cardNo;
    private String instlmtMonth;
    private String issueCmpnyCode;
    private String issueCmpnyNm;
    private String puchasCmpnyCode;
    private String puchasCmpnyNm;
    private String cdrsrNo;
    private String cardType;
    private String pgVanGb;
    private String tId;
    private String pgRciptNo;
    private String gbInfo;
    private String vanPgComp;
}
