package dev.kangmin.pawpal.domain.board.repository.querydsl;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.dto.BoardInfoDto;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface BoardRepositoryCustom {
    Page<BoardInfoDto> findAllBoardPosts(Pageable pageable);

    Page<BoardInfoDto> findMyBoardByMember(Member member, Pageable pageable);

    Page<BoardInfoDto> findBoardByCreateDateBetween(Pageable pageable, Date startDate, Date endDate);

    Page<BoardInfoDto> findBoardOrderByViewCount(Pageable pageable);

    //검색
    Page<BoardInfoDto> findByTitle(Pageable pageable, String title);

    Page<BoardInfoDto> findByKeyword(Pageable pageable, String keyword);

    Page<BoardInfoDto> findByContent(Pageable pageable, String content);
}
