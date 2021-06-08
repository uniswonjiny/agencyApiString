package org.bizpay.common.domain.external;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 외부결제 주문 상태및 정보확인
@Getter
@Setter
@ToString
public class OrderStatusInfo {
	private String orderName;
	private String orderPrice;
	private String exorderNo;
	private String mberId;
	private String confmNo;
	private String status;
}
