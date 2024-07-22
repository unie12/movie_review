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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        List<ReviewMovieDTO> sortedReviews = sortReview(reviewMovieDTOS, sort);

        ReviewListDTO dto = ReviewListDTO.builder()
                .userCommonDTO(userCommonDTO)
                .reviews(sortedReviews)
                .build();


        return new UserActivityDTOAdapter(dto);
    }

    private List<ReviewMovieDTO> sortReview(List<ReviewMovieDTO> reviewMovieDTOS, String sort) {
        switch (sort) {
            default:
            case "review_date_desc":
                return reviewMovieDTOS.stream()
                        .sorted((a, b) -> b.getReviewDate().compareTo(a.getReviewDate()))
                        .collect(Collectors.toList());
            case "review_date_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparing(ReviewMovieDTO::getReviewDate))
                        .collect(Collectors.toList());
            case "release_date_desc":
                return reviewMovieDTOS.stream()
                        .sorted((a, b) -> b.getMovieCommonDTO().getRelease_date().compareTo(a.getMovieCommonDTO().getRelease_date()))
                        .collect(Collectors.toList());
            case "release_date_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparing(r -> r.getMovieCommonDTO().getRelease_date()))
                        .collect(Collectors.toList());
            case "runtime_desc":
                return reviewMovieDTOS.stream()
                        .sorted((a, b) -> b.getMovieCommonDTO().getRuntime().compareTo(a.getMovieCommonDTO().getRuntime()))
                        .collect(Collectors.toList());
            case "runtime_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparing(r -> r.getMovieCommonDTO().getRuntime()))
                        .collect(Collectors.toList());
            case "ajou_rating_desc":
                return reviewMovieDTOS.stream()
                        .sorted((a, b) -> b.getMovieCommonDTO().getAjou_rating().compareTo(a.getMovieCommonDTO().getAjou_rating()))
                        .collect(Collectors.toList());
            case "ajou_rating_asc":
                return reviewMovieDTOS.stream()
                        .sorted(Comparator.comparing(r -> r.getMovieCommonDTO().getAjou_rating()))
                        .collect(Collectors.toList());
        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("review_date_desc", "리뷰 최근 작성순"),
                new SortOption("review_date_asc", "리뷰 과거 작성순"),
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순"),
                new SortOption("runtime_desc", "상영 시간 긴순"),
                new SortOption("runtime_asc", "상영 시간 짧은순"),
                new SortOption("ajou_rating_desc", "아주대 평점 높은순"),
                new SortOption("ajou_rating_asc", "아주대 평점 낮은순")

        );
    }
}
