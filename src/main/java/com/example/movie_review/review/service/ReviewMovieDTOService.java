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
import org.springframework.transaction.annotation.Transactional;

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
    @Scheduled(fixedRate = 18000000) // 30분
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
        checkCacheExist();

        switch (event.getEventType()) {
            case CREATED:
                createRecentReviewCache(event.getReview());
                break;
            case UPDATED:
                updateRecentReviewCache(event.getReview());
                break;
            case DELETED:
                removeReviewFromCache(event.getReview().getId());
            case HEART:
                updateSingleReviewInList(event.getReview());
                break;
            case ACCOUNT:
                updateReviewCache();
                break;
        }
    }

    /**
     * recent 캐시 리뷰에서 페이지네이션 적용해서 반환
     */
    public Page<ReviewMovieDTO> getRecentReviews(Pageable pageable, User currentUser) {
        checkCacheExist();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cachedRecentReviews.size());
        List<ReviewMovieDTO> pageContent = cachedRecentReviews.subList(start, end);

        return new PageImpl<>(addUserSpecialInfo(pageContent, currentUser), pageable, cachedRecentReviews.size());
    }

    /**
     * popular 캐시 리뷰에서 페이지네이션 적용해서 반환
     */
    public Page<ReviewMovieDTO> getPopularReviews(Pageable pageable, User currentUser) {
        checkCacheExist();

        // 페이지네이션 적용
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cachedPopularReviews.size());
        List<ReviewMovieDTO> pageContent = cachedPopularReviews.subList(start, end);

        return new PageImpl<>(addUserSpecialInfo(pageContent, currentUser), pageable, cachedPopularReviews.size());
    }

    public List<ReviewMovieDTO> addUserSpecialInfo(List<ReviewMovieDTO> reviews, User currentUser) {
        return reviews.stream()
                .map(review -> {
                    boolean isLikedByCurrentUser = reviewservice.isLikedByCurrentUser(reviewservice.getReviewById(review.getReviewDTO().getReview().getId()), currentUser.getEmail());
                    ReviewDTO reviewDTO = review.getReviewDTO();
                    reviewDTO.setLikedByCurrentUser(isLikedByCurrentUser);
                    review.setReviewDTO(reviewDTO);
                    return review;
                })
                .collect(Collectors.toList());
    }

    /**
     * 홈에서 인기 리뷰 보여줄 때 사용
     */
    public List<ReviewMovieDTO> getMixedHomeReviews(int count) {
        checkCacheExist();

        List<ReviewMovieDTO> mixedReviews = new ArrayList<>();
        int popularReviewCount = Math.min(cachedPopularReviews.size(), 10);
        mixedReviews.addAll(cachedPopularReviews.subList(0, popularReviewCount));

        int recentReviewCount = Math.min(cachedRecentReviews.size(), 10);
        mixedReviews.addAll(cachedRecentReviews.subList(0, recentReviewCount));

        Collections.shuffle(mixedReviews);
        
        return mixedReviews.stream()
                .filter(review -> !review.getReviewDTO().isSpoiler())
                .distinct()
                .limit(count)
                .map(this::addFilterInfo)
                .collect(Collectors.toList());
    }

    /**
     * 해당 리뷰의 filter 적용
     */
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

//    public void updatePopularReviewCache(Review review) {
//        ReviewMovieDTO newReviewDTO = getReviewMovieDTO(review);
//        cachedPopularReviews.add(0, newReviewDTO);
//        if (cachedPopularReviews.size() > 1000) {
//            cachedPopularReviews.remove(cachedPopularReviews.size()-1);
//        }
//    }

    /**
     * 리뷰 수정이 이루어진 경우 recent 리뷰를 앞에 땡겨오기
     */
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

    /**
     * 리뷰 생성 시 recent 맨 앞에 생성
     */
    public void createRecentReviewCache(Review review) {
        ReviewMovieDTO newReviewDTO = getReviewMovieDTO(review);
        cachedRecentReviews.add(0, newReviewDTO);
        if (cachedRecentReviews.size() > 1000) {
            cachedRecentReviews.remove(cachedRecentReviews.size()-1);
        }
    }

    /**
     * 리뷰 삭제 시 해당 리뷰를 두 개의 캐시 리뷰에서 삭제
     */
    public void removeReviewFromCache(Long reviewId) {
        cachedRecentReviews.removeIf(review -> review.getReviewDTO().getReview().getId().equals(reviewId));
        cachedPopularReviews.removeIf(review -> review.getReviewDTO().getReview().getId().equals(reviewId));
    }


    /**
     * 리류 좋아요 클릭 시 해당 리뷰에 대해 두 개의 캐시 리뷰에서 업데이트
     */
    public void updateSingleReviewInList(Review updateReview) {
        ReviewMovieDTO newReviewDTO = getReviewMovieDTO(updateReview);

        // popular cache 업데이트
        Optional<ReviewMovieDTO> existingPopularReviewOpt = cachedPopularReviews.stream()
                .filter(r -> r.getReviewDTO().getReview().getId().equals(updateReview.getId()))
                .findFirst();

        if (existingPopularReviewOpt.isPresent()) {
            int index = cachedPopularReviews.indexOf(existingPopularReviewOpt.get());
            cachedPopularReviews.set(index, newReviewDTO);
        }

        // recent cache 업데이트
        Optional<ReviewMovieDTO> existingRecentReviewOpt = cachedRecentReviews.stream()
                .filter(r -> r.getReviewDTO().getReview().getId().equals(updateReview.getId()))
                .findFirst();

        if (existingRecentReviewOpt.isPresent()) {
            int index = cachedRecentReviews.indexOf(existingRecentReviewOpt.get());
            cachedRecentReviews.set(index, newReviewDTO);
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
        Page<ReviewDTO> reviewDTOS = reviewRepository.findReviewByDbMoviesId(movieId, pageRequest, sortBy, direction);

        return reviewDTOS.map(dto -> {
            Double userRating = dbRatingService.getDbRating(dto.getUser().getEmail(), movieId)
                    .map(DbRatings::getScore)
                    .orElse(null);
            Review review = reviewRepository.findById(dto.getReview().getId()).orElse(null);
            boolean isLikeByCurrentUser = reviewservice.isLikedByCurrentUser(review, email);
            dto.setUserRating(userRating);
            dto.setLikedByCurrentUser(isLikeByCurrentUser);
            dto.setSpoiler(review != null && review.isSpoiler());
            return dto;
        });
    }

    public List<ReviewMovieDTO> getReviewMovieDTOs(List<Review> reviews, String userEmail) {
        return reviews.stream()
                .map(this::getReviewMovieDTO)
                .collect(Collectors.toList());
    }

    // 현재 사용자를 기준으로 해당 review user와 비교해야지
    public ReviewMovieDTO getReviewMovieDTO(Review review) {
        User user = review.getUser();
        DbMovies dbMovie = review.getDbMovies();
        MovieDetails movieDetails = dbMovie.getMovieDetails();

        MovieCommonDTO movieCommonDTO = movieCommonDTOService.getMovieCommonDTO(dbMovie, movieDetails);

//        boolean isLikedByCurrentUser = reviewservice.isLikedByCurrentUser(review, user.getEmail());
        ReviewDTO reviewDTO = reviewDTOService.createReviewDTO(review, user.getEmail(), false); // 여기도 current user를

        return ReviewMovieDTO.builder()
                .movieCommonDTO(movieCommonDTO)
                .reviewDTO(reviewDTO)
//                .isLikedByCurrentUser(review.getHearts().stream()
//                        .anyMatch(heart -> heart.getUser().getEmail().equals(user.getEmail())))
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
