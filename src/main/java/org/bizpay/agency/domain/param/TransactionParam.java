package org.bizpay.agency.domain.param;

import lombok.Data;

@Data
public class TransactionParam {
    private String startConfmDt; // 거래날짜 시작일
    private String endConfmDt; // 거래날자 종료일
    private int startPageNumber; // 시작 페이지
    private int endPageNumber; // 종료 페이지 번호
    private String companyName; // 가맹점명
    private String bprprr; // 대표자명
    private String mberName; // 판매자성명
    private String confmNo; // 승인번호
    private String issueCmpnyNm; // 카드사
    private int amount; // 금액
    private String userId;
    private int dealerKind;
}
