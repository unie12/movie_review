package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingDTO;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class RatingDTO implements UserActivityDTO{
    private UserCommonDTO userCommonDTO;
    private List<DbRatingDTO> ratings;

    @Override
    public List<?> getActivityItems() {
        return ratings;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCommonDTO", userCommonDTO);
        map.put("ratings", ratings);
        return map;
    }
}
