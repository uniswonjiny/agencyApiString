package org.bizpay.agency.domain.result;

import lombok.Data;

// 가맹점 목록
@Data
public class MerchantManagementList {
    private int no;
    private String usid;
    private String cmpnm;
    private String companyName;
    private String mberName;
    private String creatDt;
    private String calculateType;
    private float feeRate;
    private String adres;
    private String mberMobile;
    private String email;
    private String accountNo;
    private String bankName;
    private String depositor;
    private String bossName;
}
