package com.example.movie_review.domain.DTO;

import com.example.movie_review.domain.review.Comment;
import com.example.movie_review.domain.review.Review;
import com.example.movie_review.user.User;
import lombok.Data;

@Data
public class CommentCreateRequest {

    private String body;
    public Comment toEntity(Review review, User user) {
        return Comment.builder()
                .user(user)
                .review(review)
                .body(body)
                .build();
    }
}
