package dev.kangmin.pawpal.domain.member.repository.querydsl;


import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findByExistsStatusAndDeleteAt(LocalDateTime localDateTime);
}
