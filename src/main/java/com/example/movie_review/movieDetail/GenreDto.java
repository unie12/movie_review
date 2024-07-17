package com.example.movie_review.movieDetail;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    private Long id;
    private String name;
}
