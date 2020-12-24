package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InqireDelingParam {
	private String searchType; // 검색타입 수정후 제거될값이다 !!!!!
	private String dateStart; // 검색시작일
	private String dateEnd; // 검색종료일
	private String bizCode; // 검색 조건으로 선택한 대리점코드
	private String memberBizeCode; //!!! 로그인한 고객의 코드 -- 로그인 세션 관리 기능만들고 변경해야 하는 항목!!!!!
	private int startIndex; //페이징 시작갯수 번호
	private int endIndex; // 페이지 끝번호
	private String delngSeCode; // 거래구분코드
	private String calculateType; // 정산구분
	private String mberName;// 판매자성명
	private int confmNo; // 승인번호
	private String mberId; // 판매자 아이디
	private String bizNum; // 사업자 번호
	private String issueCmpnyNm; // 카드사
	private int sellPrice; // 판매금액
	private String badCardSearch;// 동일카드
	private String orderbyColumn;
	private String orderby;
	private String delngPayType; // 익일 바로정산 구분 
	private String tid;// 단말기 아이디 스와이프 구분용 스와이프 순간 , D+2
	private String code;
}
