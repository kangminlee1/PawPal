package dev.kangmin.pawpal.domain.mylike.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static dev.kangmin.pawpal.domain.mylike.QMyLike.myLike;

@Repository
@RequiredArgsConstructor
public class MyLikeRepositoryImpl implements MyLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MyLike findByMemberEmailAndBoardId(String email, long boardId) {
        return queryFactory
                .select(myLike)
                .from(myLike)
                .where(
                        myLike.member.email.eq(email),
                        myLike.board.boardId.eq(boardId)
                )
                .fetchOne();
    }
}
