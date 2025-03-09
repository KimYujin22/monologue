package com.nerdy.monologue.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// JWT 기반의 인증 정보 처리 클래스
public class JWTAuthenticationToken extends UsernamePasswordAuthenticationToken {

    // 인증된 사용자 정보와 인증 관련 정보를 처리.
    public JWTAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

}