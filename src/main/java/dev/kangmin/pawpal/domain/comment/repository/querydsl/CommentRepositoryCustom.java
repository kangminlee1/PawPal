package dev.kangmin.pawpal.domain.comment.repository.querydsl;

import dev.kangmin.pawpal.domain.comment.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepositoryCustom {

    Comment findByParentId(Long parentId);

}
