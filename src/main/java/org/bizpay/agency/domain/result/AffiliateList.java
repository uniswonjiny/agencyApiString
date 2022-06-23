package org.bizpay.agency.domain.result;

import lombok.Data;
//  대리점 매출 목록
@Data
public class AffiliateList {
    private int no;
    private String memberName;
    private int amount;
    private int feeAmount;
}
