package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderErrorType {
	public String message; // 에러 메세지
	public String KSNET_PG_IP;
	public int KSNET_PG_PORT;
	public String pStoreId;
	public String pKeyInType;
	public String pTransactionNo;
}
