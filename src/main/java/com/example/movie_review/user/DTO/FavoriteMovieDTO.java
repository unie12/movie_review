package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FavoriteMovieDTO {
    private UserCommonDTO userCommonDTO;
    private List<MovieCommonDTO> favoriteMovies;
}
