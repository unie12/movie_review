package com.example.movie_review.review.service;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.repository.DbMovieRepository;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.heart.Heart;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.review.DTO.ReviewMovieDTO;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.event.ReviewEvent;
import com.example.movie_review.review.repository.ReviewRepository;
import com.example.movie_review.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
     * Review Cache 업데이트
     */
    @Scheduled(fixedRate = 144000000) // 4시간
    public void updateReviewCache() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        int minHeartCont = 0;

        List<Review> popularReviews = reviewRepository.findPopularReviewsWithMinHearts(minHeartCont, startDate);
        this.cachedPopularReviews = popularReviews.stream()
                .map(this::getReviewMovieDTO)
                .collect(Collectors.toList());
        Collections.shuffle(cachedPopularReviews);

        List<Review> recentReviews = reviewRepository.findRecentReviews();
        this.cachedRecentReviews = recentReviews.stream()
                .map(this::getReviewMovieDTO)
                .collect(Collectors.toList());
    }

    @EventListener
    public void handleReviewEvent(ReviewEvent event) {
        switch (event.getEventType()) {
            case CREATED:
                createRecentReviewCache(event.getReview());
                break;
            case UPDATED:
                updateRecentReviewCache(event.getReview());
                break;
            case DELETED:
                updateReviewCache();
                break;
        }
    }

    public Page<ReviewMovieDTO> getRecentReviews(Pageable pageable) {
        checkCacheExist();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cachedRecentReviews.size());
        List<ReviewMovieDTO> pageContent = cachedRecentReviews.subList(start, end);

        return new PageImpl<>(pageContent, pageable, cachedRecentReviews.size());
//        Page<Review> recentReviews = reviewRepository.findRecentReviewsWithPagination(pageable);
//        return recentReviews.map(this::getReviewMovieDTO);
    }

    public Page<ReviewMovieDTO> getPopularReviews(Pageable pageable) {
        checkCacheExist();

        // 페이지네이션 적용
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cachedPopularReviews.size());
        List<ReviewMovieDTO> pageContent = cachedPopularReviews.subList(start, end);

        return new PageImpl<>(pageContent, pageable, cachedPopularReviews.size());
    }

    public List<ReviewMovieDTO> getMixedHomeReviews(int count) {
        checkCacheExist();

        List<ReviewMovieDTO> mixedReviews = new ArrayList<>();
        int popularReviewCount = Math.min(cachedPopularReviews.size(), 10);
        mixedReviews.addAll(cachedPopularReviews.subList(0, popularReviewCount));

        int recentReviewCount = Math.min(cachedRecentReviews.size(), 10);
        mixedReviews.addAll(cachedRecentReviews.subList(0, recentReviewCount));

        Collections.shuffle(mixedReviews);
        
        return mixedReviews.stream()
                .distinct()
                .limit(count)
                .map(this::addFilterInfo)
                .collect(Collectors.toList());
    }

    private ReviewMovieDTO addFilterInfo(ReviewMovieDTO review) {
        int popularReviewCount = Math.min(cachedPopularReviews.size(), 10);
        review.setFilter(cachedPopularReviews.subList(0, popularReviewCount).contains(review) ? "popular" : "recently");
        return review;
    }

    private void checkCacheExist() {
        if (cachedPopularReviews == null || cachedRecentReviews == null) {
            updateReviewCache();
        }
    }

    public void updatePopularReviewCache(Review review) {
        ReviewMovieDTO newReviewDTO = getReviewMovieDTO(review);
        cachedPopularReviews.add(0, newReviewDTO);
        if (cachedPopularReviews.size() > 1000) {
            cachedPopularReviews.remove(cachedPopularReviews.size()-1);
        }
    }

    public void updateRecentReviewCache(Review review) {
        ReviewMovieDTO newReviewDTO = getReviewMovieDTO(review);
        Optional<ReviewMovieDTO> existingReviewOpt = cachedRecentReviews.stream()
                .filter(r -> r.getReviewDTO().getReview().getId().equals(review.getId()))
                .findFirst();

        int index = cachedRecentReviews.indexOf(existingReviewOpt.get());

        if (index > 0) {
            cachedRecentReviews.remove(index);
            cachedRecentReviews.add(0, newReviewDTO);
        } else {
            cachedRecentReviews.set(index, newReviewDTO);
        }
    }

    public void createRecentReviewCache(Review review) {
        ReviewMovieDTO newReviewDTO = getReviewMovieDTO(review);

        cachedRecentReviews.add(0, newReviewDTO);
        if (cachedRecentReviews.size() > 1000) {
            cachedRecentReviews.remove(cachedRecentReviews.size()-1);
        }
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
