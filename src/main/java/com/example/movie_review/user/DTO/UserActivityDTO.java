package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.MovieCommonDTO;

import java.util.List;
import java.util.Map;

public interface UserActivityDTO {
    UserCommonDTO getUserCommonDTO();
    List<?> getActivityItems();
//    List<MovieCommonDTO> getMovies();
    Map<String, Object> toMap();
}
