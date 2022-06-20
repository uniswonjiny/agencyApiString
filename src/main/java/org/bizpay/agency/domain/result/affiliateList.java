package org.bizpay.agency.domain.result;

import lombok.Data;
// 대리점 지사 리스트 사용항목들
@Data
public class affiliateList {
    public String gubun; // 지사 대리점 구분
    public String agencyName; // 대리점명
    public int count; // 산하 숫자
}
