package org.bizpay.agency.domain.result;

import lombok.Data;

@Data
public class Notice {
    private int no;
    private String title;
    private String createDate;
    private String content;
    private String type;
}
