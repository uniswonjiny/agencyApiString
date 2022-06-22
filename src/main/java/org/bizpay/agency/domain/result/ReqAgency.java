package org.bizpay.agency.domain.result;

import lombok.Data;

// 대리점 요청목록
@Data
public class ReqAgency {
    private int no;
    private String bossBizCode;
    private String email;
    private String companyName;
    private String mobileNumber;
    private String createdAt;
    private String status;
}
