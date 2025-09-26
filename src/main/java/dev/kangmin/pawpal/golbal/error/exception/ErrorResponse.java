package dev.kangmin.pawpal.golbal.error.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.errorMessage = errorCode.getErrorMessage();
    }
}
