package dev.kangmin.pawpal.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    /**
     * refresh Token 생성
     * @param refreshToken
     * @param authKey
     */
    @Transactional
    public void saveRefreshToken(String refreshToken, String authKey) {
        RefreshToken token = RefreshToken.builder()
                .jwtRefreshToken(refreshToken)
                .authKey(authKey)
                .build();

        refreshTokenRepository.save(token);
    }

    /**
     * refresh Token 삭제
     * -> 없으면 pass
     * -> 있으면 삭제
     * @param refreshToken
     */
    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findRefreshTokenByJwtRefreshToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

}
