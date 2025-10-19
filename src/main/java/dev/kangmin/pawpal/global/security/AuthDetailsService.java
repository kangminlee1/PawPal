package dev.kangmin.pawpal.global.security;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService  {

    private final MemberRepository memberRepository;

    //로그인(email 기준)
    @Override
    public AuthDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_IS_NOT_EXISTS));

        return new AuthDetails(member, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }

    //jwt 인증(member Id 기준)
    public AuthDetails loadByMemberId(String memberIdStr) throws UsernameNotFoundException {
        Long memberId = Long.parseLong(memberIdStr);

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_IS_NOT_EXISTS));

        return new AuthDetails(member, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
