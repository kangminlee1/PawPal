package dev.kangmin.pawpal.domain.board.service;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.dto.*;
import dev.kangmin.pawpal.domain.board.repository.BoardRepository;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;


    /**
     * 사용자와 게시글 id로 찾기
     *
     * @param member
     * @param boardId
     * @return
     */
    public Board findByMemberEmailAndBoardId(Member member, Long boardId) {
        return boardRepository.findByMemberAndBoardId(member, boardId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, BOARD_IS_NOT_EXISTS));
    }

    /**
     * 게시글 id로 찾기
     *
     * @param boardId
     * @return
     */
    public Board findByBoardId(Long boardId) {
        return boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, BOARD_IS_NOT_EXISTS));
    }

    //등록
    @Transactional
    public void generateBoard(Member member, GenerateBoardDto generateBoardDto) {
        if (member.getExistStatus().equals(ExistStatus.DELETED)) {
            throw new CustomException(FORBIDDEN, MEMBER_ALREADY_REMOVED);
        }

        Board board = Board.builder()
                .title(generateBoardDto.getTitle())
                .content(generateBoardDto.getContent())
                .existStatus(ExistStatus.EXISTS)
                .build();

        boardRepository.save(board);
    }

    //수정
    @Transactional
    public void modifyBoard(Member member, ModifyBoardDto modifyBoardDto) {
        Board board = findByBoardId(modifyBoardDto.getBoardId());
        if (!board.getMember().equals(member)) {
            throw new CustomException(FORBIDDEN, BOARD_OWNER_MISMATCH);
        }
        if (member.getExistStatus().equals(ExistStatus.DELETED)) {
            throw new CustomException(FORBIDDEN, MEMBER_ALREADY_REMOVED);
        }

        board.modifyBoard(modifyBoardDto);
    }

    //삭제 -> 아직은 삭제시지 않음 복구 가능성 존재( 추후 완전 삭제를 할지말지 결정하자)
    @Transactional
    public void deleteBoard(Member member, Long boardId) {
        Board board = findByBoardId(boardId);
        if (!board.getMember().equals(member)) {
            throw new CustomException(FORBIDDEN, BOARD_OWNER_MISMATCH);
        }
        if (member.getExistStatus().equals(ExistStatus.DELETED)) {
            throw new CustomException(FORBIDDEN, MEMBER_ALREADY_REMOVED);
        }
        board.deleteBoard();
    }

    //조회
    //전체 -> 기본을 최신순으로?? 할까?
    /**
     * 전체 게시글 조회
     * 로그인 안된 사용자도 볼 수는 있음
     * @param page
     * @param size
     * @return
     */
    public Page<BoardInfoDto> getBoardInfoList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        //queryDSL로 DTO 반환하게 하자 -> 아직 작성 안함
        return boardRepository.findAllBoardPosts(pageable);
    }


    /**
     * 나의 게시글 조회
     * @param member
     * @param page
     * @param size
     * @return
     */
    public Page<BoardInfoDto> getMyBoardInfoList(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findMyBoardByMember(member, pageable);
    }

    /**
     * 공백 제거
     * @param title
     * @return
     */
    public String removeTitleSpace(String title) {
        return title.trim();
    }

    //게시글 필터
    //검색
    /**
     * 게시글 제목으로 검색
     * @param page
     * @param size
     * @param title
     * @return
     */
    public Page<BoardInfoDto> getBoardByTitle(int page, int size, String title) {
        Pageable pageable = PageRequest.of(page, size);
        title = removeTitleSpace(title);
        return boardRepository.findByTitle(pageable, title);
    }

    /**
     * 게시글 제목 + 내용으로 검색
     * 해당 검색어 포함하는 것
     * @param page
     * @param size
     * @param keyword
     * @return
     */
    public Page<BoardInfoDto> getBoardByKeyword(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        keyword = removeTitleSpace(keyword);
        return boardRepository.findByKeyword(pageable, keyword);
    }

    /**
     * 게시글 내용으로 검색
     * @param page
     * @param size
     * @param content
     * @return
     */
    public Page<BoardInfoDto> getBoardByContent(int page, int size, String content) {
        Pageable pageable = PageRequest.of(page, size);
        content = removeTitleSpace(content);
        return boardRepository.findByContent(pageable, content);
    }

    //특정 기간(ex) yyyy-mm-dd

    /**
     * 특정 기간 사이에 게시글 조회
     * yyyy-mm-dd 00:00:00 ~ yyyy-mm-dd 23:59:59
     * @param page
     * @param size
     * @param dateRangeDto
     * @return
     */
    public Page<BoardInfoDto> getBoardOrderByCreateDate(int page, int size, DateRangeDto dateRangeDto) {
        if (dateRangeDto.getStartDate().isAfter(dateRangeDto.getEndDate())) {
            throw new CustomException(BAD_REQUEST, INVALID_DATE_RANGE);
        }

        //00시~ 23시 59분 59초
        LocalDateTime startDate = dateRangeDto.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeDto.getEndDate().atTime(23, 59, 59);

        //최신 순
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return boardRepository.findBoardByCreateDateBetween(pageable, startDate, endDate);
    }

    /**
     * 종아요 많은 게시글 순서 정렬
     * @param page
     * @param size
     * @return
     */
    public Page<BoardInfoDto> getBoardOrderByLikeCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findBoardOrderByMyLike(pageable);
    }

    /**
     * 조회수 많은 게시글 순서 정렬
     * @param page
     * @param size
     * @return
     */
    public Page<BoardInfoDto> getBoardOrderByViewCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findBoardOrderByViewCount(pageable);
    }

    /**
     * 게시글 세부 조회
     * 게시글 id 값 받아서 해당 게시글의 세부 정보 dto 반환
     * @param boardId
     * @return
     */
    public BoardDetailDto getBoardDetail(Long boardId) {
        Board board = findByBoardId(boardId);

        if (board.getExistStatus().equals(ExistStatus.DELETED)) {
            throw new CustomException(FORBIDDEN, BOARD_ALREADY_REMOVED);
        }

        return BoardDetailDto.of(board);
    }


}
