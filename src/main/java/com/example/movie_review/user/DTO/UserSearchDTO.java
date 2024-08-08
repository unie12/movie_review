package com.example.movie_review.user.DTO;

import lombok.Data;

@Data
public class UserSearchDTO {
    private UserCommonDTO user;
    private boolean isSubscribed;

    public UserSearchDTO(UserCommonDTO user, boolean isSubscribed) {
        this.user = user;
        this.isSubscribed = isSubscribed;
    }
}
