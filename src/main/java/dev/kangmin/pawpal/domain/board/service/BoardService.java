package dev.kangmin.pawpal.domain.board.service;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.dto.BoardDetailDto;
import dev.kangmin.pawpal.domain.board.dto.BoardInfoDto;
import dev.kangmin.pawpal.domain.board.dto.GenerateBoardDto;
import dev.kangmin.pawpal.domain.board.dto.ModifyBoardDto;
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

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    //찾기
    public Board findByMemberEmailAndBoardId(String email, Long boardId) {
        return boardRepository.findByMemberEmailAndBoardId(email, boardId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.BOARD_IS_NOT_EXISTS));
    }

    public Board findByBoardId(Long boardId) {
        return boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.BOARD_IS_NOT_EXISTS));
    }

    //등록
    public void generateBoard(String email, GenerateBoardDto generateBoardDto) {
        Member member = memberService.findMemberByEmail(email);

        Board board = Board.builder()
                .title(generateBoardDto.getTitle())
                .content(generateBoardDto.getContent())
                .existStatus(ExistStatus.EXISTS)
                .build();
    }

    //수정
    public void modifyBoard(String email, ModifyBoardDto modifyBoardDto) {
        Board board = findByMemberEmailAndBoardId(email, modifyBoardDto.getBoardId());
        board.modifyBoard(modifyBoardDto);
    }

    //삭제 -> 아직은 삭제시지 않음 복구 가능성 존재( 추후 완전 삭제를 할지말지 결정하자)
    public void deleteBoard(String email, Long boardId) {
        Board board = findByMemberEmailAndBoardId(email, boardId);
        board.deleteBoard();
    }

    //조회
    //전체 -> 기본을 최신순으로?? 할까?
    public Page<BoardInfoDto> getBoardInfoList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        //queryDSL로 DTO 반환하게 하자 -> 아직 작성 안함
        return boardRepository.findAllBoardPosts(pageable);
    }

    //나의 게시글 목록
    public Page<BoardInfoDto> getMyBoardInfoList(String email, int page, int size) {
        Member member = memberService.findMemberByEmail(email);

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
    //게시글 제목
    public Page<BoardInfoDto> getBoardByTitle(int page, int size, String title) {
        Pageable pageable = PageRequest.of(page, size);
        title = removeTitleSpace(title);
        return boardRepository.findByTitle(pageable, title);
    }

    //게시글 제목 + 내용 (해당 검색어 포함하는 것만)
    public Page<BoardInfoDto> getBoardByKeyword(int page, int size, String keyword) {
        keyword = removeTitleSpace(keyword);
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findByKeyword(pageable, keyword);
    }

    //게시글 내용 검색
    public Page<BoardInfoDto> getBoardByContent(int page, int size, String content) {
        content = removeTitleSpace(content);
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findByContent(pageable, content);
    }

    //특정 기간(ex) yyyy-mm-dd
    public Page<BoardInfoDto> getBoardOrderByCreateDate(int page, int size, Date startDate, Date endDate) {
        //최신 순
        Pageable pageable = PageRequest.of(page,size, Sort.by("createDate").descending());
        return boardRepository.findBoardByCreateDateBetween(pageable, startDate, endDate);
    }

    //좋아요 순


    //조회수 순
    public Page<BoardInfoDto> getBoardOrderByViewCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return boardRepository.findBoardOrderByViewCount(pageable);
    }

    //세부 조회
    public BoardDetailDto getBoardDetail(Long boardId) {
        Board board = findByBoardId(boardId);
        return BoardDetailDto.of(board);
    }


}
