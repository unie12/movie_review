package com.example.movie_review.user.service;

import com.example.movie_review.review.ReviewListDTO;
import com.example.movie_review.review.ReviewMovieDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserActivityDTOAdapter;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.DTO.UserDTOService;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserActivityService;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("review")
@RequiredArgsConstructor
public class ReviewService implements UserActivityService {
    private final com.example.movie_review.review.ReviewService reviewService;
    private final UserService userService;
    private final UserDTOService userDTOService;

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        User user = userService.getUserByEmail(userEmail);
        List<ReviewMovieDTO> reviewMovieDTOS = reviewService.getReviewMovieDTOs(user.getReviews());
        UserCommonDTO userCommonDTO = userDTOService.getUserCommonDTO(userEmail);

        ReviewListDTO dto = ReviewListDTO.builder()
                .userCommonDTO(userCommonDTO)
                .reviews(reviewMovieDTOS)
                .build();

        return new UserActivityDTOAdapter(dto);
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("review_date_desc", "리뷰 최근 작성순"),
                new SortOption("review_date_asc", "리뷰 과거 작성순"),
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순")
        );
    }
}
