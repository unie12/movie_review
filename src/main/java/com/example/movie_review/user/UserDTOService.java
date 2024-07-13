package com.example.movie_review.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserDTOService {
    private final UserService userService;

    public UserDTO getuserDTO(String userEmail, Authentication auth) throws AccessDeniedException {
        User user = userService.getUserByEmail(userEmail);

        if(!user.getEmail().equals(auth.getName())) {
            throw new AccessDeniedException("You don't have permission to view this user");
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder()
                .id(user.getId())
                .user(user)
                .favoriteCnt(user.getFavoriteCount())
                .reviewCnt(user.getReviewCount())
                .ratingCnt(user.getRatingCount())
                .heartCnt(user.getHeartCount());

        return userDTO.build();
    }
}
