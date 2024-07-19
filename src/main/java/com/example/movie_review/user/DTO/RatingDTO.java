package com.example.movie_review.user.DTO;

import com.example.movie_review.dbRating.DbRatingDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RatingDTO {
    private UserCommonDTO userCommonDTO;
    private List<DbRatingDTO> ratings;
}
