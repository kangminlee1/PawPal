package dev.kangmin.pawpal.golbal.jwt.filter;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.golbal.error.exception.CustomException;
import dev.kangmin.pawpal.golbal.error.exception.ErrorCode;
import dev.kangmin.pawpal.golbal.jwt.JwtUtil;
import dev.kangmin.pawpal.golbal.security.AuthDetails;
import dev.kangmin.pawpal.golbal.security.AuthDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtUtil jwtUtil;
    private AuthDetailsService authDetailService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, AuthDetailsService authDetailService) {
        this.jwtUtil = jwtUtil;
        this.authDetailService = authDetailService;
    }


    /**
     * 인증 or 권한이 필요한 주소 요청이 있을 떄 해당 필터 통과
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //헤더 추츨 및 검증
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            // 없거나 옳은 방식이 아니면 거부
            chain.doFilter(request, response);
            return;
        }

        //JWT 검증 후 정상적인 사용자인지 검증
        String token = header.substring(7);

        try {
            Member member = jwtUtil.validJwtToken(token);

            if (member != null) {
                //정상
                AuthDetails authDetails = new AuthDetails(member, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
                // Authentication 객체 생성
                Authentication authentication = new UsernamePasswordAuthenticationToken(authDetails, null, authDetails.getAuthorities());
                // Security Session 에 Authentication 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.error(e + " TOKEN IS EXPIRED");
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        } catch (SignatureException e) {
            log.error(e + " INVALID TOKEN");
            throw new CustomException(ErrorCode.INVALID_ID_TOKEN);
        } catch (JwtException e) {
            log.error(e + " JWT TOKEN IS INVALID");
            throw new CustomException(ErrorCode.UNSUPPORTED_SOCIAL_LOGIN);
        }
    }
}
