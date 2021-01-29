package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountTransParam {
	private String dateStart; // 검색시작일
	private String dateEnd; // 검색종료일
	private String memberBizeCode;
	private String pymntYn ;
	private String payType ; // 코드
	private String bizCode; 
	private String mberName; // 판매자성명
	private String indutyId;// 판매자 아이디
	private String bizrno; // 사업자번호
	private String confmNo;// 승인번호
}
