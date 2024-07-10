package com.example.movie_review.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * exceptionrestcontroller 뿐만 아닌 해당 패키지의 다른 controller엣서 발생한 exception도 잘 처리
 */
//@RestControllerAdvice(basePackages = "com.example.movie_review")
@RestControllerAdvice(annotations = Controller.class)
@RequestMapping("/exception-example")
@RequiredArgsConstructor
public class ExceptionManager {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<?> myExceptionHandler(MyException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(new ExceptionDto(e.getErrorCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return "Runtime Exception 발생!";
    }
}
