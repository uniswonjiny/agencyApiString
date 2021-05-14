package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DelngParam {
	private int mberCode;
    private String mberCodeSn;
    private String rciptNo;
    private String vanCode;
    private String appCode;
    private String delngSeCode;
    private String confmNo;
    private String confmDt;
    private String confmTime;
    private long splpc;
    private long vat;
    private int trgetMberCode;
    private String trgetMberCodeSn;
    private String trgetRciptNo;
    private String bigo;
    private String goodNm;
    private String goodCnt;
    private String custPhone;
    private String bizrno;
    private long mberFee;
    private long mberFeeAmt;
    private long payAmt;
    private String approvalConfirm;
    private int deviceSeqNo;
    private String delngPayType;
}
