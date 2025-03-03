package com.nerdy.monologue.member.repository;

import com.nerdy.monologue.member.domain.entity.Member;
import com.nerdy.monologue.member.security.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

}