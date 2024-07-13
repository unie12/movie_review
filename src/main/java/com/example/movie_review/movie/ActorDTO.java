package com.example.movie_review.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActorDTO {
    private Long id;
    private String name;
    private String profile_path;
    private String character_name;
}