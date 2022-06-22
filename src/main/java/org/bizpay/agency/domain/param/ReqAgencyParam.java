package org.bizpay.agency.domain.param;

import lombok.Data;

// 대리점등록
@Data
public class ReqAgencyParam {
    private String bossBizCode;
    private String companyName;
    private String memberName;
    private String mobileNumber;
    private String email;
    private String startDt;
    private String endDt;
    private int startNo;
    private int endNo;
    private String status;
}
