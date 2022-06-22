package org.bizpay.agency.domain.result;

import lombok.Data;

// 대리점 관리용 목록 데이터
@Data
public class AgencyManagementList {
    public String dealerKind;
    public String bizCode;
    public String dealerId;
    public String count;
    public String createDt;
    public String cmpnm;
    public String bprprr;
    public String email;
    public String bankName;
    public String bankSerial;
    public String bankUser;
    public int no;
    private String mTelno;
    private String bplc;
}
