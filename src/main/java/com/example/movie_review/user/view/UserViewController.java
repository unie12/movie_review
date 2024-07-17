package com.example.movie_review.user.view;

import com.example.movie_review.heart.Heart;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.ReviewDTO;
import com.example.movie_review.review.ReviewService;
import com.example.movie_review.subscription.SubscriptionService;
import com.example.movie_review.user.User;
import com.example.movie_review.user.DTO.UserDTO;
import com.example.movie_review.user.DTO.UserDTOService;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/info/{userEmail}")
@RequiredArgsConstructor
public class UserViewController {

    private final UserDTOService userDTOService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final ReviewService reviewService;

    /**
     * 사용자 활동 내역 보기
     */
    @GetMapping("")
    public String userInfo(@PathVariable String userEmail, Model model, Authentication auth) throws AccessDeniedException {
        UserDTO userDTO = userDTOService.getuserDTO(userEmail);
        boolean isSubscribed = subscriptionService.isSubscribed(auth.getName(), userEmail);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("isSubscribed", isSubscribed);
        return "info";
    }

    /**
     * 찜한 영화 확인
     */
    @GetMapping("/{category}")
    public String getUserInfo(@PathVariable String userEmail, @PathVariable String category, Model model, Authentication auth) throws AccessDeniedException {
        User user = userService.getUserByEmail(userEmail);

        //        Info permission 부여
//        if(!user.getEmail().equals(auth.getName())) {
//            throw new AccessDeniedException("You don't have permission to view this user");
//        }

        UserDTO userDTO = userDTOService.getuserDTO(userEmail);
        model.addAttribute("userDTO", userDTO);

        switch (category) {
            case "favorite":
                return "user-favorite-movies";
            case "review":
                List<Review> reviews = user.getReviews();
                List<ReviewDTO> reviewDTOS = reviewService.getReviewDTOs(reviews);
                model.addAttribute("reviewDTOs", reviewDTOS);
                return "user-reviews";
            case "rating":
                return "user-ratings";
            case "heart":
                List<Heart> hearts = user.getHearts();
                List<ReviewDTO> heartReviewDTOs = reviewService.getReviewDTOs(
                        hearts.stream().map(Heart::getReview).collect(Collectors.toList()));
                model.addAttribute("heartReviewDTOs", heartReviewDTOs);
                return "user-hearts";
            case "subscription":
                return "user-subscription";
            case "subscriber":
                return "user-subscriber";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category");
        }
    }

}
