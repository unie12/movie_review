package com.example.movie_review.controller.exception;

import com.example.movie_review.domain.DTO.ExceptionDto;
import com.example.movie_review.domain.MyException;
import com.example.movie_review.service.ExceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception-example")
@RequiredArgsConstructor
public class ExceptionRestController {

    private final ExceptionService exceptionService;

    @GetMapping("/throw-my-exception/1")
    public void throwMyException1() {
        exceptionService.login();
    }

    @GetMapping("/throw-my-exception/2")
    public void throwMyException2() {
        exceptionService.writeComment();
    }

    @GetMapping("/throw-my-exception/3")
    public void throwMyException3() {
        exceptionService.join();
    }

    @GetMapping("/throw-my-exception/4")
    public void throwMyException4() {
        exceptionService.editComment();
    }

    @GetMapping("/throw-runtime-exception")
    public void throwRuntimeException() {
        throw new RuntimeException("Runtime exception occurred!");
    }


//    @ExceptionHandler(MyException.class)
//    public ResponseEntity<?> myExceptionHandler(MyException e) {
//        e.printStackTrace();
//        return ResponseEntity.status(e.getErrorCode().getStatus())
//                .body(new ExceptionDto(e.getErrorCode()));
//    }

}
