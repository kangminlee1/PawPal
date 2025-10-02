package dev.kangmin.pawpal.domain.board.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.dto.BoardInfoDto;
import dev.kangmin.pawpal.domain.board.repository.BoardRepository;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardInfoDto> findAllBoardPosts(Pageable pageable) {
        return null;
    }

    @Override
    public Page<BoardInfoDto> findMyBoardByMember(Member member, Pageable pageable) {
        return null;
    }

    @Override
    public Page<BoardInfoDto> findBoardByCreateDateBetween(Pageable pageable, Date startDate, Date endDate) {
        return null;
    }

    @Override
    public Page<BoardInfoDto> findBoardOrderByViewCount(Pageable pageable) {
        return null;
    }


    //검색
    @Override
    public Page<BoardInfoDto> findByTitle(Pageable pageable, String title) {
        return null;
    }

    @Override
    public Page<BoardInfoDto> findByKeyword(Pageable pageable, String keyword) {
        return null;
    }

    @Override
    public Page<BoardInfoDto> findByContent(Pageable pageable, String content) {
        return null;
    }
}
