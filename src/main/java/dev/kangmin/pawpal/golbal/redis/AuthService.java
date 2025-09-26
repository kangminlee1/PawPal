package dev.kangmin.pawpal.golbal.redis;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.golbal.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;



}
