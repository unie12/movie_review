package com.example.movie_review.auth;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.kobis.BoxOfficeMovieDTO;
import com.example.movie_review.movieDetail.DTO.MovieUiDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class DailyHomeDTO {
    private List<BoxOfficeMovieDTO> dailyBoxOffice;
    private List<MovieUiDTO> tmdbPopular;
    private List<MovieUiDTO> tmdbTrend;

    public DailyHomeDTO(List<BoxOfficeMovieDTO> dailyBoxOffice, List<MovieUiDTO> tmdbPopular, List<MovieUiDTO> tmdbTrend) {
        this.dailyBoxOffice = dailyBoxOffice;
        this.tmdbPopular = tmdbPopular;
        this.tmdbTrend = tmdbTrend;
    }
}
