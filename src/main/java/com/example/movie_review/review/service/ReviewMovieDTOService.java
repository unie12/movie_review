package com.example.movie_review.review.service;

import com.example.movie_review.dbMovie.repository.DbMovieRepository;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.heart.Heart;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.review.*;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.review.DTO.ReviewMovieDTO;
import com.example.movie_review.review.repository.ReviewRepository;
import com.example.movie_review.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewMovieDTOService {

    private final ReviewRepository reviewRepository;
    private final DbMovieRepository dbMovieRepository;

    private List<ReviewMovieDTO> cachedPopularReviews;
    private List<ReviewMovieDTO> cachedRecentReviews;

    private final DbRatingService dbRatingService;
    private final ReviewService reviewservice;
    private final MovieCommonDTOService movieCommonDTOService;
    private final ReviewDTOService reviewDTOService;

    /**
     * @param count: 총 몇개의 리뷰를 보여줄건지
     * @return
     */
    public List<ReviewMovieDTO> getRandomPopularReviews(int count) {
        List<Review> popularReviews = reviewRepository.findPopularReviews(0);
        Collections.shuffle(popularReviews);
        return popularReviews.stream()
                .limit(count)
                .map(this::getReviewMovieDTO)
                .collect(Collectors.toList());
    }


    /**
     * 홈 리뷰 관련
     */

    public void updateReviewCache() {
        this.cachedPopularReviews = getPopularReviews(PageRequest.of(0, 10)).getContent();
        this.cachedRecentReviews = getRecentReviews(PageRequest.of(0, 10)).getContent();
    }

    public Page<ReviewMovieDTO> getRecentReviews(Pageable pageable) {
        Page<Review> recentReviews = reviewRepository.findRecentReviewsWithPagination(pageable);
        return recentReviews.map(this::getReviewMovieDTO);
    }

    public Page<ReviewMovieDTO> getPopularReviews(Pageable pageable) {
        List<Review> popularReviews = reviewRepository.findPopularReviewsWithMinHearts(0);

        Collections.shuffle(popularReviews);

        // 페이지네이션 적용
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), popularReviews.size());
        List<Review> pageContent = popularReviews.subList(start, end);

        // ReviewMovieDTO로 변환
        List<ReviewMovieDTO> reviewDTOs = pageContent.stream()
                .map(this::getReviewMovieDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewDTOs, pageable, popularReviews.size());
    }

    public List<ReviewMovieDTO> getMixedHomeReviews(int count) {
//        if (cachedPopularReviews == null || cachedRecentReviews == null) {
        updateReviewCache();
//        }

        List<ReviewMovieDTO> mixedReviews = new ArrayList<>();
        mixedReviews.addAll(cachedPopularReviews);
        mixedReviews.addAll(cachedRecentReviews);

        Collections.shuffle(mixedReviews);

        return mixedReviews.stream()
                .distinct()
                .limit(count)
                .map(this::addFilterInfo)
                .collect(Collectors.toList());
    }

    private ReviewMovieDTO addFilterInfo(ReviewMovieDTO review) {
        review.setFilter(cachedPopularReviews.contains(review) ? "popular" : "recently");
        return review;
    }


    /**
     * 해당 영화 sort에 따른 리뷰 정렬
     */
    public Page<ReviewDTO> getMovieReviews(Long movieTId, int page, int size, String sort, String email) {
        Long movieId = dbMovieRepository.findByTmdbId(movieTId).get().getId();
        Sort.Direction direction = sort.endsWith(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortBy = sort.split(",")[0];

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ReviewDTO> reviewDTOS = reviewRepository.findReviewByMovieId(movieId, pageRequest, sortBy, direction);

        return reviewDTOS.map(dto -> {
            Double userRating = dbRatingService.getDbRating(dto.getUser().getEmail(), movieId)
                    .map(DbRatings::getScore)
                    .orElse(null);
            Review review = reviewRepository.findById(dto.getReview().getId()).orElse(null);
            boolean isLikeByCurrentUser = reviewservice.isLikedByCurrentUser(review, email);
            dto.setUserRating(userRating);
            dto.setLikedByCurrentUser(isLikeByCurrentUser);
            return dto;
        });
    }

    public List<ReviewMovieDTO> getReviewMovieDTOs(List<Review> reviews, String userEmail) {
        return reviews.stream()
                .map(this::getReviewMovieDTO)
                .collect(Collectors.toList());
    }

    public ReviewMovieDTO getReviewMovieDTO(Review review) {
        User user = review.getUser();
        DbMovies dbMovie = review.getDbMovies();
        MovieDetails movieDetails = dbMovie.getMovieDetails();

        MovieCommonDTO movieCommonDTO = movieCommonDTOService.getMovieCommonDTO(dbMovie, movieDetails);

        boolean isLikedByCurrentUser = reviewservice.isLikedByCurrentUser(review, user.getEmail());
        ReviewDTO reviewDTO = reviewDTOService.createReviewDTO(review, user.getEmail(), isLikedByCurrentUser);

        return ReviewMovieDTO.builder()
                .movieCommonDTO(movieCommonDTO)
                .reviewDTO(reviewDTO)
                .isLikedByCurrentUser(review.getHearts().stream()
                        .anyMatch(heart -> heart.getUser().getEmail().equals(user.getEmail())))
                .original_title(movieDetails.getOriginal_title())
                .reviewDate(review.getUploadDate())
                .heartCnt(review.getHeartCount())
                .heartDate(review.getHearts().stream()
//                        .filter(heart -> heart.getUser().getEmail().equals(user.getEmail()))  // userEmail을 파라미터로 받아야 함
                        .map(Heart::getHeartTime)
                        .findFirst()
                        .orElse(null))
                .build();
    }
}
