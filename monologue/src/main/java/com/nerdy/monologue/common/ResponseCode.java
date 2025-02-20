package com.nerdy.monologue.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    OK("OK", "정상 작동"),
    SERVER_ERROR("SEV-001","서버 오류"),
    ;

    private final String responseCode;
    private final String message;
}
