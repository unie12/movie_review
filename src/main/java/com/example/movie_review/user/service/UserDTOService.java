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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.text.DecimalFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDTOService {
    private final UserService userService;
    private final ReviewService reviewService;
    private final SubscriptionService subscriptionService;
    private final UserFavoriteMovieService userFavoriteMovieService;

    private final ReviewRepository reviewRepository;
    private final DbRatingRepository dbRatingRepository;
    private final UserRepository userRepository;

    private static final ZoneId SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");
    private static final ZoneOffset UTC_OFFSET = ZoneOffset.UTC;

    public List<WeeklyUserDTO> getWeeklyRatingUsers() {
        LocalDateTime now = LocalDateTime.now(UTC_OFFSET);
        LocalDateTime startDate = getCurrentWeekStartDate(now);

        log.info("Fetching weekly rating users from {} to {} (UTC)", startDate, now);

        List<WeeklyUserDTO> result = dbRatingRepository.findTopRaters(startDate, now);

        log.info("Found {} weekly rating users", result.size());

        return result.stream().limit(5).collect(Collectors.toList());
    }

    public List<WeeklyUserDTO> getWeeklyReviewUsers() {
        LocalDateTime now = LocalDateTime.now(UTC_OFFSET);
        LocalDateTime startDate = getCurrentWeekStartDate(now);

        log.info("Fetching weekly rating users from {} to {} (UTC)", startDate, now);

        List<WeeklyUserDTO> result = reviewRepository.findTopReviewers(startDate, now);

        log.info("Found {} weekly rating users", result.size());

        return result.stream().limit(5).collect(Collectors.toList());
    }

    private LocalDateTime getCurrentWeekStartDate(LocalDateTime dateTime) {
        // UTC 기준 월요일 00:00:00로 설정
        LocalDateTime currentMondayUtc = dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // UTC 기준 월요일 00:00을 KST로 변환하면 월요일 09:00
        ZonedDateTime currentMondayKst = currentMondayUtc.atZone(UTC_OFFSET).withZoneSameInstant(SEOUL_ZONE_ID);

        // 현재 시간이 월요일 09:00 (KST) 이후라면 현재 주의 월요일을,
        // 그렇지 않다면 지난 주의 월요일을 반환
        if (dateTime.atZone(UTC_OFFSET).withZoneSameInstant(SEOUL_ZONE_ID).isAfter(currentMondayKst)) {
            return currentMondayUtc;
        } else {
            return currentMondayUtc.minusWeeks(1);
        }
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

    public Page<UserSearchDTO> getUsersByReviewLike(Long reviewId, Pageable pageable, Authentication authentication) {
        User curUser = userService.getUserByEmail(authentication.getName());

        Review review = reviewService.getReviewById(reviewId);
        Page<User> likedUsers = userRepository.findByLikedReviews(review, pageable);

        return likedUsers.map(user -> {
            UserCommonDTO dto = getUserCommonDTO(user);
            boolean isSubscribed = subscriptionService.isSubscribed(curUser.getEmail(), user.getEmail());
            int ratingCnt = user.getRatingCount();
            int reviewCnt = user.getReviewCount();
            return new UserSearchDTO(dto, isSubscribed, ratingCnt, reviewCnt);
        });
    }

    public Page<UserSearchDTO> searchUsers(String query, int page, int size, String currentEmail) {
        Page<User> users = userRepository.searchByNickname(query, PageRequest.of(page-1, size));

        return users.map(user -> {
            boolean isSubscribed = subscriptionService.isSubscribed(currentEmail, user.getEmail());
            int ratingCnt = user.getRatingCount();
            int reviewCnt = user.getReviewCount();
            return new UserSearchDTO(getUserCommonDTO(user), isSubscribed, ratingCnt, reviewCnt);
        });
    }

    public List<String> getUserNicknameSuggestions(String query) {
        return userRepository.findTop10ByNicknameContainingIgnoreCase(query)
                .stream()
                .map(User::getNickname)
                .collect(Collectors.toList());
    }

}
