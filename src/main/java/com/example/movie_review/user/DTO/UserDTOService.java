package com.example.movie_review.user.DTO;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingDTO;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDTOService {
    private final UserService userService;

    public UserCommonDTO getUserCommonDTO(String email) {
        User user = userService.getUserByEmail(email);

        UserCommonDTO.UserCommonDTOBuilder userCommonDTo = UserCommonDTO.builder()
                .id(user.getId())
                .email(email)
                .nickname(user.getNickname())
                .picture(user.getPicture());

        return userCommonDTo.build();
    }

    public SubscriptionDTO getUserSubscribersDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(email);

        List<UserCommonDTO> subscribers = user.getSubscribers().stream()
                .map(sub -> UserCommonDTO.builder()
                        .id(sub.getSubscriber().getId())
                        .email(sub.getSubscriber().getEmail())
                        .nickname(sub.getSubscriber().getNickname())
                        .picture(sub.getSubscriber().getPicture())
                        .build())
                .collect(Collectors.toList());

        SubscriptionDTO.SubscriptionDTOBuilder subscriptionDTOBuilder = SubscriptionDTO.builder()
                .userCommonDTO(userCommonDTO)
                .subscriptionDTOs(subscribers);

        return subscriptionDTOBuilder.build();
    }

    public SubscriptionDTO getUserSubscriptionsDTO(String email) {
        User user = userService.getUserByEmail(email);
        UserCommonDTO userCommonDTO = getUserCommonDTO(email);

        List<UserCommonDTO> subscriptions = user.getSubscriptions().stream()
                .map(sub -> UserCommonDTO.builder()
                        .id(sub.getSubscribed().getId())
                        .email(sub.getSubscribed().getEmail())
                        .nickname(sub.getSubscribed().getNickname())
                        .picture(sub.getSubscribed().getPicture())
                        .build())
                .collect(Collectors.toList());

        SubscriptionDTO.SubscriptionDTOBuilder subscriptionDTOBuilder = SubscriptionDTO.builder()
                .userCommonDTO(userCommonDTO)
                .subscriptionDTOs(subscriptions);

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

        UserCommonDTO userCommonDTO = UserCommonDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .picture(user.getPicture())
                .build();

        List<MovieCommonDTO> favoriteMovies = user.getUserFavoriteMovies().stream()
                .map(favorite -> MovieCommonDTO.builder()
                        .id(favorite.getDbMovie().getId())
                        .tId(favorite.getDbMovie().getMovieDetails().getTId())
                        .title(favorite.getDbMovie().getMovieDetails().getTitle())
                        .poster_path(favorite.getDbMovie().getMovieDetails().getPoster_path())
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
                                .build())
                        .score(dbRating.getScore())
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

        List<MovieCommonDTO> favoriteMovies = user.getUserFavoriteMovies().stream()
                .map(favorite -> MovieCommonDTO.builder()
                        .id(favorite.getDbMovie().getId())
                        .tId(favorite.getDbMovie().getMovieDetails().getTId())
                        .title(favorite.getDbMovie().getMovieDetails().getTitle())
                        .poster_path(favorite.getDbMovie().getMovieDetails().getPoster_path())
                        .build())
                .collect(Collectors.toList());
        System.out.println("favoriteMovies.get(0).getTId() = " + favoriteMovies.get(0).getTId());

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
                                .build())
                        .score(rating.getScore())
                        .build())
                .collect(Collectors.toList());

        RatingDTO.RatingDTOBuilder ratingDTOBuilder = RatingDTO.builder()
                .userCommonDTO(userCommonDTO)
                .ratings(ratings);

        return ratingDTOBuilder.build();
    }
}