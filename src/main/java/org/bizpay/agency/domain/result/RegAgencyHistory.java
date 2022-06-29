package org.bizpay.agency.domain.result;

import lombok.Data;

// 대리점 등록 요청 히스트로
@Data
public class RegAgencyHistory {
    private int regNo;
    private String createdAt;
    private String status;
}
