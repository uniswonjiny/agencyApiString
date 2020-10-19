package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReturnMsg {
	private String type; // 유형타입
	private String message; // 메세지
}
