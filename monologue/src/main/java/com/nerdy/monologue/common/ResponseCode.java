package com.nerdy.monologue.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    OK("OK", "정상 작동"),
    SERVER_ERROR("SEV-001","서버 오류"),
    MEM_NOT_FOUND("MEM-001", "멤버를 찾을 수 없습니다."),
    INVALID_CREDENTIALS("AUTH-001", "이메일 또는 비밀번호가 없습니다."),
    NO_AUTHENTICATION("AUTH-002", "인증 정보가 없습니다. 사용자가 로그인되어 있지 않습니다."),
    AUTHENTICATION_ERROR("AUTH-003", "인증 처리 중 오류가 발생했습니다."),
    MISSING_REQUIRED_FIELDS("AUTH-004", "필수 값이 누락되었습니다."),
    UNKNOWN_AUTH_ERROR("AUTH-005", "알 수 없는 인증 오류가 발생했습니다."),
    TOKEN_EXPIRED("AUTH-006", "JWT 토큰이 만료되었습니다."),
    INVALID_TOKEN("AUTH-007", "유효하지 않은 JWT 토큰입니다.")
    ;

    private final String responseCode;
    private final String message;
}