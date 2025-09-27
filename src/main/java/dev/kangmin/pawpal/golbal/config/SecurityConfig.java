package dev.kangmin.pawpal.golbal.config;

import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.golbal.jwt.JwtUtil;
import dev.kangmin.pawpal.golbal.jwt.filter.JwtAuthenticationFilter;
import dev.kangmin.pawpal.golbal.jwt.filter.JwtAuthorizationFilter;
import dev.kangmin.pawpal.golbal.redis.RefreshTokenService;
import dev.kangmin.pawpal.golbal.security.AuthDetailsService;
import dev.kangmin.pawpal.golbal.security.oauth.OAuth2CustomUserService;
import dev.kangmin.pawpal.golbal.security.oauth.handler.OAuthFailureHandler;
import dev.kangmin.pawpal.golbal.security.oauth.handler.OAuthSuccessHandler;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final AuthDetailsService authDetailsService;
    private final OAuth2CustomUserService oAuth2CustomUserService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;

    /**
     * 로그인 --> 사용자 자격 증명 검증 및 권한 부여
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //핸들러
    @Bean
    public OAuthSuccessHandler oAuthSuccessHandler(){
        return new OAuthSuccessHandler(jwtUtil(),refreshTokenService);
    }
    @Bean
    public OAuthFailureHandler oAuthFailureHandler(){
        return new OAuthFailureHandler();
    }


    /**
     * 로그인 --> 사용자 정보를 토대로 토큰 생성 or 검증
     * @return
     */
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(memberRepository);
    }


    /**
     * CORS 관련 설정
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedMethod("*"); //모든 Method 허용(POST, GET, ...)
        configuration.addAllowedHeader("*"); //모든 Header 허용
        configuration.setMaxAge(Duration.ofSeconds(3600)); //브라우저가 응답을 캐싱해도 되는 시간(1시간)
        configuration.setAllowCredentials(true); //CORS 요청에서 자격 증명(쿠키, HTTP 헤더) 허용
        configuration.addExposedHeader("Authorization"); // 클라이언트가 특정 헤더값에 접근 가능하도록 하기
        configuration.addExposedHeader("Authorization-Refresh");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration); //위에서 설정한 Configuration 적용
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtUtil(), refreshTokenService), UsernamePasswordAuthenticationFilter.class)// 사용자 인증
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil(), authDetailsService), UsernamePasswordAuthenticationFilter.class)// 사용자 권한 부여

                .oauth2Login(login -> login
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuth2CustomUserService))
                        .successHandler(oAuthSuccessHandler())
                        .failureHandler(oAuthFailureHandler())
                )

                .authorizeHttpRequests(
                        request -> request
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()//유효 하지 않은 토큰은 Filter를 통해 걸러짐
                                .requestMatchers("/signup", "/login/**","/logout").permitAll()
                                .requestMatchers("/api/v1/auth/login").permitAll()
                                .anyRequest().authenticated()//위에 url 제외한 요청은 모두 인증 필요
                )
                .build();

    }

}
