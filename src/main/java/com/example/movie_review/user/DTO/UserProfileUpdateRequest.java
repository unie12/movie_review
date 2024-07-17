package com.example.movie_review.user.DTO;

import com.example.movie_review.genre.PreferredGenres;
import com.example.movie_review.movieDetail.MovieDTO;
import com.example.movie_review.user.PreferredMovies;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String nickname;
    private Long age;
    private String gender;
    private String mbti;
    private List<MovieDTO> favoriteMovies;

    private List<Long> preferredMovieIds;
    private List<Long> preferredGenreIds;
}
