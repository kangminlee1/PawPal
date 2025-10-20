package dev.kangmin.pawpal.domain.member.service;

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

    /**
     * 사용자 정보 수정
     * @param modifyMemberDto
     * @param email
     */
    @Transactional
    public void modifiedMember(ModifyMemberDto modifyMemberDto, String email) {

        //사용자 본인 검증
        //임시로 이메일 받아서 그걸로 멤버 찾은 후 하는 방향
        Member member = findMemberByEmail(email);

        //비밀번호 암호화 후 저장
        member.modifyMember(member.getName(), modifyMemberDto.getEmail(), passwordEncoder.encode(modifyMemberDto.getPassword()));

    }

    //회원 탈퇴
    @Transactional
    public void changeMember(Member member) {

        if (member.getExistStatus() == ExistStatus.EXISTS) {
            member.changeStatus(ExistStatus.DELETED, LocalDateTime.now());
        }else{
            //삭제 상태를 취소할 경우 null로 처리해야 1달이 지났을 때 삭제 되지 않음
            member.changeStatus(ExistStatus.EXISTS, null);
        }

    }

    // 근데 사용자를 완전히 삭제 했을 때  문제점
    //1. 게시글, 댓글은 남긴다는 가정하에 다시 가입했을때 정보 꼬임
    //2. 삭제된 사용자들을 null 값으로 다 처리하면 다 null이라 해당 게시글, 댓글 이걸 어케 처리할까
    //초 분 시간 일 월 요일
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleted() {
        //삭제 처리중인 사용자 찾기 + 삭제 상태 변경 후 1달 지난 거
        List<Member> deleteMemberList = memberRepository.findByExistsStatusAndDeleteAt(LocalDateTime.now().minusMonths(1));

        //삭제 전용 member
        Member deletedMember = findMemberByEmail(deleteEmail);
        for (Member member : deleteMemberList) {
            //좋아요 삭제
            long deleteCount = memberRepository.deleteMyLimeByMember(member);
            log.info("좋아요 삭제 건수 : " + deleteCount);

            //작성한 게시글, 댓글 "익명화" 처리
            long anonymizedCount = memberRepository.anonymizeBoardsAndComments(member, deletedMember);
            log.info("익명화 건수 : " + anonymizedCount);



            //실제 DB에서 삭제 처리
            memberRepository.delete(member);
        }

    }
}
