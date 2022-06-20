package org.bizpay.agency.domain.result;

import lombok.Data;

@Data
public class TransactionInfo {
    private int no;
    private String confmDt;
    private String confmTime;
    private String companyName;
    private int splpc;
    private int vat;
    private String cmpnm;
    private String bprprr;
    private String mberName;
    private String bizCode;
    private String creatDt;
    private String payType;
    private String delngSeCode;
    private String confmNo;
    private String instlmtMonth;
    private String issueCmpnyNm;
    private String rciptNo;
    private String cardNo;
}
