package dev.kangmin.pawpal.api.auth;

import dev.kangmin.pawpal.api.auth.dto.LoginDto;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.jwt.dto.JwtToken;
import dev.kangmin.pawpal.global.redis.AuthService;
import dev.kangmin.pawpal.global.redis.dto.GetJwtTokenDto;
import dev.kangmin.pawpal.global.redis.dto.PostRefreshTokenDto;
import dev.kangmin.pawpal.global.security.AuthMember;
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

//    //일반 사용자 로그인(OAUTH X)
//    @PostMapping("/login")
//    public ResponseEntity<JwtToken> login(
//            @RequestBody LoginDto loginDto){
//
//
//        return ResponseEntity.ok(jwtToken);
//    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
//            @AuthenticationPrincipal Member member,
            @AuthMember Member member,
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
