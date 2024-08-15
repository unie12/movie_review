package com.example.movie_review.movieDetail.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class KeywordDTO {
    private String keyword;
    private int count;
    private int size;

    public KeywordDTO(String keyword, int count, int size) {
        this.keyword = keyword;
        this.count = count;
        this.size = size;
    }
}
