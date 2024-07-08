package com.example.movie_review.tag;

import lombok.Data;

@Data
public class TagRequest {
    private Long userId;
    private Long movieId;
    private String tag;
}
