package com.example.movie_review.user.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class UserMovieInfoDTO {
    private String mbti;
    private String department;

    public UserMovieInfoDTO(String mbti, String department) {
        this.mbti = mbti;
        this.department = department;
    }
}
