package dev.kangmin.pawpal.domain.mylike.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static dev.kangmin.pawpal.domain.board.QBoard.board;
import static dev.kangmin.pawpal.domain.mylike.QMyLike.myLike;

@Repository
@RequiredArgsConstructor
public class MyLikeRepositoryImpl implements MyLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public MyLike findByMemberIdAndBoardId(Long memberId, long boardId) {
        return queryFactory
                .select(myLike)
                .from(myLike)
                .join(myLike.board, board).fetchJoin()
                .where(
                        myLike.board.boardId.eq(boardId),
                        myLike.member.memberId.eq(memberId)
                )
                .fetchOne();
    }
}
