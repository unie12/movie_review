package com.example.movie_review.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserCommonDTO {
    private Long id;
    private String email;
    private String nickname;
    private String picture;
}
