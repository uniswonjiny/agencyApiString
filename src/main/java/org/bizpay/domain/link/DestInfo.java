package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 배송지정보
@Getter
@Setter
@ToString
public class DestInfo {
	private String mobilePhone;
    private String addrInfo;
    private String addrDetailInfo;
    private String zonecode;
    private String message;
}
