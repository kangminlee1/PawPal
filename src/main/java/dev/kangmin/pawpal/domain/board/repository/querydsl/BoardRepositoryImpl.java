package dev.kangmin.pawpal.domain.board.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.board.dto.BoardInfoDto;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static dev.kangmin.pawpal.domain.board.QBoard.board;
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
                        board.likeCount
                ))
                .from(board)
                .where(board.existStatus.eq(ExistStatus.EXISTS))
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
    public Page<BoardInfoDto> findMyBoardByMember(Member member, Pageable pageable) {

        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(Projections.constructor(BoardInfoDto.class,
                        board.boardId,
                        board.title,
                        board.createDate,
                        board.viewCount,
                        board.member.name,
                        board.likeCount
                ))
                .from(board)
                .where(
                        board.member.eq(member),
                        board.existStatus.eq(ExistStatus.EXISTS)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(board.existStatus.eq(ExistStatus.EXISTS),
                        board.member.eq(member));

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }

    //날짜 사이 값들
    @Override
    public Page<BoardInfoDto> findBoardByCreateDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate) {
        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(Projections.constructor(BoardInfoDto.class,
                        board.boardId,
                        board.title,
                        board.createDate,
                        board.viewCount,
                        board.member.name,
                        board.likeCount
                ))
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.createDate.between(startDate, endDate)
                )
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
                .select(Projections.constructor(BoardInfoDto.class,
                        board.boardId,
                        board.title,
                        board.createDate,
                        board.viewCount,
                        board.member.name,
                        board.likeCount

                ))
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS)
                )
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

    //좋아요 많은 순
    @Override
    public Page<BoardInfoDto> findBoardOrderByMyLike(Pageable pageable) {

        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(
                        Projections.constructor(BoardInfoDto.class,
                                board.boardId,
                                board.title,
                                board.createDate,
                                board.viewCount,
                                board.member.name,
                                board.likeCount
                        ))
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS)
                )
                .orderBy(
                        Expressions.numberTemplate(
                                Long.class,
                                "sum(case when {0} = {1} then 1 else 0 end)",
                                myLike.existStatus,
                                ExistStatus.EXISTS
                        ).desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS)
                );

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }


    //검색
    //제목에 포함되는 것 찾기
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
                                board.likeCount
                        )
                )
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.title.containsIgnoreCase(title)
                )
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

    //제목 + 글 내용에 포함되는것
    @Override
    public Page<BoardInfoDto> findByKeyword(Pageable pageable, String keyword) {
        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(Projections.constructor(BoardInfoDto.class,
                                board.boardId,
                                board.title,
                                board.createDate,
                                board.viewCount,
                                board.member.name,
                                board.likeCount
                        )
                )
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.title.containsIgnoreCase(keyword).or(board.content.containsIgnoreCase(keyword))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.title.containsIgnoreCase(keyword)
                );

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }

    //내용에 포함되는 것 찾기
    @Override
    public Page<BoardInfoDto> findByContent(Pageable pageable, String content) {

        List<BoardInfoDto> boardInfoDtoList = queryFactory
                .select(
                        Projections.constructor(BoardInfoDto.class,
                                board.boardId,
                                board.title,
                                board.createDate,
                                board.viewCount,
                                board.member.name,
                                board.likeCount

                        )
                )
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.content.containsIgnoreCase(content)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.existStatus.eq(ExistStatus.EXISTS),
                        board.content.containsIgnoreCase(content)
                );

        return PageableExecutionUtils.getPage(boardInfoDtoList, pageable, countQuery::fetchOne);
    }

    @Override
    public void updateBoardWriterToDeleted(Member member, Member deletedMember) {
        queryFactory
                .update(board)
                .set(board.member, deletedMember)
                .where(board.member.eq(member))
                .execute();
    }
}
