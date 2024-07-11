package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovieRepository;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final DbMovieRepository dbMovieRepository;

    /**
     * 해당 영화에 대한 해당 유저의 리뷰 정보 가져오기
     */
    public Optional<Review> getReview(Long movieId, String email) {
        User user = getUserByEmail(email);
        DbMovies dbMovie = getDbMovieById(movieId);

        return reviewRepository.findByDbMoviesAndUser(dbMovie, user);
    }

    /**
     * 해당 영화에 대한 해당 유저의 리뷰를 확인
     * 1. 존재하지 않을 시 -> save
     * 2. 존재할 시 -> update
     */
    public Review saveOrUpdateReview(Long movieId, String email, String reviewContext) {
        User user = getUserByEmail(email);
        DbMovies dbMovie = getDbMovieById(movieId);
        Optional<Review> byDbMoviesAndUser = reviewRepository.findByDbMoviesAndUser(dbMovie, user);

        if(byDbMoviesAndUser.isPresent()) {
            Review review = byDbMoviesAndUser.get();
            review.setContext(reviewContext);
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
        User user = getUserByEmail(email);
        DbMovies dbMovie = getDbMovieById(movieId);
        Review existReview = getReviewByMovieAndUser(dbMovie, user);

        reviewRepository.delete(existReview);
    }

    /**
     * 사용자, 영화, 리뷰 조회
     */
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Email"));
    }

    private DbMovies getDbMovieById(Long movieId) {
        return dbMovieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Movie ID"));
    }

    private Review getReviewByMovieAndUser(DbMovies dbMovie, User user) {
        return reviewRepository.findByDbMoviesAndUser(dbMovie, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Review"));
    }
}
