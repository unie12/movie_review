package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.movieDetail.MovieDetails;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserService userService;
    private final DbMovieService dbMovieService;
    private final DbRatingService dbRatingService;


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

    public List<ReviewDTO> getReviewDTOs(List<Review> reviews) {
        return reviews.stream().map(review -> {
            Double userRating = dbRatingService.getDbRating(review.getUser().getEmail(), review.getDbMovies().getMovieDetails().getId())
                    .map(DbRatings::getScore)
                    .orElse(null);
            UserCommonDTO userCommonDTO = UserCommonDTO.builder()
                    .email(review.getUser().getEmail())
                    .nickname(review.getUser().getNickname())
                    .picture(review.getUser().getPicture())
                    .build();
            ReviewCommonDTO reviewCommonDTO = ReviewCommonDTO.builder()
                    .id(review.getId())
                    .text(review.getContext())
                    .build();
            return new ReviewDTO(userRating, review.getHeartCount(),userCommonDTO, reviewCommonDTO);
        }).collect(Collectors.toList());
    }

    public List<ReviewMovieDTO> getReviewMovieDTOs(List<Review> reviews) {
        return reviews.stream().map(review -> {
            User user = review.getUser();
            DbMovies dbMovie = review.getDbMovies();
            MovieDetails movieDetails = dbMovie.getMovieDetails();

            Double userRating = dbRatingService.getDbRating(user.getEmail(), movieDetails.getId())
                    .map(DbRatings::getScore)
                    .orElse(null);

            UserCommonDTO userCommonDTO = UserCommonDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .picture(user.getPicture())
                    .build();

            MovieCommonDTO movieCommonDTO = MovieCommonDTO.builder()
                    .id(dbMovie.getId())
                    .tId(movieDetails.getTId())
                    .title(movieDetails.getTitle())
                    .poster_path(movieDetails.getPoster_path())
                    .release_date(movieDetails.getRelease_date())
                    .runtime(movieDetails.getRuntime())
                    .ajou_rating(dbMovie.getDbRatingAvg())
                    .ajou_rating_cnt(dbMovie.getDbRatingCount())
                    .build();

            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .userRating(userRating)
                    .heartCnt(review.getHeartCount())
                    .user(userCommonDTO)
                    .review(ReviewCommonDTO.builder()
                            .id(review.getId())
                            .text(review.getContext())
                            .build())
                    .build();

            return ReviewMovieDTO.builder()
                    .movieCommonDTO(movieCommonDTO)
                    .reviewDTO(reviewDTO)
                    .isLikedByCurrentUser(review.getHearts().stream()
                            .anyMatch(heart -> heart.getUser().getEmail().equals(user.getEmail())))
                    .original_title(movieDetails.getOriginal_title())
                    .reviewDate(review.getUploadDate())
                    .build();

        }).collect(Collectors.toList());
    }


    public List<Review> findReviewByDbMovies(DbMovies dbMovie) {
        return reviewRepository.findReviewByDbMovies(dbMovie);
    }
}
