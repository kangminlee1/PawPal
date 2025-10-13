package dev.kangmin.pawpal.global.jwt;


import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.global.jwt.dto.JwtToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final MemberRepository memberRepository;

    @Value("${jwt.secretKey}")
    private String secret;

    private SecretKey secretKey;

    private static final long ACCESS_EXPIRE_TIME = 1000L * 60 * 60;
    private static final long REFRESH_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 14;
    private static final String AUTHORITIES_KEY = "auth";

    //    @PostConstruct
//    protected void init() {
//        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
//    }
    @PostConstruct
    protected void init() {
        // 최신 JJWT 권장 방식
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    //생성부 (원래는 authkey 만 받음)
    public JwtToken generateJwtToken(String authKey, Long memberId) {
        final Date now = new Date();

        return JwtToken.builder()
                .jwtAccessToken("Bearer " + generateAccessToken(authKey, now, memberId))
                .jwtRefreshToken("Bearer " + generateRefreshToken(authKey, now, memberId))
                .build();
    }

    public String generateAccessToken(String authKey, Date now, long memberId) {
        Date expireDate = new Date(now.getTime() + ACCESS_EXPIRE_TIME);
        return Jwts.builder()
                .issuer("pawpal")
                .subject(authKey)
                .claim(AUTHORITIES_KEY, authKey)
                .claim("memberId", memberId)
//                .claims().add(AUTHORITIES_KEY, authKey).add("memberId", memberId)
//                .and()
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String authKey, Date now, long memberId) {
        Date expireDate = new Date(now.getTime() + REFRESH_EXPIRE_TIME);

        return Jwts.builder()
                .issuer("pawpal")
                .subject(authKey)
                .claim(AUTHORITIES_KEY, authKey)
                .claim("memberId", memberId)
//                .claims().add(AUTHORITIES_KEY, authKey).add("memberId", memberId)
//                .and()
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(secretKey)
                .compact();
    }


    //토큰 검증 부

    /**
     * 토큰에서 email 정보 가져오기
     *
     * @param token
     * @return
     */
    private String getAuthKeyClaim(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(AUTHORITIES_KEY, String.class);
    }

    /**
     * 토큰에서 memberId 가져오기
     * @param token
     * @return
     */
    private Long getMemberId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    /**
     * 토큰에서 만료일 가져오기
     *
     * @param token
     * @return
     */
    private Date getExpireTimeClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    /**
     * 토큰 만료 여부
     *
     * @param token
     * @return
     */
    private Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    /**
     * 토큰 검증
     *
     * @param token
     * @return
     */
    public Member validJwtToken(String token) {
        String authKey = getAuthKeyClaim(token);
        Long memberId = getMemberId(token);

        if (authKey == null || memberId == null) {
            return null;
        }

        if (this.isExpired(token)) {
            return null;
        }
        return memberRepository.findByEmail(authKey)
                .orElse(null);
    }

    /**
     * refresh token 검증
     *
     * @param refreshToken
     * @return
     */
    public boolean validRefreshToken(String refreshToken) {
        String authKey = getAuthKeyClaim(refreshToken);
        Long memberId = getMemberId(refreshToken);

        if (authKey == null || memberId == null) {
            return false;
        }
        if (this.isExpired(refreshToken)) {
            return false;
        }
        Member memberRefreshToken = memberRepository.findByEmail(authKey)
                .orElse(null);

        return memberRefreshToken != null;
    }

}
