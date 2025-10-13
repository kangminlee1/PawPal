package dev.kangmin.pawpal.domain.board.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.dto.BoardInfoDto;
import dev.kangmin.pawpal.domain.board.repository.BoardRepository;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static dev.kangmin.pawpal.domain.board.QBoard.board;
import static dev.kangmin.pawpal.domain.member.QMember.member;
import static dev.kangmin.pawpal.domain.mylike.QMyLike.myLike;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    //게시글 전체 조회
    @Override
    public Page<BoardInfoDto> findAllBoardPosts(Pageable pageable) {

        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(Projections.constructor(BoardInfoDto.class,
                        board.boardId,
                        board.title,
                        board.createDate,
                        board.viewCount,
                        board.member.name,
                        Expressions.numberTemplate(Long.class,
                                        "sum(case when {0} = {1} then 1 else 0 end",
                                        myLike.existStatus,
                                        ExistStatus.EXISTS)
                                .coalesce(0L)
                                .intValue()
                ))
                .from(board)
                .leftJoin(myLike).on(myLike.board.eq(board))
                .where(board.existStatus.eq(ExistStatus.EXISTS))
                .groupBy(board.boardId)

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(board.existStatus.eq(ExistStatus.EXISTS));

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }

    //내가 쓴 게시글 목록
    @Override
    public Page<BoardInfoDto> findMyBoardByMemberEmail(String email, Pageable pageable) {

        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(
                        Projections.constructor(BoardInfoDto.class,
                                board.boardId,
                                board.title,
                                board.createDate,
                                board.viewCount,
                                board.member.name,
                                Expressions.numberTemplate(Long.class,
                                                "sum(case when {0} = {1} then 1 else 0 end",
                                                myLike.existStatus,
                                                ExistStatus.EXISTS)
                                        .coalesce(0L)
                                        .intValue())
                )
                .from(board)
                .leftJoin(myLike).on(myLike.board.eq(board))
                .where(
                        board.member.email.eq(email),
                        board.existStatus.eq(ExistStatus.EXISTS)
                )
                .groupBy(board.boardId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(board.existStatus.eq(ExistStatus.EXISTS),
                        board.member.email.eq(email));

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }

    //날짜 사이 값들
    @Override
    public Page<BoardInfoDto> findBoardByCreateDateBetween(Pageable pageable, Date startDate, Date endDate) {
        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(
                        Projections.constructor(BoardInfoDto.class,
                                board.boardId,
                                board.title,
                                board.createDate,
                                board.viewCount,
                                board.member.name,
                                Expressions.numberTemplate(Long.class,
                                                "sum(case when {0} = {1} then 1 else 0 end",
                                                myLike.existStatus,
                                                ExistStatus.EXISTS)
                                        .coalesce(0L)
                                        .intValue())
                )
                .from(board)
                .leftJoin(myLike).on(myLike.board.eq(board))
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.createDate.between(startDate, endDate)
                )
                .groupBy(board.boardId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.createDate.between(startDate, endDate)
                );

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }

    //조회수 높은 순
    @Override
    public Page<BoardInfoDto> findBoardOrderByViewCount(Pageable pageable) {

        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(
                        Projections.constructor(BoardInfoDto.class,
                                board.boardId,
                                board.title,
                                board.createDate,
                                board.viewCount,
                                board.member.name,
                                Expressions.numberTemplate(Long.class,
                                                "sum(case when {0} = {1} then 1 else 0 end",
                                                myLike.existStatus,
                                                ExistStatus.EXISTS)
                                        .coalesce(0L)
                                        .intValue())
                )
                .from(board)
                .leftJoin(myLike).on(myLike.board.eq(board))
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS)
                )
                .groupBy(board.boardId)
                .orderBy(board.viewCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(board.existStatus.eq(ExistStatus.EXISTS));

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }


    //검색
    @Override
    public Page<BoardInfoDto> findByTitle(Pageable pageable, String title) {

        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(
                        Projections.constructor(BoardInfoDto.class,
                                board.boardId,
                                board.title,
                                board.createDate,
                                board.viewCount,
                                board.member.name,
                                Expressions.numberTemplate(Long.class,
                                                "sum(case when {0} = {1} then 1 else 0 end",
                                                myLike.existStatus,
                                                ExistStatus.EXISTS)
                                        .coalesce(0L)
                                        .intValue())
                )
                .from(board)
                .leftJoin(myLike).on(myLike.board.eq(board))
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.title.containsIgnoreCase(title)
                )
                .groupBy(board.boardId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.title.containsIgnoreCase(title)
                );

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
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
