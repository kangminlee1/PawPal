package dev.kangmin.pawpal.api;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.global.redis.AuthService;
import dev.kangmin.pawpal.global.redis.dto.GetJwtTokenDto;
import dev.kangmin.pawpal.global.redis.dto.PostRefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal Member member,
            @RequestBody PostRefreshTokenDto postRefreshTokenDto){
        authService.logout(member, postRefreshTokenDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //재발급
    @PostMapping("/reissue")
    public GetJwtTokenDto reissue(
            @AuthenticationPrincipal Member member,
            @RequestBody PostRefreshTokenDto postRefreshTokenDto) {
        return authService.reIssue(member, postRefreshTokenDto);
    }

}
