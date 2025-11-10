package dev.kangmin.pawpal.domain.member.service;

import dev.kangmin.pawpal.domain.board.repository.BoardRepository;
import dev.kangmin.pawpal.domain.comment.repository.CommentRepository;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.dto.GenerateDto;
import dev.kangmin.pawpal.domain.member.dto.ModifyMemberDto;
import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import dev.kangmin.pawpal.domain.mylike.repository.MyLikeRepository;
import dev.kangmin.pawpal.domain.mylike.service.MyLikeService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    private static final String deleteEmail = "delete@ex.com";

    /**
     * 사용자 등록
     * 단, 생성 전 존재여부 확인 후 등록해야함
     * -> OAuth의 경우 처음 로그인할 경우 회원가입이 진행
     * -> 그 때 등록된 identify 존재 여부 확인 후 없으면 생성
     * @param generateDto
     */
    @Transactional
    public void createMember(GenerateDto generateDto) {

        Member newMember = Member.builder()
                .name(generateDto.getName())
                .email(generateDto.getEmail())
                .password(passwordEncoder.encode(generateDto.getPassword()))
                .provider(generateDto.getProvider())
                .providerId(generateDto.getProviderId())
                .identify(generateDto.getIdentify())
                .build();
        memberRepository.save(newMember);
    }

    //사용자 찾기
    /**
     * 이메일로 찾기
     * @param email
     * @return
     */
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, MEMBER_IS_NOT_EXISTS));
    }

    /**
     * memberId로 찾기
     * @param memberId
     * @return
     */
    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, MEMBER_IS_NOT_EXISTS));
    }



    // 근데 사용자를 완전히 삭제 했을 때  문제점
    //1. 게시글, 댓글은 남긴다는 가정하에 다시 가입했을때 정보 꼬임
    //2. 삭제된 사용자들을 null 값으로 다 처리하면 다 null이라 해당 게시글, 댓글 이걸 어케 처리할까
    //초 분 시간 일 월 요일
    //이것도 레디스 처리하자

    @Transactional
    public void deletedMember(Long memberId) {
        Member member = findMemberByMemberId(memberId);
        Member deletedMember = findMemberByMemberId(1L);

        //좋아요 삭제
        memberRepository.deleteMyLikeByMember(member);

        //익명화 ( 작성한 게시글, 댓글 "익명화" 처리)
        //memberId 1번이 삭제된 사용자
        boardRepository.updateBoardWriterToDeleted(member, deletedMember);
        commentRepository.updateCommentWriterToDeleted(member, deletedMember);
        log.info("게시글 및 댓글 익명화 처리");

        //실제 사용자 삭제
        memberRepository.delete(member);
        log.info("회원 {} 이 삭제 되었습니다.", memberId);
    }
}
