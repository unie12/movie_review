package com.example.movie_review.review.DTO;

import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserCommonDTO;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ReviewListDTO implements UserActivityDTO {
    private UserCommonDTO userCommonDTO;
    private List<ReviewMovieDTO> reviews;

    @Override
    public List<?> getActivityItems() {
        return reviews;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCommonDTO", userCommonDTO);
        map.put("reviews", reviews);
        return map;
    }
}
