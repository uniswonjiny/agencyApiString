package org.bizpay.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BizInfo {
	private String bankName; // 은행명
	private String bankSerial; // 계좌번호
	private String bankUser; // 예금주
	private float baroRate; // 익일/바로 수익율
	private String bizrno; // 사업자번호
	private String bizCode; // 사업자코드
	private int bizInstallmentMonths; // 사업자할부 개월
	private long bizLimitDay; // 사업자	1일 한도
	private long bizLimitMonth; // 사업자 월 한도
	private long bizLimitOne; // 사업자 1회 한도
	private long bizLimitYear; // 사업자 년 한도
	private String bizTelno; // 사업장전화번호
	private String bizType; // 사업자구분 N 개인, Y 사업자
	private String bplc; // 사업장주소
	private String bprprr; // 사업주명
	private String cmpnm; // 사업장명
	private String createDt; // 등록일
	private String dealerId; // 대리점아이디
	private String dealerKind; // 대리점구분
	private float dealerRate; // 대리점수익률
	private String dt;
	private String email;
	private float etcRate; // 기타 수익율
	private String historyMemo; // 변경이력 메모
	private String installmentMonths; // 할부 개월
	private int joinAmt; // 가맹비
	private long limitDay; // 1일 한도
	private long limitMonth; // 월 한도
	private long limitOne; // 1회 한도
	private long limitYear; // 년 한도
	private String memberInputYn; // 판매자등록 허용:1, 허용안함:0
	private float memberRate; // 판매자수수료율
	private String memo; // 메모
	private String mTelno; // 핸드폰번호
	private String payBizrno; // 결제사 사업자번호
	private String payBplc; // 결제사 주소
	private String payBprprr; // 결제사 대표자명
	private String payCmpnm; // 결제사 회사명
	private String payTelno; // 결제사 전화번호
	private String pgGb; // PG사 구분
	private String pgTrmnlNo; // PG 터미널ID
	private String pgVan; // PG 또는 VAN
	private float pymntRate;
	private String recommendBizCode; // 추천대리점 BIZ_CODE
	private String trgetBizCode; // 기준사업자
	private String trmnlNo;
	private String vanGb; // VAN사 구분
	private int dealerFee;
	private int agencyFee;
	private int distributorFee;
	private String grade;
}
