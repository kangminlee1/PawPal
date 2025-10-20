package dev.kangmin.pawpal.domain.board.repository.querydsl;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.dto.BoardInfoDto;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface BoardRepositoryCustom {
    Page<BoardInfoDto> findAllBoardPosts(Pageable pageable);

    Page<BoardInfoDto> findMyBoardByMemberEmail(String email, Pageable pageable);

    Page<BoardInfoDto> findBoardByCreateDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    Page<BoardInfoDto> findBoardOrderByViewCount(Pageable pageable);

    Page<BoardInfoDto> findBoardOrderByMyLike(Pageable pageable);

    //검색
    Page<BoardInfoDto> findByTitle(Pageable pageable, String title);

    Page<BoardInfoDto> findByKeyword(Pageable pageable, String keyword);

    Page<BoardInfoDto> findByContent(Pageable pageable, String content);
}
