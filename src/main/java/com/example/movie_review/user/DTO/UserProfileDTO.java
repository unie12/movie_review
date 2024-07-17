package com.example.movie_review.user.DTO;


import com.example.movie_review.movieDetail.GenreDto;
import com.example.movie_review.movieDetail.MovieDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfileDTO {
    private String email;
    private String nickname;
    private Long age;
    private String gender;
    private String mbti;
    private List<GenreDto> tmdbGenres;
    private List<Long> userPreferredGenreIds;
    private List<MovieDTO> favoriteMovies;

}
