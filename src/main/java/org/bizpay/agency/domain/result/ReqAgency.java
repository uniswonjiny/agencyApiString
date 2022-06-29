package org.bizpay.agency.domain.result;

import lombok.Data;

import java.util.List;

// 대리점 요청목록
@Data
public class ReqAgency {
    private int no;
    private String bossBizCode;
    private String bossBizName ;
    private String email;
    private String companyName;
    private String mobileNumber;
    private String memberName;
    private String createdAt;
    private String status;
    private List<RegAgencyHistory> history;
}
