package com.example.movie_review.user.service;

import com.example.movie_review.heart.Heart;
import com.example.movie_review.review.Review;
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

@Service("heart")
public class HeartService extends AbstractUserActivityService {
    private final UserService userService;
    private final ReviewMovieDTOService reviewMovieDTOService;

    public HeartService(UserDTOService userDTOService, UserService userService, ReviewMovieDTOService reviewMovieDTOService) {
        super(userDTOService);
        this.userService = userService;
        this.reviewMovieDTOService = reviewMovieDTOService;
    }

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        User user = userService.getUserByEmail(userEmail);

        List<Review> likedReviews = user.getHearts().stream()
                .map(Heart::getReview)
                .collect(Collectors.toList());

        List<ReviewMovieDTO> reviewMovieDTOS = reviewMovieDTOService.getReviewMovieDTOs(likedReviews, userEmail);

        int start = page * size;
        int end = Math.min(start + size, reviewMovieDTOS.size());

        UserCommonDTO userCommonDTO = userDTOService.getUserCommonDTO(user);
        List<ReviewMovieDTO> sortedReviews = sortHeart(reviewMovieDTOS, sort);

        ReviewListDTO dto = ReviewListDTO.builder()
                .userCommonDTO(userCommonDTO)
                .reviews(sortedReviews.subList(start, end))
                .build();

        return new UserActivityDTOAdapter(dto);
    }

    private List<ReviewMovieDTO> sortHeart(List<ReviewMovieDTO> reviewMovieDTOS, String sort) {
        switch (sort) {
            case "heart_date_desc":
                return reviewMovieDTOS.stream()
                        .sorted((a, b) -> b.getHeartDate().compareTo(a.getHeartDate()))
                        .collect(Collectors.toList());
            case "heart_date_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparing(ReviewMovieDTO::getHeartDate))
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
                new SortOption("heart_date_desc", "리뷰 좋아요 최근"),
                new SortOption("heart_date_asc", "리뷰 좋아요 과거"),
                new SortOption("heart_count_desc", "좋아요 많은순"),
                new SortOption("heart_count_asc", "좋아요 적은순")
        ));
        options.addAll(getCommonSortOptions());
        return options;
    }
}

