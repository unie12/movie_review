package com.example.movie_review.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRecommendDTO {
    private String tmdbId;
    private String title;
    private String poster_path;
    private String popularity;
}
