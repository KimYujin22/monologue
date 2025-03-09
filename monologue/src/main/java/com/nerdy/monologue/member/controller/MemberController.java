package com.nerdy.monologue.member.controller;

import com.nerdy.monologue.member.service.MemberServiceImpl.LoginResponse;
import com.nerdy.monologue.member.DTO.LoginRequest;
import com.nerdy.monologue.member.DTO.UpdateMemberRequest;
import com.nerdy.monologue.member.service.MemberService;
import com.nerdy.monologue.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
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
    public ResponseEntity<LoginResponse> login(@RequestParam String email, @RequestParam String password) {
        LoginResponse response = memberService.login(email, password);
        return ResponseEntity.ok(response);
    }

    // 회원 정보 조회 API
    @GetMapping("/profile")
    public ResponseEntity<Member> readMemberInfo() {
        Member member = memberService.readMemberInfo();
        return ResponseEntity.ok(member);
    }

    // 회원 정보 수정 API
    @PutMapping("/profile")
    public ResponseEntity<?> updateMemberInfo(@RequestParam(required = false) String name,
                                          @RequestParam(required = false) String newPassword,
                                          @RequestParam(required = false) String imageUrl) {
        Member updatedMember = memberService.updateMemberInfo(name, newPassword, imageUrl);
        return ResponseEntity.ok(updatedMember);
    }

    // 회원 삭제 API
    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteMember() {
        memberService.deleteMemberInfo();
        return ResponseEntity.ok("회원 탈퇴 성공");
    }
}