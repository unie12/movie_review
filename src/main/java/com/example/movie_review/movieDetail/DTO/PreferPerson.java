package com.example.movie_review.movieDetail.DTO;

import lombok.Data;

@Data
public class PreferPerson {
    private Long id;
    private String profile_path;
    private String original_name;
    private String name;
    private int frequency;


    public PreferPerson(Long id, String profile_path, String original_name, String name, int frequency) {
        this.id = id;
        this.profile_path = profile_path;
        this.original_name = original_name;
        this.name = name;
        this.frequency = frequency;
    }

}
