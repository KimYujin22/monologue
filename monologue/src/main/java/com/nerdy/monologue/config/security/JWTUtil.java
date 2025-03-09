package com.nerdy.monologue.config.security;

import com.nerdy.monologue.common.Exception.CustomException;
import com.nerdy.monologue.common.ResponseCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {

    private static final String MEMBERPK_CLAIM_KEY = "userId";
    private static final String CATEGORY_CLAIM_KEY = "category";

    private final Key secretKey;
    private final long expirationTime; // Access Token 만료 시간
    private final long refreshExpirationTime; // Refresh Token 만료 시간

    public JWTUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration,
            @Value("${jwt.refresh-expiration}") long refreshExpirationTime) {

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expiration * 1000;
        this.refreshExpirationTime = expiration * 10 * 1000;
    }

    // Access Token 생성
    public String createAccessToken(String category, String email) {
        return createToken(category, email, expirationTime);
    }

    // Refresh Token 생성
    public String createRefreshToken(String category, String email) {
        return createToken(category, email, refreshExpirationTime);
    }

    // JWT 토큰 생성 (공통)
    private String createToken(String category, String email, long expirationTime) {
        return Jwts.builder()
                .setSubject(email) // email 저장
                .claim(CATEGORY_CLAIM_KEY, category) // 토큰 종류 저장 (access/refresh)
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 파싱 (토큰 검증 및 클레임 추출)
    Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료: {}", e.getMessage());
            throw new RuntimeException(ResponseCode.TOKEN_EXPIRED.getMessage());
        } catch (JwtException e) {
            log.error("토큰 파싱 실패: {}", e.getMessage());
            throw new RuntimeException(ResponseCode.INVALID_TOKEN.getMessage());
        }
    }

    // 토큰에서 유저 ID 추출
    public String getMemberId(String token) {
        return parseToken(token).get(MEMBERPK_CLAIM_KEY, String.class);
    }

    public String getCategory(String token) {
        return parseToken(token).get(CATEGORY_CLAIM_KEY, String.class);
    }

    // 토큰이 만료되었는지 확인
    public Boolean isExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }
}