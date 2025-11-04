package dev.kangmin.pawpal.domain.member.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static dev.kangmin.pawpal.domain.board.QBoard.board;
import static dev.kangmin.pawpal.domain.comment.QComment.comment;
import static dev.kangmin.pawpal.domain.dog.QDog.dog;
import static dev.kangmin.pawpal.domain.member.QMember.member;
import static dev.kangmin.pawpal.domain.mylike.QMyLike.myLike;

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
