package dev.kangmin.pawpal.domain.mylike.service;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.service.BoardService;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import dev.kangmin.pawpal.domain.mylike.repository.MyLikeRepository;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class MyLikeService {

    private final MyLikeRepository myLikeRepository;
    private final BoardService boardService;

    //좋아요 등록 및 수정
    @Transactional
    public void registerOrCanceledLike(Member member, long boardId){
        MyLike myLike = myLikeRepository.findByMemberIdAndBoardId(member.getMemberId(), boardId);

        if (myLike == null) {
            //처음 좋아요 등록할 때
            Board board = boardService.findByBoardId(boardId);

            MyLike newLike = MyLike.builder()
                    .member(member)
                    .board(board)
                    .existStatus(ExistStatus.EXISTS)
                    .build();

            board.addLike();
            myLikeRepository.save(newLike);
        } else {
            if (ExistStatus.DELETED == myLike.getExistStatus()) {
                myLike.changed(ExistStatus.EXISTS);
                myLike.getBoard().addLike();
            } else {
                myLike.changed(ExistStatus.DELETED);
                myLike.getBoard().removeLike();
            }
        }
    }
}
