package com.example.movie_review.genre;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GenreDTO {
    private Long id;
    private String name;

    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
