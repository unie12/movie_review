package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class FavoriteMovieDTO implements UserActivityDTO{
    private UserCommonDTO userCommonDTO;
    private List<FavoriteMovieItem> favoriteMovies;

    @Override
    public List<?> getActivityItems() {
        return favoriteMovies;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCommonDTO", userCommonDTO);
        map.put("favoriteMovies", favoriteMovies);
        return map;
    }
}
