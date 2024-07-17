package com.example.movie_review.movieDetail;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class GenreDto {
    private Long id;
    private String name;
}
