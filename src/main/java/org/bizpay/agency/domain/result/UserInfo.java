package org.bizpay.agency.domain.result;

import lombok.Data;

// 사용자 로그인 성공시 제공되는 가장 기본 정보
@Data
public class UserInfo {
    public String userId; // 사용자 아아디
    public String userName; // 사용자 이름
    public String authKey; // 사용자의 인증키
}
