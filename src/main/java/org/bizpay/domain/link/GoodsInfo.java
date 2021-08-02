package org.bizpay.domain.link;
/**
 * 상품정보
 * */
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsInfo {
	private String itName; // 상품명
	private String itDetailUrl; // 상품 URL
	private int itPrice; // 상품가격 단가
	private int itCount; // 상품갯수
	private String itAddInfo; // 상품설명
}
