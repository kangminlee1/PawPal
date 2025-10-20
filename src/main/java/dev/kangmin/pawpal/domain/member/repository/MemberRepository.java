package dev.kangmin.pawpal.domain.member.repository;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.repository.querydsl.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberId(Long memberId);

    Boolean existsByEmail(String email);

    Boolean existsByIdentify(String identify);

}
