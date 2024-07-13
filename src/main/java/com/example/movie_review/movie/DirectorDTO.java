package com.example.movie_review.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DirectorDTO {
    private Long id;
    private String name;
    private String profile_path;
}