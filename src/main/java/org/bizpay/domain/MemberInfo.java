package org.bizpay.domain;

import lombok.Data;

@Data
public class MemberInfo {
    String mberCode;
    String bizCode;
    String usid;
    String password;
    String idntfcNm;
    String cmpnm;
    String mberCodeSn;
    String authorCode;
    String recommendBizCode;
    String useAt;
    String grade;
    int dealerKind;
}
