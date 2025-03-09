package com.nerdy.monologue.config.security;

import com.nerdy.monologue.common.Exception.CustomException;
import com.nerdy.monologue.common.ResponseCode;
import com.nerdy.monologue.member.domain.entity.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// 현재 로그인한 사용자 정보를 불러오는 메서드
public class MemberAuthorizationUtil {

    private MemberAuthorizationUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    // 로그인된 사용자 ID 가져오기
    public static Long getLoginMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CustomException(ResponseCode.NO_AUTHENTICATION);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getMemberId();
    }

    // 로그인된 사용자 정보(Member) 가져오기
    public static Member getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CustomException(ResponseCode.NO_AUTHENTICATION);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        System.out.println("userDetail : " + userDetails);
        System.out.println("memberId : " + userDetails.getMemberId());

        return userDetails.getMember();  // CustomUserDetails에서 Member 객체를 가져온다고 가정
    }
}
