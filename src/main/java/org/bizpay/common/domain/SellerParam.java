package org.bizpay.common.domain;
// 판매자매출용파라미터
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SellerParam {
	private String dateStart; // 검색시작일
	private String dateEnd; // 검색종료일
	private String bizCode; // 검색 조건으로 선택한 대리점코드
	private String memberBizeCode; // 사용자의 비즈코드
	private String mberName;// 판매자성명
	private String mberId; // 판매자 아이디
	private String companyName; // 상호
	private String bizNum; // 사업자 번호 
	private String calculateType; // 정산구분-직거래상태
	private String delngSeCode; // 거래구분코드 - 매출구분
	private int confmNo; // 승인번호
}
