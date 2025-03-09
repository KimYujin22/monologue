package com.nerdy.monologue.member.service;

import com.nerdy.monologue.member.domain.entity.Member;
import com.nerdy.monologue.member.service.MemberServiceImpl.LoginResponse;

public interface MemberService {
    void signUp(Member member);
    LoginResponse login(String email, String password);
    Member findByEmail(String email);

    Member readMemberInfo();
    Member updateMemberInfo(String name, String newPassword, String imageUrl);
    void deleteMemberInfo();
}