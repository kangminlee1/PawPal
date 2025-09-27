package dev.kangmin.pawpal.domain.board.repository;

import dev.kangmin.pawpal.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
