package dev.kangmin.pawpal.global.error.exception;

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
    MEMBER_ALREADY_REMOVED("삭제된 사용자 입니다."),

    //강아지
    DOG_OWNER_MISMATCH("귀하의 강아지가 아닙니다"),
    DOG_IS_NOT_EXISTS("해당 강아지가 존재하지 않습니다."),

    //강아지 예방 접종
    VACCINE_IS_NOT_EXISTS("예방접종 기록이 없습니다."),
    VACCINE_OWNER_MISMATCH("해당 예방접종 기록의 작성자가 아닙니다."),

    //강아지 건강 검진
    HEALTH_INFO_IS_NOT_EXISTS("건강검진 기록이 없습니다."),

    //사료/간식
    FOOD_INFO_IS_NOT_EXISTS("샤료/간식 정보가 없습니다."),


    //게시판(커뮤니티)
    BOARD_IS_NOT_EXISTS("해당 게시글이 존재하지 않습니다."),
    INVALID_DATE_RANGE("시작 날짜가 종료 날짜보다 더 빨라야 합니다."),
    BOARD_OWNER_MISMATCH("로그인된 사용자와 게시글 작성자가 다릅니다."),
    BOARD_ALREADY_REMOVED("삭제된 게시글 입니다."),

    //댓글
    COMMENT_OWNER_MISMATCH("로그인된 사용자와 댓글 작성자가 다릅니다."),
    COMMENT_IS_NOT_EXISTS("해당 댓글이 존재하지 않습니다."),

    //좋아요

    //메일
    SEND_MAIL_ERROR("메일 발송 중 오류 발생."),

    //산책 관련
    WALK_IS_NOT_EXISTS("산책 정보가 존재하지 않습니다."),
    WALK_OWNER_MISMATCH("사용자의 선택한 강아지가 다릅니다."),
    WALK_DOG_MISMATCH("산책한 강아지와 다릅니다."),

    //견종 관련
    DOG_BREED_IS_NOT_EXISTS("해당 견종은 존재하지 않습니다.");

    private final String errorMessage;
    ErrorCode(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
