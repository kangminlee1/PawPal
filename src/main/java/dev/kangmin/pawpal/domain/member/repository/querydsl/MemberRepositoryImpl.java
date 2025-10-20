package dev.kangmin.pawpal.domain.member.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static dev.kangmin.pawpal.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findByExistsStatusAndDeleteAt(LocalDateTime localDateTime) {
        return queryFactory
                .select(member)
                .from(member)
                .where(
                        member.existStatus.eq(ExistStatus.DELETED),
                        member.deleteAt.before(localDateTime)
                )
                .fetch();
    }
}
