package dev.kangmin.pawpal.global.config;

import dev.kangmin.pawpal.api.mail.service.MailService;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {

    private final MemberService memberService;
    private final MailService mailService;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    //host, port, timeout 설정
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);

        LettuceClientConfiguration clientConfiguration
                = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(1))
                .shutdownTimeout(Duration.ZERO)
                .build();

        log.info("Connected To Redis At -> host : {}, port : {}", host, port);
        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    //Redis Template Use
    //key serializer, value serializer 설정 후 빈 등록
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    //사용자 삭제 처리
    //건강검진 시간 알림
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory
    ) {
        RedisMessageListenerContainer messageListenerContainer
                = new RedisMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(connectionFactory);

        messageListenerContainer.addMessageListener(
                (message, pattern) -> {
                    String expiredKey = message.toString();
                    if (expiredKey.startsWith("member:end:")) {
                        Long memberId = Long.parseLong(expiredKey.split(":")[2]);
                        //삭제
                        memberService.deletedMember(memberId);
                    }

                    if (expiredKey.startsWith("health:end:")) {

                    }

                }, new PatternTopic("__keyevent@*__:expired"));

        return messageListenerContainer;
    }
}
