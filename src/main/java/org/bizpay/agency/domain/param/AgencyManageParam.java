package org.bizpay.agency.domain.param;

import lombok.Data;

// 대리점관리
@Data
public class AgencyManageParam {
    private String userId;
    private int dealerKind;
    private int startNo; // 시작 페이지
    private int endNo; // 종료 페이지 번호
    private String startDt;
    private String endDt;
    private String dealerId;
    private String cmpnm; // 대리점 지사명
    private String bprprr; // 대표자명
    private String type;
}
