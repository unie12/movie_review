package com.example.movie_review.user.service;

import com.example.movie_review.heart.Heart;
import com.example.movie_review.review.Review;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("heart")
@RequiredArgsConstructor
public class HeartService implements UserActivityService {
    private final com.example.movie_review.review.ReviewService reviewService;
    private final UserService userService;
    private final UserDTOService userDTOService;

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        User user = userService.getUserByEmail(userEmail);

        List<Review> likedReviews = user.getHearts().stream()
                .map(Heart::getReview)
                .collect(Collectors.toList());

        List<ReviewMovieDTO> reviewMovieDTOS = reviewService.getReviewMovieDTOs(likedReviews);
        UserCommonDTO userCommonDTO = userDTOService.getUserCommonDTO(userEmail);

        // 정렬 type에 맞춰서 정렬
        List<ReviewMovieDTO> sortedReviews = sortHeart(reviewMovieDTOS, sort);

        ReviewListDTO dto = ReviewListDTO.builder()
                .userCommonDTO(userCommonDTO)
                .reviews(sortedReviews)
                .build();

        return new UserActivityDTOAdapter(dto);
    }

    private List<ReviewMovieDTO> sortHeart(List<ReviewMovieDTO> reviewMovieDTOS, String sort) {
        switch (sort) {
            case "release_date_desc":
                return reviewMovieDTOS.stream()
                        .sorted((a, b) -> b.getMovieCommonDTO().getRelease_date().compareTo(a.getMovieCommonDTO().getRelease_date()))
                        .collect(Collectors.toList());
            case "release_date_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparing(r -> r.getMovieCommonDTO().getRelease_date()))
                        .collect(Collectors.toList());
//            case "favorite_date_desc":
//                return reviewMovieDTOS.stream()
//                        .sorted((a, b) -> b.getReviewDTO().getCreatedAt().compareTo(a.getReviewDTO().getCreatedAt()))
//                        .collect(Collectors.toList());
//            case "favorite_date_asc":
//                return reviewMovieDTOS.stream()
//                        .sorted(Comparator.comparing(r -> r.getReviewDTO().getCreatedAt()))
//                        .collect(Collectors.toList());
            default:
                return reviewMovieDTOS;
        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("release_date_desc", "개봉 최근"),
                new SortOption("release_date_asc", "개봉 과거"),
                new SortOption("favorite_date_desc", "리뷰 좋아요 최근"),
                new SortOption("favorite_date_asc", "리뷰 좋아요 과거")
        );    }
}

