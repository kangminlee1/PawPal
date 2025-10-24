package dev.kangmin.pawpal.domain.comment.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static dev.kangmin.pawpal.domain.comment.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findByCommentId(Long commentId) {
        return Optional.ofNullable(
                queryFactory
                        .select(comment)
                        .from(comment)
                        .where(
                                comment.commentId.eq(commentId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Optional<Comment> findByParentId(Long parentId) {
        return Optional.ofNullable(queryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.parent.commentId.eq(parentId)
                )
                .fetchOne()
        );
    }
}
