package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AgencySalesParam {
	private String dateStart; // 검색시작일
	private String dateEnd; // 검색종료일
	private String bizCode; // 검색 조건으로 선택한 대리점코드
	private String memberBizeCode; // 사용자의 비즈코드
	private String mberName;// 판매자성명
	private String mberId; // 판매자 아이디
	private String companyName; // 상호
	private String bizNum; // 사업자 번호
	private int widthMber; // 소속대리점 딜러포함유무
}
