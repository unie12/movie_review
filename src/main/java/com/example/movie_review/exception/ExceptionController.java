package com.example.movie_review.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception-example")
@RequiredArgsConstructor
public class ExceptionController {

    @GetMapping("/throw-exception/1")
    public ResponseEntity<?> throwException1() {
        // ex) username으로 User을 찾을 수 없는 경우 발생한 경우
        ErrorCode errorCode = ErrorCode.USERNAME_NOT_FOUND;

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ExceptionDto(errorCode));
    }

    @GetMapping("/throw-exception/2")
    public ResponseEntity<?> throwException2() {
        // ex) 사용자가 인증, 인가에 실패한 경우
        ErrorCode errorCode = ErrorCode.INVALID_PERMISSION;

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ExceptionDto(errorCode, "ADMIN만 접근 가능합니다"));
    }

    @GetMapping("/throw-exception/3")
    public ResponseEntity<?> throwException3() {
        // username 중복
        ErrorCode errorCode = ErrorCode.DUPLICATED_USER_NAME;

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ExceptionDto(errorCode));
    }

    @GetMapping("/throw-exception/4")
    public ResponseEntity<?> throwException4() {
        // ex) DB 에러가 발생한 경우
        ErrorCode errorCode = ErrorCode.DATABASE_ERROR;

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ExceptionDto(errorCode));
    }
}
