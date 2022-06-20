package org.bizpay.agency.domain.result;

import lombok.Data;
// 대리점 영업 현황
@Data
public class AgencyCount {
    public int directCount; // 직접모집수
    public int inDirectCount; // 간접모집수
}
