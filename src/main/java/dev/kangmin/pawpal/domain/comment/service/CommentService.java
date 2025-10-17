package dev.kangmin.pawpal.domain.comment.service;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.service.BoardService;
import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.comment.dto.CommentDto;
import dev.kangmin.pawpal.domain.comment.dto.UpdateCommentDto;
import dev.kangmin.pawpal.domain.comment.repository.CommentRepository;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final BoardService boardService;

    /**
     * 댓글 id로 찾기
     * @param commentId
     * @return
     */
    public Comment findByCommentId(Long commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, COMMENT_IS_NOT_EXISTS));
    }

    /**
     * 댓글 생성
     * 일반 댓글
     * 대댓글
     * @param commentDto
     * @param member
     * @param boardId
     * @return
     */
    public CommentDto generateComment(CommentDto commentDto, Member member, Long boardId) {
        Board board = boardService.findByBoardId(boardId);

//        //현재 로그인된 사람과 작성된 댓글의 사용자와 같은지 검증 -> 이미 jwt 검증을 하기에 필요 없음
//        if(!member.getMemberId().equals(commentDto.getMemberId())){
//            //다를 경우 -> 인증 OK , 권한 X
//            throw  new CustomException(FORBIDDEN, UNAUTHORIZED_MEMBER);
//        }

        Comment newComment;

        if (commentDto.getParentId() == null) {
            //일반 댓글
            newComment = Comment.builder()
                    .content(commentDto.getContent())
                    .existStatus(ExistStatus.EXISTS)
                    .board(board)
                    .member(member)
                    .build();
        }else{
            //대댓글
            Comment parentComment = commentRepository.findByParentId(commentDto.getParentId());

            if (parentComment == null) {
                //대댓글의 부모 정보가 누락일 경우

            }

            newComment = Comment.builder()
                    .content(commentDto.getContent())
                    .member(member)
                    .board(board)
                    .parent(parentComment)
                    .existStatus(ExistStatus.EXISTS)
                    .build();
        }

        //댓글 작성하면 바로 내가 작성한 내용 보여주기(프론트랑 합의 본 설정)
        commentRepository.save(newComment);
        return CommentDto.of(newComment);
    }

    /**
     * 댓글 삭제
     * @param updateCommentDto
     * @param member
     */
    public void deleteComment(UpdateCommentDto updateCommentDto, Member member) {
        Comment comment = findByCommentId(updateCommentDto.getCommentId());

        if (!member.equals(comment.getMember())) {
            //현재 로그인된 사용자의 댓글이 아닐 경우
            throw new CustomException(FORBIDDEN, UNAUTHORIZED_MEMBER);
        }

        comment.deleteComment(ExistStatus.DELETED);
    }

    /**
     * 댓글 수정
     * @param updateCommentDto
     * @param member
     */
    public void modifyComment(UpdateCommentDto updateCommentDto, Member member) {
        Comment comment = findByCommentId(updateCommentDto.getCommentId());

        if (!member.equals(comment.getMember())) {
            //현재 로그인된 사용자의 댓글이 아닐 경우
            throw new CustomException(FORBIDDEN, UNAUTHORIZED_MEMBER);
        }

        comment.modifiedComment(updateCommentDto.getContent());
    }



}
