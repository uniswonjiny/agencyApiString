package org.bizpay.agency.domain.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
// 내정보보기용도
@Data
public class AgencyMainInfo {
    private AgencyBasicInfo agencyBasicInfo; // 기본정보
    private AgencyBankInfo agencyBankInfo; // 계좌정보
    private AgencyRecruitInfo agencyRecruitInfo; // 모집정보
}
