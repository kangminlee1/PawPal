package dev.kangmin.pawpal.domain.healthrecord.service;

import dev.kangmin.pawpal.domain.healthrecord.repository.HealthRecordRepository;
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
public class HealthRecordRedisService {

    private final StringRedisTemplate redisTemplate;
    private final HealthRecordRepository healthRecordRepository;

    private static final String HEALTH_EXPIRATION_KEY_PREFIX = "health:end:";


    /**
     * REDIS에 키 생성
     * @param member
     * @param healthId
     * @return
     */
    public String generateRedisKey(Member member, Long healthId) {
        return HEALTH_EXPIRATION_KEY_PREFIX + member.getMemberId() + "/" + healthId;
    }

    /**
     * REDIS에 키 존재 여부
     * 존재하면 true, 없으면 false
     * @param member
     * @param healthId
     * @return
     */
    public boolean isExists(Member member, Long healthId) {
        String key = generateRedisKey(member, healthId);
        return redisTemplate.hasKey(key);
    }

    /**
     * health Redis 삭제
     * @param member
     * @param healthId
     */
    public void deleteRedisKey(Member member, Long healthId) {
        String key = generateRedisKey(member, healthId);
        redisTemplate.delete(key);
    }

    /**
     * 만료일 설정
     * - 만료일이 현재 시간 이전일 경우 예외
     * - 만료일과 현재 시간의 차이동안 유효하게 redis 설정
     * @param member
     * @param healthId
     * @param expiredTime
     */
    public void setHealthRecordExpiredTime(Member member, Long healthId, LocalDateTime expiredTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, expiredTime);

        //만료일이 현재 보다 이전일 경우
        if (duration.isNegative()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_DATE_RANGE);
        }

        //TTL 초단위로 변경
        long ttl = duration.getSeconds();

        String key = generateRedisKey(member, healthId);
        redisTemplate.opsForValue().set(key, "active", ttl, TimeUnit.SECONDS);
    }

}
