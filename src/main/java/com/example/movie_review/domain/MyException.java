package com.example.movie_review.domain;

import com.example.movie_review.domain.ENUM.ErrorCode;
import lombok.Getter;

@Getter
public class MyException extends RuntimeException {

    private String result;
    private ErrorCode errorCode;
    private String message;

    public MyException(ErrorCode errorCode) {
        this.result = "ERROR";
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
