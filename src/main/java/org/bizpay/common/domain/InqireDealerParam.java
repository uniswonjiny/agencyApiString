package org.bizpay.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InqireDealerParam {
	private String cmpnm ; // 상호
	private String dealerId;// 딜러아이디
	private String useAt; // 거래구분
	private String bizrno; // 사업자 번호
	private String bizrnoEnc; // 사업자 번호암호화값
	private String memberBizeCode; // 사업자코드
	private int startIndex; // 조회 시작번호
	private int endIndex; // 조회 끝 번호
}
