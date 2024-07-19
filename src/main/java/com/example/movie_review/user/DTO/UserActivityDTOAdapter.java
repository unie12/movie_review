package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import lombok.Data;

import java.util.List;

public class UserActivityDTOAdapter implements UserActivityDTO {
    private final Object originalDTO;
    public UserActivityDTOAdapter(Object dto) {
        this.originalDTO = dto;
    }

    @Override
    public UserCommonDTO getUserCommonDTO() {
        if (originalDTO instanceof FavoriteMovieDTO) {
            return ((FavoriteMovieDTO) originalDTO).getUserCommonDTO();
        } else if (originalDTO instanceof RatingDTO) {
            return ((RatingDTO) originalDTO).getUserCommonDTO();
        }
        throw new IllegalArgumentException("Unsupported DTO type");
    }

    @Override
    public List<?> getActivityItems() {
        if (originalDTO instanceof FavoriteMovieDTO) {
            return ((FavoriteMovieDTO) originalDTO).getFavoriteMovies();
        } else if (originalDTO instanceof RatingDTO) {
            return ((RatingDTO) originalDTO).getRatings();
        }
        throw new IllegalArgumentException("Unsupported DTO type");
    }

    @Override
    public List<MovieCommonDTO> getMovies() {
        return null;
    }

    public Object getOriginalDTO() {
        return originalDTO;
    }


}
