package com.nerdy.monologue.member.service;

import com.nerdy.monologue.member.domain.entity.Member;

public interface MemberService {
    void signUp(Member member);
    String login(String email, String password);
    Member findByEmail(String email);

    Member readMemberInfo(String token);
    Member updateMemberInfo(String token, String name, String newPassword, String imageUrl);
    void deleteMemberInfo(String token);
}