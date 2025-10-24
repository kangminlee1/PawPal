package dev.kangmin.pawpal.domain.board.repository;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.repository.querydsl.BoardRepositoryCustom;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    Optional<Board> findByMemberAndBoardId(Member member, Long boardId);

    Optional<Board> findByBoardId(Long boardId);

}
