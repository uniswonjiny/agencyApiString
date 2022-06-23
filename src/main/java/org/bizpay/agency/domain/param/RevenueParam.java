package org.bizpay.agency.domain.param;

import lombok.Data;

@Data
public class RevenueParam {
    public String userId;
    public String startDt;
    public String endDt;
    public int startNo;
    public int endNo;
    public int dealerKind;
    private String type;
}
