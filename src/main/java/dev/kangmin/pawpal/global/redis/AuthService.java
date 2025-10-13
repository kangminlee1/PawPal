package dev.kangmin.pawpal.global.redis;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import dev.kangmin.pawpal.global.jwt.JwtUtil;
import dev.kangmin.pawpal.global.redis.dto.GetJwtTokenDto;
import dev.kangmin.pawpal.global.redis.dto.PostRefreshTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;


    /**
     * 로그아웃 처리
     * refreshToken 삭제
     * @param member
     * @param postRefreshTokenDto
     */
    @Transactional
    public void logout(Member member, PostRefreshTokenDto postRefreshTokenDto){
        if (refreshTokenRepository.findRefreshTokenByJwtRefreshToken(postRefreshTokenDto.getJwtRefreshToken()).isEmpty()) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED);
        }

        //토큰 만료되지 않았으면 토큰을 삭제
        refreshTokenService.removeRefreshToken(postRefreshTokenDto.getJwtRefreshToken());
        //사용자 인증 정보 제거
        SecurityContextHolder.clearContext();

    }

    /**
     * refreshToken 재발급
     * @param member
     * @param postRefreshTokenDto
     * @return
     */
    @Transactional
    public GetJwtTokenDto reIssue(Member member, PostRefreshTokenDto postRefreshTokenDto){

        String refreshToken = postRefreshTokenDto.getJwtRefreshToken().substring(7);

        //유효한 토큰 여부
        if(!jwtUtil.validRefreshToken(refreshToken)){
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_ID_TOKEN);
        }

        //토큰 만료 여부
        if(refreshTokenRepository.findRefreshTokenByJwtRefreshToken(postRefreshTokenDto.getJwtRefreshToken()).isEmpty()){
            throw new CustomException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED);
        }

        final GetJwtTokenDto getJwtTokenDto = GetJwtTokenDto.builder()
                .jwtAccessToken("Bearer " + jwtUtil.generateAccessToken(member.getEmail(), new Date(), member.getMemberId()))
                .jwtRefreshToken(postRefreshTokenDto.getJwtRefreshToken())
                .build();

        return getJwtTokenDto;
    }

}
