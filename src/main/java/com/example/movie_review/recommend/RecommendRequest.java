package com.example.movie_review.recommend;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RecommendRequest {
    private List<String> tmdb_ids;
    private Map<String, Double> ratings;
}