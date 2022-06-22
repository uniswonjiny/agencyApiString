package org.bizpay.agency.domain.param;

import lombok.Data;
// 대리점 관리 검색용
@Data
public class AgencyParam {
    private String userId;
    private String dealerId; // 산하 아이디
    private String dealerName; // 산하 대리점 이름
    private String dealerMemberName; // 산하 대리점 대표자이름
    private String startDt;
    private String endDt;
    private int dealerKind;
    private int startNo; // 시작 페이지
    private int endNo; // 종료 페이지 번호
    private String type; // 지사인 경우  a 가맹점리스트 b 대리점가맹점리스트
}
