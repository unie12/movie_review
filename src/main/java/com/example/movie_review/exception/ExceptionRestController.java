package com.example.movie_review.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception-example")
@RequiredArgsConstructor
public class ExceptionRestController {
    public void throwRuntimeException() {
        throw new RuntimeException("Runtime exception occurred!");
    }

}
