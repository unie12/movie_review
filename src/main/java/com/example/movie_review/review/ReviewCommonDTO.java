package com.example.movie_review.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCommonDTO {
    private Long id;
    private String text;
}
