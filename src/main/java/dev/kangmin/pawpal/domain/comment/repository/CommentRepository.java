package dev.kangmin.pawpal.domain.comment.repository;

import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.comment.repository.querydsl.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
