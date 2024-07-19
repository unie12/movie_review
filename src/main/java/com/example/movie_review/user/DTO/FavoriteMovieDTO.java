package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FavoriteMovieDTO implements UserActivityDTO{
    private UserCommonDTO userCommonDTO;
    private List<MovieCommonDTO> favoriteMovies;

    @Override
    public List<?> getActivityItems() {
        return favoriteMovies;
    }

    @Override
    public List<MovieCommonDTO> getMovies() {
        return favoriteMovies;
    }
}
