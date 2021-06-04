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
    private float mberFee;
    private String mberFeeAmt;
    private String payAmt;
    private String approvalConfirm;
    private int deviceSeqNo;
    private String delngPayType;
    private String mberId;
    private String cancelDt;
    private String cardDeleteYn;
    private String storeId;
    private String toSwiptStatus;// 스와이프 송수신 상태
    private String paymentDevice; // 결제사용기계
}