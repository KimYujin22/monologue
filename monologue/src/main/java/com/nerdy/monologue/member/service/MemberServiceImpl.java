package com.nerdy.monologue.member.service;

import com.nerdy.monologue.common.Exception.CustomException;
import com.nerdy.monologue.common.ResponseCode;
import com.nerdy.monologue.config.security.JWTUtil;
import com.nerdy.monologue.config.security.MemberAuthorizationUtil;
import com.nerdy.monologue.member.domain.entity.Member;
import com.nerdy.monologue.member.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Override
    public void signUp(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new CustomException(ResponseCode.MEM_NOT_FOUND);
        }
        member.changePassword(passwordEncoder.encode(member.getPassword())); // 비밀번호 암호화 처리
        memberRepository.save(member);
    }

    @Override
    public LoginResponse login(String email, String password) {
        // 1. 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ResponseCode.MEM_NOT_FOUND));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ResponseCode.INVALID_CREDENTIALS);
        }

        // 3. JWT 토큰 생성
        String token = jwtUtil.createAccessToken("member", member.getId().toString());

        // 4. 토큰과 사용자 정보 반환
        return new LoginResponse(token, member);
    }

    @Override
    public Member findByEmail(String email) {
        return MemberAuthorizationUtil.getLoginMember();
    }

    // 회원 정보 조회
    public Member readMemberInfo() {
        return MemberAuthorizationUtil.getLoginMember();
    }

    // 회원 정보 수정 (이름, 비밀번호, 이미지)
    public Member updateMemberInfo(String name, String newPassword, String imageUrl) {
        Member member = MemberAuthorizationUtil.getLoginMember();

        if (name != null && !name.isEmpty()) {
            member.updateName(name);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            member.changePassword(passwordEncoder.encode(newPassword));
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            member.updateCharacterImageUrl(imageUrl);
        }
        return memberRepository.save(member);
    }

    // 회원 삭제
    public void deleteMemberInfo() {
        Member member = MemberAuthorizationUtil.getLoginMember();
        memberRepository.delete(member);
    }

    @Getter
    public static class LoginResponse {
        private final String token;
        private final Member member;

        public LoginResponse(String token, Member member) {
            this.token = token;
            this.member = member;
        }
    }

}