package com.example.movie_review.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDTOService {
    private final UserService userService;

    public UserDTO getuserDTO(String userEmail, Authentication auth) throws AccessDeniedException {
        User user = userService.getUserByEmail(userEmail);

//        다른 사용자가 다른 사용자의 info url 접속
//        if(!user.getEmail().equals(auth.getName())) {
//            throw new AccessDeniedException("You don't have permission to view this user");
//        }

        Set<Long> likedReviewIds = user.getHearts().stream()
                .map(heart -> heart.getReview().getId())
                .collect(Collectors.toSet());

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder()
                .id(user.getId())
                .user(user)
                .favoriteCnt(user.getFavoriteCount())
                .reviewCnt(user.getReviewCount())
                .ratingCnt(user.getRatingCount())
                .heartCnt(user.getHeartCount())
                .likedReviewIds(likedReviewIds)
                .subscriptionCnt(user.getSubscriptionCount())
                .subscriberCnt(user.getSubscriberCount());

        return userDTO.build();
    }

}
