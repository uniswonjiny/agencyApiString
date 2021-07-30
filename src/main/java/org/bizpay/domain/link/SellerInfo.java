package org.bizpay.domain.link;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 판재마 정보
@Getter
@Setter
@ToString
public class SellerInfo {
	private String mberCode; // 판매자 멤버코드
  	private String mberName; // 판매자 이름
  	private String mberMobile; // 판매자 전화번호
  	private String companyName;; // 판매자 상호명
}
