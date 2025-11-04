package dev.kangmin.pawpal.domain.comment.repository.querydsl;

import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepositoryCustom {

    Optional<Comment> findByParentId(Long parentId);

    Optional<Comment> findByCommentId(Long commentId);

    void updateCommentWriterToDeleted(Member member, Member deletedMember);
}
