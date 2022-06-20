package org.bizpay.agency.domain.result;

import lombok.Data;

@Data
public class SettlementInfo {
    private int cclDt;
    private String cmpnm;
    private String bizType;
    private int mberDelngFee;
    private int recommandSaleTotal;
    private int dealerRegFee;
    private int totalFee;
    private int totAmtOption01;
    private int totAmtOption02;
    private int forwardAmt;
    private int modifyAmt;
    private int sendAmt;
    private String taxInvoiceYn;
    private String sendYn;
    private String dealerId;
}
