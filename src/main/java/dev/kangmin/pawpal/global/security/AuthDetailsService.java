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

    @Override
    public AuthDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_IS_NOT_EXISTS));

        return new AuthDetails(member, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
