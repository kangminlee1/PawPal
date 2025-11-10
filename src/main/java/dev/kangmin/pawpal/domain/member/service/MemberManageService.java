package dev.kangmin.pawpal.domain.member.service;

import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberManageService {

    private MemberRepository memberRepository;
    private MemberRedisService memberRedisService;

    /**
     * 회원 상태 변경
     * 탈퇴 상태 -> 존재 상태 : redis에 삭제하기 위한 key 삭제
     * 존재 상태 -> 탈퇴 상태 : redis에 삭제하기 위한 key 생성
     * @param member
     */
    @Transactional
    public void changeMember(Member member) {

        if (member.getExistStatus() == ExistStatus.EXISTS) {
            LocalDateTime currentTime = LocalDateTime.now();
            member.changeStatus(ExistStatus.DELETED, currentTime);
            memberRedisService.setMemberExpireTime(member, currentTime);
        }else{
            //삭제 상태를 취소할 경우 null로 처리해야 1달이 지났을 때 삭제 되지 않음
            member.changeStatus(ExistStatus.EXISTS, null);
            memberRedisService.deleteMemberFromRedis(member);
        }

    }


}
