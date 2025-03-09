package com.nerdy.monologue.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JWTFilterV1 extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization 헤더 찾음
        String authorization = request.getHeader("Authorization");
        System.out.println("token : " + authorization);

        // ... Authorization 헤더 검증하고 accessToken 가져오는 로직
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // Bearer 토큰에서 "Bearer "를 제거한 후 토큰만 추출
            String accessToken = authorization.substring(7);
            System.out.println("accessToken : " + accessToken);
            System.out.println("member id : " + jwtUtil.getMemberId(accessToken));

            // accessToken이 만료되었는지 확인
            if (jwtUtil.isExpired(accessToken)) {
                log.warn("토큰 만료됨");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return; // 필터 체인 진행 막기
            }

            // accessToken에서 MemberId, category 추출
            Claims claims = jwtUtil.parseToken(accessToken);
            String userId = claims.getSubject();
            String category = claims.get("category", String.class);

            if (userId != null) {
                // Authentication 객체를 사용해 인증된 사용자 정보 설정
                Authentication authToken = getAuthentication(userId, category);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // 인증 정보 확인
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                log.info("authentication : {}", authentication);
                log.info("principal : {}", authentication.getPrincipal());
            }
        }
        // 필터 체인 진행
        filterChain.doFilter(request, response);
    }

    // Authentication 객체 생성
    private Authentication getAuthentication(String userId, String category) {
        // Authentication 객체 생성 로직: 여기서 email과 password를 기반으로 인증 정보를 설정
        return new JWTAuthenticationToken(userId, category);
    }

}
