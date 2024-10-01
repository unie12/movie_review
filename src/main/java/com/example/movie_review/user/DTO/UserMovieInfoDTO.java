package com.example.movie_review.user.DTO;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMovieInfoDTO {
    private String mbti;
    private String department;
    private String email;

}
