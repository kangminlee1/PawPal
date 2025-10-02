package dev.kangmin.pawpal.global.error.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDto {

    private String errorMessage;

    /**
     * CustomException 응답 메서드
     * 상태 코드 , 에러 코드 이용하여 클라이언트에게 응답 반환
     * @param customException
     * @return
     */
    public static ResponseEntity<ErrorDto> errorDtoResponseEntity(CustomException customException) {
        ErrorCode errorCode = customException.getErrorCode();

        return ResponseEntity
                .status(customException.getHttpStatus())
                .body(ErrorDto.builder()
                        .errorMessage(customException.getMessage())
                        .build());
    }
}
