package com.example.movie_review.review.service;

import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.heart.HeartRepository;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.repository.ReviewRepository;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HeartRepository heartRepository;

    private final UserService userService;
    private final DbMovieService dbMovieService;


    /**
     * 해당 영화에 대한 해당 유저의 리뷰 정보 가져오기
     */
    public Optional<Review> getReview(Long movieId, String email) {
        User user = userService.getUserByEmail(email);
        DbMovies dbMovie = dbMovieService.getDbMovieById(movieId);

        return reviewRepository.findByDbMoviesAndUser(dbMovie, user);
    }

    /**
     * 해당 영화에 대한 해당 유저의 리뷰를 확인
     * 1. 존재하지 않을 시 -> save
     * 2. 존재할 시 -> update
     */
    public Review saveOrUpdateReview(Long movieId, String email, String reviewContext) {
        User user = userService.getUserByEmail(email);
        DbMovies dbMovie = dbMovieService.getDbMovieById(movieId);
        Optional<Review> byDbMoviesAndUser = reviewRepository.findByDbMoviesAndUser(dbMovie, user);

        if(byDbMoviesAndUser.isPresent()) {
            Review review = byDbMoviesAndUser.get();
            review.setContext(reviewContext);
            review.setUploadDate(LocalDateTime.now());
            return reviewRepository.save(review);
        }
        else {
            Review review = new Review();
            review.setContext(reviewContext);
            review.setUser(user);
            review.setDbMovies(dbMovie);
            return reviewRepository.save(review);
        }
    }

    /**
     * 해당 영화에 대한 해당 유저의 review 삭제
     */
    public void deleteReview(Long movieId, String email) {
        User user = userService.getUserByEmail(email);
        DbMovies dbMovie = dbMovieService.getDbMovieById(movieId);
        Review existReview = getReviewByMovieAndUser(dbMovie, user);

        reviewRepository.delete(existReview);
    }


    public Review getReviewByMovieAndUser(DbMovies dbMovie, User user) {
        return reviewRepository.findByDbMoviesAndUser(dbMovie, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Review"));
    }

    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Review Id"));
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public boolean isLikedByCurrentUser(Review review, String email) {
        return heartRepository.existsByUserAndReview(userService.getUserByEmail(email), review);
    }


    public List<Review> findReviewByDbMovies(DbMovies dbMovie) {
        return reviewRepository.findReviewByDbMovies(dbMovie);
    }

    public Long getTotalReviews() {
        return reviewRepository.getTotalReviews();
    }
}
