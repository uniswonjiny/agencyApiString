package org.bizpay.agency.domain.result;

import lombok.Data;

@Data
public class MerchantSalesList {
    private int no; // 순번
    private String memberName; // 가맹점이름
    private int amount; // 매출합계
    private int feeAmount; // 수수료 수익합계
}
