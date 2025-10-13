package dev.kangmin.pawpal.domain.comment.service;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.service.BoardService;
import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.comment.dto.CommentDto;
import dev.kangmin.pawpal.domain.comment.repository.CommentRepository;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final BoardService boardService;

    //댓글 등록 - 즉시 보여주기
    public void generateComment(CommentDto commentDto, String email, long boardId) {
        Member member = memberService.findMemberByEmail(email);
        Board board = boardService.findByBoardId(boardId);

        //현재 로그인된 사람과 작성된 내용의 정보의 사용자와 같은지 검증
        if(!member.getMemberId().equals(commentDto.getMemberId())){

        }

    }




    //댓글 삭제 처리

    //댓글 수정



}
