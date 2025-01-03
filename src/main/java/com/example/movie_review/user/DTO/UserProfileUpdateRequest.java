package com.example.movie_review.user.DTO;

import com.example.movie_review.movieDetail.DTO.MovieDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String nickname;
    private Long age;
    private String gender;
    private String mbti;
    private String department;
    private List<Long> preferredGenreIds;
    private List<MovieDTO> favoriteMovies;
}
