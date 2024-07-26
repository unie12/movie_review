package com.example.movie_review.review;

import com.example.movie_review.dbMovie.DbMovieRepository;
import com.example.movie_review.dbMovie.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.heart.Heart;
import com.example.movie_review.heart.HeartRepository;
import com.example.movie_review.movieDetail.MovieDetails;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final DbMovieRepository dbMovieRepository;
    private final HeartRepository heartRepository;

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

    public Page<ReviewDTO> getMovieReviews(Long movieTId, int page, int size, String sortBy, String email) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Review> reviewPage = reviewRepository.findByDbMovies_MovieDetails_Id(
                dbMovieRepository.findByTmdbId(movieTId).orElseThrow().getId(), pageRequest);

        return reviewPage.map(review -> {
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
            boolean isLikedByCurrentUser = isLikedByCurrentUser(review, email);
            System.out.println("isLikedByCurrentUser = " + isLikedByCurrentUser);
            return new ReviewDTO(userRating, review.getHeartCount(), userCommonDTO, reviewCommonDTO, isLikedByCurrentUser);
        });
    }

    public boolean isLikedByCurrentUser(Review review, String email) {
        return heartRepository.existsByUserAndReview(userService.getUserByEmail(email), review);
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
            return new ReviewDTO(userRating, review.getHeartCount(),userCommonDTO, reviewCommonDTO, false);
        }).collect(Collectors.toList());
    }

    public List<ReviewMovieDTO> getReviewMovieDTOs(List<Review> reviews, String userEmail) {
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
                    .heartCnt(review.getHeartCount())
                    .heartDate(review.getHearts().stream()
                            .filter(heart -> heart.getUser().getEmail().equals(userEmail))  // userEmail을 파라미터로 받아야 함
                            .map(Heart::getHeartTime)
                            .findFirst()
                            .orElse(null))
                    .build();

        }).collect(Collectors.toList());
    }


    public List<Review> findReviewByDbMovies(DbMovies dbMovie) {
        return reviewRepository.findReviewByDbMovies(dbMovie);
    }
}
