package com.example.movie_review.user.service;

import com.example.movie_review.review.DTO.ReviewListDTO;
import com.example.movie_review.review.DTO.ReviewMovieDTO;
import com.example.movie_review.review.service.ReviewMovieDTOService;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserActivityDTOAdapter;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("review")
public class ReviewService extends AbstractUserActivityService {
    private final UserService userService;
    private final ReviewMovieDTOService reviewMovieDTOService;

    public ReviewService(UserDTOService userDTOService, UserService userService, ReviewMovieDTOService reviewMovieDTOService) {
        super(userDTOService);
        this.userService = userService;
        this.reviewMovieDTOService = reviewMovieDTOService;
    }

    @Override
    public UserActivityDTO getUserActivity(String authEmail, String userEmail, String sort, int page, int size) {
        User user = userService.getUserByEmail(userEmail);
        List<ReviewMovieDTO> reviewMovieDTOS = reviewMovieDTOService.getReviewMovieDTOs(user.getReviews(), userEmail);
        reviewMovieDTOS = reviewMovieDTOService.addUserSpecialInfo(reviewMovieDTOS, userService.getUserByEmail(authEmail));

        int start = page * size;
        int end = Math.min(start + size, reviewMovieDTOS.size());

        List<ReviewMovieDTO> sortedReviews = sortReview(reviewMovieDTOS, sort);
        UserCommonDTO userCommonDTO = userDTOService.getUserCommonDTO(user);

        ReviewListDTO dto = ReviewListDTO.builder()
                .userCommonDTO(userCommonDTO)
                .reviews(sortedReviews.subList(start, end))
                .build();

        return new UserActivityDTOAdapter(dto);
    }

    private List<ReviewMovieDTO> sortReview(List<ReviewMovieDTO> reviewMovieDTOS, String sort) {
        switch (sort) {
            case "review_date_desc":
                return reviewMovieDTOS.stream()
                        .sorted((a, b) -> b.getReviewDate().compareTo(a.getReviewDate()))
                        .collect(Collectors.toList());
            case "review_date_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparing(ReviewMovieDTO::getReviewDate))
                        .collect(Collectors.toList());
            case "heart_count_desc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparingInt(ReviewMovieDTO::getHeartCnt).reversed())
                        .collect(Collectors.toList());
            case "heart_count_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparingInt(ReviewMovieDTO::getHeartCnt))
                        .collect(Collectors.toList());
            default:
                return sortByCommonMovieOptions(reviewMovieDTOS, sort, ReviewMovieDTO::getMovieCommonDTO);
        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        List<SortOption> options = new ArrayList<>(Arrays.asList(
                new SortOption("review_date_desc", "리뷰 최근 작성순"),
                new SortOption("review_date_asc", "리뷰 과거 작성순"),
                new SortOption("heart_count_desc", "좋아요 많은순"),
                new SortOption("heart_count_asc", "좋아요 적은순")
        ));
        options.addAll(getCommonSortOptions());
        return options;
    }
}
