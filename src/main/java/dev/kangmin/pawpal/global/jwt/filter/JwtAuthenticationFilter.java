package dev.kangmin.pawpal.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.global.jwt.JwtUtil;
import dev.kangmin.pawpal.global.jwt.dto.JwtToken;
import dev.kangmin.pawpal.global.redis.RefreshTokenService;
import dev.kangmin.pawpal.global.security.AuthDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // json -> java
            ObjectMapper om = new ObjectMapper();
            Member member = om.readValue(request.getInputStream(), Member.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    member.getEmail(),
                    member.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();

            return authentication;
        } catch (IOException e) {
            throw new BadCredentialsException("FAILED TO PARSE AUTHENTICATION REQUEST");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthDetails authDetails = (AuthDetails) authResult.getPrincipal();
        String email = authDetails.getMember().getEmail();
        //사용자 구분을 위해 memberId 추가
        Long memberId = authDetails.getMember().getMemberId();

        JwtToken jwtToken = jwtUtil.generateJwtToken(email, memberId);

        refreshTokenService.saveRefreshToken(jwtToken.getJwtRefreshToken(), email);

        response.addHeader("Authorization", "Bearer " + jwtToken.getJwtAccessToken());

        Cookie refreshCookie = new Cookie("refreshToken", jwtToken.getJwtRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 14);

        response.addCookie(refreshCookie);
    }
}
