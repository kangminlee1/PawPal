package dev.kangmin.pawpal.domain.mylike.service;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.board.service.BoardService;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import dev.kangmin.pawpal.domain.mylike.repository.MyLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyLikeService {

    private final MyLikeRepository myLikeRepository;
    private final MemberService memberService;
    private final BoardService boardService;

    //좋아요 등록 및 수정
    @Transactional
    public void registerOrCanceledLike(String email, long boardId){
        MyLike myLike = myLikeRepository.findByMemberEmailAndBoardId(email, boardId);

        if(myLike!=null){
            //처음 좋아요 등록할 때
            Member member = memberService.findMemberByEmail(email);
            Board board = boardService.findByBoardId(boardId);

            MyLike newLike = MyLike.builder()
                    .member(member)
                    .board(board)
                    .existStatus(ExistStatus.EXISTS)
                    .build();

            myLikeRepository.save(newLike);
        }else if(myLike.getExistStatus() == ExistStatus.DELETED){
            //취소한 상태에서 눌렀을 때
            myLike.changed(ExistStatus.EXISTS);
        }else{
            myLike.changed(ExistStatus.DELETED);
        }
    }
}
