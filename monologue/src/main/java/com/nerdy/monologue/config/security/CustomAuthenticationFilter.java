package com.nerdy.monologue.config.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdy.monologue.common.Exception.CustomException;
import com.nerdy.monologue.common.ResponseCode;
import com.nerdy.monologue.member.domain.entity.Member;
import com.nerdy.monologue.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JWTUtil jwtUtil) {
        super.setAuthenticationManager(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        try {
            // 요청에서 JSON 데이터를 읽어와 email, password 추출
            Map<String, String> requestBody = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String email = requestBody.get("email");
            String password = requestBody.get("password");

            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                throw new CustomException(ResponseCode.INVALID_CREDENTIALS);
            }

            log.info("로그인 시도 - 이메일: {}", email);

            UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));

            // token 검증을 위해 AuthenticationManager에 전달하여 인증 수행
            return this.getAuthenticationManager().authenticate(authtoken);
        } catch (IOException e) {
            throw new CustomException(ResponseCode.AUTHENTICATION_ERROR);
        } catch (Exception e) {
            throw new CustomException(ResponseCode.UNKNOWN_AUTH_ERROR);
        }
    }

    @Override // 로그인 성공 시 실행하는 메소드 (여기서 JWT를 발급)
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        String email = authentication.getName();
        Member memberbyEmail = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ResponseCode.MEM_NOT_FOUND));
        String memberId = memberbyEmail.getId().toString();

        // 토큰 종류(카테고리), 유저 이름, 역할 등을 페이로드에 담는다.
        String newAccess = jwtUtil.createAccessToken("access", email);
        String newRefresh = jwtUtil.createRefreshToken("refresh", email);

        // [Refresh 토큰 - DB에서 관리] 리프레쉬 토큰 관리권한이 서버에 있습니다
        saveOrUpdateRefreshEntity(memberbyEmail, newRefresh);

        response.setCharacterEncoding("UTF-8");

        // 로그인 성공 시 -> [response Header] : Access Token 추가, [response Cookie] : Refresh Token 추가
        setTokenResponse(response, newAccess, newRefresh);
        // 로그인 성공에 대한 추가 정보를 response body에 담음
        response.getWriter().write("로그인에 성공했습니다.");
    }

    // 리프레시 토큰을 저장하는 메서드
    private void saveOrUpdateRefreshEntity(Member member, String refreshToken) {
        member.updateRefreshToken(refreshToken);
        memberRepository.save(member);
    }

    // 토큰을 response에 추가하는 메서드
    private void setTokenResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(new Cookie("refresh_token", refreshToken));
    }

}
