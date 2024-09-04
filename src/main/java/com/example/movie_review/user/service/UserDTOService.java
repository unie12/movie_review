package com.example.movie_review.user.service;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingDTO;
import com.example.movie_review.dbRating.DbRatingRepository;
import com.example.movie_review.favoriteMovie.UserFavoriteMovieService;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.repository.ReviewRepository;
import com.example.movie_review.review.service.ReviewService;
import com.example.movie_review.subscription.SubscriptionService;
import com.example.movie_review.user.DTO.*;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDTOService {
    private final UserService userService;
    private final ReviewService reviewService;
    private final SubscriptionService subscriptionService;
    private final UserFavoriteMovieService userFavoriteMovieService;

    private final ReviewRepository reviewRepository;
    private final DbRatingRepository dbRatingRepository;
    private final UserRepository userRepository;

    public List<WeeklyUserDTO> getWeeklyRatingUsers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = getLastMondayNineAM(now);
        return dbRatingRepository.findTopRaters(startDate, now).stream().limit(5).collect(Collectors.toList());
    }

    public List<WeeklyUserDTO> getWeeklyReviewUsers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = getLastMondayNineAM(now);
        return reviewRepository.findTopReviewers(startDate, now).stream().limit(5).collect(Collectors.toList());
    }

    private LocalDateTime getLastMondayNineAM(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(9)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    public LocalDateTime getNextMondayNineAM() {
        return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(9)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }



    public UserCommonDTO getUserCommonDTO(User user) {
        UserCommonDTO.UserCommonDTOBuilder userCommonDTo = UserCommonDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .picture(user.getPicture())
                .mbti(user.getMbti())
                .role(user.getRole());

        return userCommonDTo.build();
    }

    public SubscriptionDTO getUserSubscribersDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(user);

        List<SubscriptionInfo> subscriptionInfos = user.getSubscribers().stream()
                .map(sub -> SubscriptionInfo.builder()
                        .userCommonDTO(getUserCommonDTO(sub.getSubscriber()))
                        .subscriptionDate(sub.getSubscriptionDate())
                        .subscriptionCnt(sub.getSubscriber().getSubscriberCount())

                        .build())
                .collect(Collectors.toList());

        SubscriptionDTO.SubscriptionDTOBuilder subscriptionDTOBuilder = SubscriptionDTO.builder()
                .userCommonDTO(userCommonDTO)
                .subscriptionDTOs(subscriptionInfos);

        return subscriptionDTOBuilder.build();
    }

    public SubscriptionDTO getUserSubscriptionsDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(user);

        List<SubscriptionInfo> subscriptionInfos = user.getSubscriptions().stream()
                .map(sub -> SubscriptionInfo.builder()
                        .userCommonDTO(getUserCommonDTO(sub.getSubscribed()))
                        .subscriptionDate(sub.getSubscriptionDate())
                        .subscriptionCnt(sub.getSubscribed().getSubscriberCount())

                        .build())
                .collect(Collectors.toList());

        SubscriptionDTO.SubscriptionDTOBuilder subscriptionDTOBuilder = SubscriptionDTO.builder()
                .userCommonDTO(userCommonDTO)
                .subscriptionDTOs(subscriptionInfos);

        return subscriptionDTOBuilder.build();
    }

    public UserDTO getuserDTO(String userEmail) throws AccessDeniedException {
        User user = userService.getUserByEmail(userEmail);

//        다른 사용자가 다른 사용자의 info url 접속
//        if(!user.getEmail().equals(auth.getName())) {
//            throw new AccessDeniedException("You don't have permission to view this user");
//        }

        Set<Long> likedReviewIds = user.getHearts().stream()
                .map(heart -> heart.getReview().getId())
                .collect(Collectors.toSet());

        UserCommonDTO userCommonDTO = getUserCommonDTO(user);

        List<MovieCommonDTO> favoriteMovies = user.getUserFavoriteMovies().stream()
                .map(favorite -> MovieCommonDTO.builder()
                        .id(favorite.getDbMovie().getId())
                        .tId(favorite.getDbMovie().getMovieDetails().getTId())
                        .title(favorite.getDbMovie().getMovieDetails().getTitle())
                        .poster_path(favorite.getDbMovie().getMovieDetails().getPoster_path())
                        .release_date(favorite.getDbMovie().getMovieDetails().getRelease_date())
                        .runtime(favorite.getDbMovie().getMovieDetails().getRuntime())
                        .ajou_rating(favorite.getDbMovie().getDbRatingAvg())
                        .ajou_rating_cnt(favorite.getDbMovie().getDbRatingCount())
                        .build())
                .collect(Collectors.toList());

        List<DbRatingDTO> ratings = user.getDbRatings().stream()
                .map(dbRating -> DbRatingDTO.builder()
                        .id(dbRating.getId())
                        .movie(MovieCommonDTO.builder()
                                .id(dbRating.getDbMovies().getId())
                                .tId(dbRating.getDbMovies().getMovieDetails().getTId())
                                .title(dbRating.getDbMovies().getMovieDetails().getTitle())
                                .poster_path(dbRating.getDbMovies().getMovieDetails().getPoster_path())
                                .release_date(dbRating.getDbMovies().getMovieDetails().getRelease_date())
                                .runtime(dbRating.getDbMovies().getMovieDetails().getRuntime())
                                .ajou_rating(dbRating.getDbMovies().getDbRatingAvg())
                                .ajou_rating_cnt(dbRating.getDbMovies().getDbRatingCount())
                                .build())
                        .score(dbRating.getScore())
                        .ratingDate(dbRating.getUploadRating())
                        .build())
                .collect(Collectors.toList());

        Double ratingSum = ratings.stream()
                .map(DbRatingDTO::getScore)
                .reduce(0.0, Double::sum);

        Double ratingAvg = user.getRatingCount() > 0 ? ratingSum / user.getRatingCount() : 0.0;

        DecimalFormat df = new DecimalFormat("#.#");
        String formattedRatingAvg = df.format(ratingAvg);

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder()
                .id(user.getId())
                .favoriteCnt(user.getFavoriteCount())
                .reviewCnt(user.getReviewCount())
                .ratingCnt(user.getRatingCount())
                .ratingAvg(Double.valueOf(formattedRatingAvg))
                .heartCnt(user.getHeartCount())
                .likedReviewIds(likedReviewIds)
                .subscriptionCnt(user.getSubscriptionCount())
                .subscriberCnt(user.getSubscriberCount())
                .userCommonDTO(userCommonDTO)
                .favoriteMovies(favoriteMovies)
                .ratings(ratings);

        return userDTO.build();
    }


    public FavoriteMovieDTO getUserFavoriteMoviesDTO(String authEmail, String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(user);

        List<FavoriteMovieItem> favoriteMovies = user.getUserFavoriteMovies().stream()
                .map(favorite -> FavoriteMovieItem.builder()
                        .movieCommonDTO(MovieCommonDTO.builder()
                                .id(favorite.getDbMovie().getId())
                                .tId(favorite.getDbMovie().getMovieDetails().getTId())
                                .title(favorite.getDbMovie().getMovieDetails().getTitle())
                                .poster_path(favorite.getDbMovie().getMovieDetails().getPoster_path())
                                .release_date(favorite.getDbMovie().getMovieDetails().getRelease_date())
                                .runtime(favorite.getDbMovie().getMovieDetails().getRuntime())
                                .ajou_rating(favorite.getDbMovie().getDbRatingAvg())
                                .ajou_rating_cnt(favorite.getDbMovie().getDbRatingCount())
                                .build())
                        .favoriteDate(favorite.getFavoriteDate())
                        .isFavorite(userFavoriteMovieService.isFavorite(authEmail, favorite.getDbMovie().getId()))
                        .build())
                .collect(Collectors.toList());

        FavoriteMovieDTO.FavoriteMovieDTOBuilder favoriteMovieDTOBuilder = FavoriteMovieDTO.builder()
                .userCommonDTO(userCommonDTO)
                .favoriteMovies(favoriteMovies);

        return favoriteMovieDTOBuilder.build();
    }

    public RatingDTO getRatingsDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(user);

        List<DbRatingDTO> ratings = user.getDbRatings().stream()
                .map(rating -> DbRatingDTO.builder()
                        .id(rating.getId())
                        .movie(MovieCommonDTO.builder()
                                .id(rating.getDbMovies().getId())
                                .tId(rating.getDbMovies().getMovieDetails().getTId())
                                .title(rating.getDbMovies().getMovieDetails().getTitle())
                                .poster_path(rating.getDbMovies().getMovieDetails().getPoster_path())
                                .release_date(rating.getDbMovies().getMovieDetails().getRelease_date())
                                .runtime(rating.getDbMovies().getMovieDetails().getRuntime())
                                .ajou_rating(rating.getDbMovies().getDbRatingAvg())
                                .ajou_rating_cnt(rating.getDbMovies().getDbRatingCount())
                                .build())
                        .score(rating.getScore())
                        .ratingDate(rating.getUploadRating())
                        .build())
                .collect(Collectors.toList());

        RatingDTO.RatingDTOBuilder ratingDTOBuilder = RatingDTO.builder()
                .userCommonDTO(userCommonDTO)
                .ratings(ratings);

        return ratingDTOBuilder.build();
    }

    public Page<UserCommonDTO> getUsersByReviewLike(Long reviewId, Pageable pageable) {
        Review review = reviewService.getReviewById(reviewId);
        Page<User> likedUsers = userRepository.findByLikedReviews(review, pageable);

        return likedUsers.map(this::getUserCommonDTO);
    }

    public Page<UserSearchDTO> searchUsers(String query, int page, int size, String currentEmail) {
        Page<User> users = userRepository.searchByNickname(query, PageRequest.of(page-1, size));

        return users.map(user -> {
            boolean isSubscribed = subscriptionService.isSubscribed(currentEmail, user.getEmail());
            return new UserSearchDTO(getUserCommonDTO(user), isSubscribed);
        });
    }

    public List<String> getUserNicknameSuggestions(String query) {
        return userRepository.findTop10ByNicknameContainingIgnoreCase(query)
                .stream()
                .map(User::getNickname)
                .collect(Collectors.toList());
    }

}
