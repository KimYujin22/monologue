package com.nerdy.monologue.member.service;

import com.nerdy.monologue.config.security.JwtTokenProvider;
import com.nerdy.monologue.member.domain.entity.Member;
import com.nerdy.monologue.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void signUp(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        member.changePassword(passwordEncoder.encode(member.getPassword())); // 비밀번호 암호화 처리
        memberRepository.save(member);
    }

    @Override
    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // 로그인 성공 후 JWT 토큰 발행
        return jwtTokenProvider.createToken(member.getEmail());
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: " + email));
    }

    // 회원 정보 조회
    public Member readMemberInfo(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        return findByEmail(email);
    }

    // 회원 정보 수정 (이름, 비밀번호, 이미지)
    public Member updateMemberInfo(String token, String name, String newPassword, String imageUrl) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = findByEmail(email);

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
    public void deleteMemberInfo(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        Member member = findByEmail(email);
        memberRepository.delete(member);
    }

}