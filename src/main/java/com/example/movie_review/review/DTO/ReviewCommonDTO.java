package com.example.movie_review.review.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCommonDTO {
    private Long id;
    private String text;

    public ReviewCommonDTO(Long id, String text) {
        this.id = id;
        this.text = text;
    }
}
