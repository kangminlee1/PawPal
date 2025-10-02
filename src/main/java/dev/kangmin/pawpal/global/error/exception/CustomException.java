package dev.kangmin.pawpal.global.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final ErrorCode errorCode;

  public CustomException(HttpStatus httpStatus, ErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.httpStatus = httpStatus;
    this.errorCode = errorCode;
  }

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
    this.httpStatus = null;
  }
}
