package dev.kangmin.pawpal.golbal.security.oauth.handler;

import dev.kangmin.pawpal.golbal.jwt.JwtUtil;
import dev.kangmin.pawpal.golbal.jwt.dto.JwtToken;
import dev.kangmin.pawpal.golbal.redis.RefreshTokenService;
import dev.kangmin.pawpal.golbal.security.AuthDetails;
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

    @Value("${backend.server-name}")
    private String backServerName;

    @Value("${frontend.redirect-url}")
    private String frontRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        JwtToken jwtToken = jwtUtil.generateJwtToken(authDetails.getUsername());

        //로그인 성공 시 refreshtoken 저장 (redis)
        refreshTokenService.saveRefreshToken(jwtToken.getJwtRefreshToken(), authDetails.getUsername());

        String redirectUrl = UriComponentsBuilder.fromUriString(setRedirectUrl(request.getServerName()))
                .queryParam("jwtAccessToken", jwtToken.getJwtAccessToken())
                .queryParam("jwtRefreshToken", jwtToken.getJwtRefreshToken())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

    private String setRedirectUrl(String serverName) {
        if ("localhost".equals(serverName)) {
            // 개발 환경
            return "http://localhost:8080/oauth/google/success";
        } else {
            // 배포 환경
            return frontRedirectUrl + "/oauth/google/success/ing";
        }
    }
}
