package com.example.movie_review.user.service;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingDTO;
import com.example.movie_review.dbRating.DbRatingRepository;
import com.example.movie_review.review.repository.ReviewRepository;
import com.example.movie_review.user.DTO.*;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDTOService {
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final DbRatingRepository dbRatingRepository;

    @Cacheable(value = "weeklyRatingUsers", key = "'weeklyRatingUsers'")
    public List<WeeklyUserDTO> getWeeklyRatingUsers() {
        LocalDateTime startDate = LocalDateTime.now().minusWeeks(1);
        LocalDateTime endDate = LocalDateTime.now();
        return dbRatingRepository.findTopRaters(startDate, endDate);
    }

    @Cacheable(value = "weeklyReviewUsers", key = "'weeklyReviewUsers'")
    public List<WeeklyUserDTO> getWeeklyReviewUsers() {
        LocalDateTime startDate = LocalDateTime.now().minusWeeks(1);
        LocalDateTime endDate = LocalDateTime.now();
        return reviewRepository.findTopReviewers(startDate, endDate);
    }

    public UserCommonDTO getUserCommonDTO(String email) {
        User user = userService.getUserByEmail(email);

        UserCommonDTO.UserCommonDTOBuilder userCommonDTo = UserCommonDTO.builder()
                .id(user.getId())
                .email(email)
                .nickname(user.getNickname())
                .picture(user.getPicture())
                .role(user.getRole());

        return userCommonDTo.build();
    }

    public SubscriptionDTO getUserSubscribersDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(email);

        List<SubscriptionInfo> subscriptionInfos = user.getSubscribers().stream()
                .map(sub -> SubscriptionInfo.builder()
                        .userCommonDTO(UserCommonDTO.builder()
                                .id(sub.getSubscriber().getId())
                                .email(sub.getSubscriber().getEmail())
                                .nickname(sub.getSubscriber().getNickname())
                                .picture(sub.getSubscriber().getPicture())
                                .build())
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
        UserCommonDTO userCommonDTO = getUserCommonDTO(email);

        List<SubscriptionInfo> subscriptionInfos = user.getSubscriptions().stream()
                .map(sub -> SubscriptionInfo.builder()
                        .userCommonDTO(UserCommonDTO.builder()
                                .id(sub.getSubscribed().getId())
                                .email(sub.getSubscribed().getEmail())
                                .nickname(sub.getSubscribed().getNickname())
                                .picture(sub.getSubscribed().getPicture())
                                .build())
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

        UserCommonDTO userCommonDTO = getUserCommonDTO(userEmail);

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

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder()
                .id(user.getId())
//                .user(user)
                .favoriteCnt(user.getFavoriteCount())
                .reviewCnt(user.getReviewCount())
                .ratingCnt(user.getRatingCount())
                .heartCnt(user.getHeartCount())
                .likedReviewIds(likedReviewIds)
                .subscriptionCnt(user.getSubscriptionCount())
                .subscriberCnt(user.getSubscriberCount())
                .userCommonDTO(userCommonDTO)
                .favoriteMovies(favoriteMovies)
                .ratings(ratings);

        return userDTO.build();
    }


    public FavoriteMovieDTO getUserFavoriteMoviesDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(email);

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
                        .build())
                .collect(Collectors.toList());

        FavoriteMovieDTO.FavoriteMovieDTOBuilder favoriteMovieDTOBuilder = FavoriteMovieDTO.builder()
                .userCommonDTO(userCommonDTO)
                .favoriteMovies(favoriteMovies);

        return favoriteMovieDTOBuilder.build();
    }

    public RatingDTO getRatingsDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(email);

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
}
