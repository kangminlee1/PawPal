package dev.kangmin.pawpal.domain.member.service;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberRedisService {
    private final StringRedisTemplate redisTemplate;
    private static final String DELETE_EXPIRATION_KEY_PREFIX = "member:end:";

    /**
     * Redis key 생성
     * @param member
     * @return
     */
    public String generateRedisKey(Member member) {
        return DELETE_EXPIRATION_KEY_PREFIX + member.getMemberId();
    }

    /**
     * 사용자 Redis에서 삭제
     * 사용자가 삭제 유예 기간이 지나게 될 경우 수행
     * Redis에 키가 없어도 예외 발생 X
     * @param member
     */
    public void deleteMemberFromRedis(Member member) {
        String key = generateRedisKey(member);
        redisTemplate.delete(key);
    }

    /**
     * 사용자 삭제 유예기간 중 만료 시간 설정
     * @param member
     * @param expireDateTime
     */
    public void setMemberExpireTime(Member member, LocalDateTime expireDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, expireDateTime);

        //만료일이 현재 시간 보다 빠를 경우(만료일이 현재 시간보다 이전일 경우)
        if (duration.isNegative()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATE_RANGE);
        }

        //TTl 초 단위로 변경
        long ttl = duration.getSeconds();

        String key = generateRedisKey(member);
        redisTemplate.opsForValue().set(key, "active", ttl, TimeUnit.SECONDS);
    }

    /**
     * Redis에 해당 키가 존재하는지 확인
     * 존재할 경우 true
     * 존재하지 않으면 false
     * @param member
     * @return
     */
    public boolean isExistsKey(Member member) {
        String key = generateRedisKey(member);
        return redisTemplate.hasKey(key);
    }

}
