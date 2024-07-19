package com.example.movie_review.user;

import lombok.Data;

@Data
public class SortOption {
    private String key;
    private String description;

    public SortOption(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
