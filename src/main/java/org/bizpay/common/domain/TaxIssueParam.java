package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaxIssueParam {
	private String dateStart; // 검색시작일
	private String dateEnd; // 검색종료일
	private String bizCode; // 검색 조건으로 선택한 대리점코드
}
