package com.example.movie_review.user.DTO;

import com.example.movie_review.user.DTO.UserDTO;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDTOService {
    private final UserService userService;

    public UserCommonDTO getUserCommonDTO(Authentication auth) {
        String email = auth.getName();
        User user = userService.getUserByEmail(email);

        UserCommonDTO.UserCommonDTOBuilder userCommonDTo = UserCommonDTO.builder()
                .email(email)
                .nickname(user.getNickname())
                .picture(user.getPicture());

        return userCommonDTo.build();
    }

    public UserDTO getuserDTO(String userEmail) throws AccessDeniedException {
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

    public List<UserDTO> getSubscribers(String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        return user.getSubscribers().stream()
                .map(subscription -> {
                    try {
                        return getuserDTO(subscription.getSubscriber().getEmail());
                    } catch (AccessDeniedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }


    public List<UserDTO> getSubscriptions(String userEmail) {
        User user = userService.getUserByEmail(userEmail);

        return user.getSubscriptions().stream()
                .map(subscription -> {
                    try {
                        return getuserDTO(subscription.getSubscribed().getEmail());
                    } catch (AccessDeniedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
