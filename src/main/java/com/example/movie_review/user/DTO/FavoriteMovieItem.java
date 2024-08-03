package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteMovieItem {
    private MovieCommonDTO movieCommonDTO;
    private LocalDateTime favoriteDate;
}
