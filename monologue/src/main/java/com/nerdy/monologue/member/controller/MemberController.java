package com.nerdy.monologue.member.controller;

import com.nerdy.monologue.config.security.JwtTokenProvider;
import com.nerdy.monologue.member.DTO.LoginRequest;
import com.nerdy.monologue.member.DTO.UpdateMemberRequest;
import com.nerdy.monologue.member.service.MemberService;
import com.nerdy.monologue.member.domain.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Autowired
    public MemberController(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Member member) {
        memberService.signUp(member);
        return ResponseEntity.ok("Sign up successful");
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = memberService.login(loginRequest.getEmail(), loginRequest.getPassword());

            Member member = memberService.findByEmail(loginRequest.getEmail());

            return ResponseEntity.ok()
                    .body(Map.of(
                            "token", token,
                            "member", Map.of(
                                    "email", member.getEmail(),
                                    "name", member.getName(),
                                    "imageUrl", member.getImageUrl()
                            )
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    // 회원 정보 조회 API
    @GetMapping("/profile")
    public ResponseEntity<?> readMember(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "").trim();

        if (jwtTokenProvider.validateToken(accessToken)) {
            // 이메일 가져오기
            String email = jwtTokenProvider.getEmailFromToken(accessToken);

            // 사용자 정보 가져오기
            Member member = memberService.findByEmail(email);

            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // 회원 정보 수정 API
    @PutMapping("/profile")
    public ResponseEntity<?> updateMember(@RequestHeader("Authorization") String token,
                                          @RequestBody UpdateMemberRequest updateRequest) {
        String accessToken = token.replace("Bearer ", "").trim();

        if (jwtTokenProvider.validateToken(accessToken)) {
            String email = jwtTokenProvider.getEmailFromToken(accessToken);
            Member updatedMember = memberService.updateMemberInfo(email, updateRequest.getName(), updateRequest.getNewPassword(), updateRequest.getImageUrl());
            return ResponseEntity.ok(updatedMember);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    // 회원 삭제 API
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteMember(@RequestHeader("Authorization") String token) {
        String accessToken = token.replace("Bearer ", "").trim();

        if (jwtTokenProvider.validateToken(accessToken)) {
            String email = jwtTokenProvider.getEmailFromToken(accessToken);
            memberService.deleteMemberInfo(email);
            return ResponseEntity.ok("Account deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }
}