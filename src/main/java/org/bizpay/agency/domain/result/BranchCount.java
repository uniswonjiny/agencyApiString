package org.bizpay.agency.domain.result;

import lombok.Data;

@Data
public class BranchCount {
    public int affiliateCount; // 소속 대리점수
    public int recommendCount; // 추천 대리점수
}
