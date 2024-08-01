package com.example.movie_review.user.DTO;

import com.example.movie_review.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCommonDTO {
    private Long id;
    private String email;
    private String nickname;
    private String picture;
    private UserRole role;

    public UserCommonDTO(Long id, String email, String nickname, String picture) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
    }
}
