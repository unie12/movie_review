package com.example.movie_review.service;

import com.example.movie_review.domain.ENUM.ErrorCode;
import com.example.movie_review.domain.MyException;
import org.springframework.stereotype.Service;

@Service
public class ExceptionService {

    public void login() {
        throw new MyException(ErrorCode.USERNAME_NOT_FOUND);
    }

    public void writeComment() {
        throw new MyException(ErrorCode.INVALID_PERMISSION);
    }

    public void join() {
        throw new MyException(ErrorCode.DUPLICATED_USER_NAME);
    }

    public void editComment() {
        throw new MyException(ErrorCode.DATABASE_ERROR);
    }

}
