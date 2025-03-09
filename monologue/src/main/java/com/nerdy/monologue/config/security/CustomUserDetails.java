package com.nerdy.monologue.config.security;

import com.nerdy.monologue.member.domain.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// 회원 정보와 인증 관련 정보를 담는 메서드 저장용 클래스
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    // 기본으로 모든 사용자에게 ROLE_USER로 권한 부여
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return member.getPassword(); // Member의 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return member.getEmail(); // Member의 이메일을 username으로 사용
    }

    public Long getMemberId() {
        return member.getId(); // Member의 ID를 반환
    }

    public String getName() {
        return member.getName(); // Member의 이름을 반환
    }

    public Member getMember() {
        return member;
    }
}