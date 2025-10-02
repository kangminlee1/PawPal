package dev.kangmin.pawpal.domain.member.service;

import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.dto.GenerateDto;
import dev.kangmin.pawpal.domain.member.dto.ModifyMemberDto;
import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 사용자 등록
     * -> 단, 생성 전 존재여부 확인 후 등록해야함
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
    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_IS_NOT_EXISTS));
    }

    //사용자 정보 수정
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
    public void deletedMember(String email) {
        //본인 검증
        //임시
        Member member = findMemberByEmail(email);

        //삭제
        member.changeStatus(ExistStatus.DELETED);
    }


}
