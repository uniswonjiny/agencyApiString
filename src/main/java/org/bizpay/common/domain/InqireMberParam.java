package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InqireMberParam {
	private String bizCode; // 비즈코드 -- 조회용
	private String memberBizeCode; // 비즈코드 -- 멤버용
	private String dateStart; // 등록일자시작
	private String dateEnd; // 등록일자종료
	private String mberName; // 판매자성명
	private String indutyId;// 판매자 아이디
	private String bizNum; // 사업자 번호
	private String useAt; // 거래구분
	private String calculateType; // 정산구분
	private String depositor; // 예금주
	private String nm; // 상호
	private String bizTypeName; // 업종
	private String bizItem; //취급품목 
	private int startIndex; //페이징 시작갯수 번호
	private int endIndex; // 페이지 끝번호
	private String inoutCode;
}
