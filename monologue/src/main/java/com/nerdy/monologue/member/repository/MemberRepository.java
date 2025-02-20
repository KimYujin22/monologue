package com.nerdy.monologue.member.repository;

import com.nerdy.monologue.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
