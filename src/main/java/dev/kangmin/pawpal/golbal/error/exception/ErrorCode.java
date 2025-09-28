package dev.kangmin.pawpal.golbal.error.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //ex) IS_NOT_EXISTS("존재하지 않습니다.);

    //TOKEN
    TOKEN_EXPIRED("토큰이 만료되었습니다."),
    INVALID_ID_TOKEN("해당 토큰은 유효하지 않습니다."),

    //AUTH
    UNSUPPORTED_SOCIAL_LOGIN("지원하지 않는 로그인 방식입니다."),
    OAUTH_AUTHENTICATION_FAILED("OAuth 인증에 실패했습니다."),

    //회원
    MEMBER_IS_NOT_EXISTS("사용자가 존재하지 않습니다."),


    //강아지
    DOG_IS_NOT_EXISTS("귀하의 강아지가 존재하지 않습니다."),


    //강아지 건강 검진
    HEALTH_INFO_IS_NOT_EXISTS("건강검진 기록이 없습니다.");

    private final String errorMessage;
    ErrorCode(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
