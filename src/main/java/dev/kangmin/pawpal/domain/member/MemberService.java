package dev.kangmin.pawpal.domain.member;

import dev.kangmin.pawpal.domain.member.dto.GenerateDto;
import dev.kangmin.pawpal.golbal.error.exception.CustomException;
import dev.kangmin.pawpal.golbal.error.exception.ErrorCode;
import dev.kangmin.pawpal.golbal.error.exception.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_IS_NOT_EXISTS));
    }


}
