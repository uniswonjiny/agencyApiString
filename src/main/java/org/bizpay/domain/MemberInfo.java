package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberInfo {
	private String mberCode;
    private String bizCode;
    private String usid;
    private String password;
    private String idntfcNm;
    private String authorCode;
    private String recommendBizCode;
    private String useAt;
    private String grade;
    private int dealerKind;
}
