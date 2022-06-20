package org.bizpay.agency.domain.param;

import lombok.Data;

@Data
public class NoticeParam {
    private String title;
    private String content;
    private String type;
    private int startPageNumber;
    private int endPageNumber;
    private String startDt;
    private String endDt;
    private int dealerKind;
}
