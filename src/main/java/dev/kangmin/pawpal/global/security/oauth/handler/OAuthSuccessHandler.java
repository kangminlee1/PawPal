package dev.kangmin.pawpal.global.security.oauth.handler;

import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.jwt.JwtUtil;
import dev.kangmin.pawpal.global.jwt.dto.JwtToken;
import dev.kangmin.pawpal.global.redis.RefreshTokenService;
import dev.kangmin.pawpal.global.security.AuthDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    @Value("${backend.server-name}")
    private String backServerName;

    @Value("${frontend.redirect-url}")
    private String frontRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();

        //사용자 구분을 위해 memberId 추가
        JwtToken jwtToken = jwtUtil.generateJwtToken(authDetails.getEmail(), authDetails.getUsername());


        //로그인 성공 시 refreshtoken 저장 (redis)
        refreshTokenService.saveRefreshToken(jwtToken.getJwtRefreshToken(), authDetails.getUsername());

        //추가 코드
        String registrationId =authDetails.getMember().getProvider();//google, kakao, naver
        String target = buildRedirectUrl(registrationId, request.getServerName());
        //추가 코드 끝

        String redirectUrl = UriComponentsBuilder.fromUriString(target)
                .queryParam("jwtAccessToken", jwtToken.getJwtAccessToken())
                .queryParam("jwtRefreshToken", jwtToken.getJwtRefreshToken())
                .build().toUriString();

//        String redirectUrl = UriComponentsBuilder.fromUriString(setRedirectUrl(request.getServerName()))
//                .queryParam("jwtAccessToken", jwtToken.getJwtAccessToken())
//                .queryParam("jwtRefreshToken", jwtToken.getJwtRefreshToken())
//                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
    private String buildRedirectUrl(String registrationId, String serverName) {
        String baseUrl;
        if ("localhost".equals(serverName)) {
            baseUrl = "http://localhost:8080/oauth";
        } else {
            baseUrl = frontRedirectUrl + "/oauth";
        }

        return switch (registrationId) {
            case "google" -> baseUrl + "/google/success";
            case "kakao" -> baseUrl + "/kakao/success";
            case "naver" -> baseUrl + "/naver/success";
            default -> throw new IllegalArgumentException("Unsupported OAuth provider: " + registrationId);
        };
    }
//    private String setRedirectUrl(String serverName) {
//        if ("localhost".equals(serverName)) {
//            // 개발 환경
//            return "http://localhost:8080/oauth/google/success";
//        } else {
//            // 배포 환경
//            return frontRedirectUrl + "/oauth/google/success/ing";
//        }
//    }
}
