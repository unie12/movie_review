package com.example.movie_review.auth;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.kobis.BoxOfficeMovieDTO;
import com.example.movie_review.user.DTO.WeeklyUserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyHomeDTO {
    private List<WeeklyUserDTO> reviewKing;
    private List<WeeklyUserDTO> ratingKing;
    private List<BoxOfficeMovieDTO> weeklyBoxOffice;

    public WeeklyHomeDTO(List<WeeklyUserDTO> reviewKing, List<WeeklyUserDTO> ratingKing, List<BoxOfficeMovieDTO> weeklyBoxOffice) {
        this.reviewKing = reviewKing;
        this.ratingKing = ratingKing;
        this.weeklyBoxOffice = weeklyBoxOffice;
    }
}
