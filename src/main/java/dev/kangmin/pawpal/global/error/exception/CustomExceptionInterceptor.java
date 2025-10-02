package dev.kangmin.pawpal.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionInterceptor {

    /**
     * 예외 발생시 처리 메서드
     * 커스텀 예외 발생: 로그
     * 상태코드 + 메시지 클라이언트에게 응답
     * @param customException
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<ErrorDto> handleCustomException(CustomException customException) {
        log.error("CustomException Error : [Status : {}, Code : {}, Message : {}]", customException.getHttpStatus(), customException.getErrorCode(), customException.getMessage());

        return ErrorDto.errorDtoResponseEntity(customException);
    }
}
