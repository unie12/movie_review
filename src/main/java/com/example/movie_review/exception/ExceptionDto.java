package com.example.movie_review.exception;

import lombok.Getter;

@Getter
public class ExceptionDto {

    private String result;
    private ErrorCode errorCode;
    private String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.result = "ERROR";
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public ExceptionDto(ErrorCode errorCode, String message) {
        this.result = "ERROR";
        this.errorCode = errorCode;
        this.message = message;
    }
}
