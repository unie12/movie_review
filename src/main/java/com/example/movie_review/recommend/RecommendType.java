package com.example.movie_review.recommend;

import lombok.Getter;

@Getter
public enum RecommendType {
    CONTENT_BASED,
    COLLABORATIVE_FILTERING,
    HYBRID,
    NCF;
}
