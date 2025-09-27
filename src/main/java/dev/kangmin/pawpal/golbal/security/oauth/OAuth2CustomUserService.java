package dev.kangmin.pawpal.golbal.security.oauth;

import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.repository.MemberRepository;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.domain.member.dto.GenerateDto;
import dev.kangmin.pawpal.golbal.error.exception.CustomException;
import dev.kangmin.pawpal.golbal.security.AuthDetails;
import dev.kangmin.pawpal.golbal.security.oauth.info.GoogleUserInfo;
import dev.kangmin.pawpal.golbal.security.oauth.info.KakaoUserInfo;
import dev.kangmin.pawpal.golbal.security.oauth.info.NaverUserInfo;
import dev.kangmin.pawpal.golbal.security.oauth.info.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

import static dev.kangmin.pawpal.golbal.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2CustomUserService extends DefaultOAuth2UserService {

    private static final String DEFAULT = "default";
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException, CustomException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        attributes
                .forEach((key, value) -> log.info(key + " : " + value));

        String registrationID = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo =
                switch (registrationID){
                    case "google" -> new GoogleUserInfo(oAuth2User.getAttributes());
                    case "kakao" -> new KakaoUserInfo(oAuth2User.getAttributes());
                    case "naver" -> new NaverUserInfo(oAuth2User.getAttributes());
                    default -> throw new CustomException(UNSUPPORTED_SOCIAL_LOGIN);
                };
        GenerateDto generateDto = GenerateDto.builder()
                .name(oAuth2UserInfo.getProviderName())
                .email(oAuth2UserInfo.getProviderEmail())
                .provider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .identify(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                .password(DEFAULT)//소셜 로그인은 비밀번호 저장해두지 않음
                .build();

        //만약 사용자 없으면 생성
        if (memberRepository.existsByEmail(generateDto.getEmail())) {
            memberService.createMember(generateDto);
        }

        Member member = memberService.findMemberByEmail(generateDto.getEmail());
        return new AuthDetails(member, attributes, Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())));
    }
    //    -------------google response------------
//    {
//        "id" : "00000000000000000000",
//        "email" : "sample@gmail.com",
//        "verified_email" : true,
//        "name" : "홍길동",
//        "given_name" : "길동",
//        "family_name" : "홍",
//        "picture" : "https://url 경로",
//        "locale" : "ko"
//    }

//    -------------kakao response------------
//    {
//        "id":00000000000000000000,
//            "properties":{
//                "nickname":"홍길동",
//                "profile_image":"https://url 경로"
//            },
//        "kakao_account":{
//        "email":"sample@gmail.com",
//        }
//    }

//    -------------naver response------------
//    {
//        "response": {
//                "email": "sample@gmail.com",
//                "nickname": "홍길동",
//                "profile_image": "https://url 경로"
//                "id": "00000000000000000000",
//                "name": "홍길동",
//    }
}
