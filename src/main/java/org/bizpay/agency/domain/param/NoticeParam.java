package org.bizpay.agency.domain.param;

import lombok.Data;

@Data
public class NoticeParam {
    private String title;
    private String content;
    private String type;
    private int startNo; // 시작 페이지
    private int endNo; // 종료 페이지 번호
    private String startDt;
    private String endDt;
    private int dealerKind;
}
