package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountExcelParam {
	private String dateStart; // 검색시작일
	private String dateEnd; // 검색종료일
	private String memberBizeCode;
	private String bizCode; // 검색 조건으로 선택한 대리점코드
	private String mberName; // 판매자성명
	private String indutyId;// 판매자 아이디
	private String bizNum; // 사업자 번호
	private String companyName; // 상호
	private String bizrno; // 사업자번호
	
}
