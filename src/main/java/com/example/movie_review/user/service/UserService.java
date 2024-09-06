package com.example.movie_review.user.service;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbRating.DbRatingRepository;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.genre.Genres;
import com.example.movie_review.movieDetail.DTO.KeywordDTO;
import com.example.movie_review.movieDetail.DTO.PreferPerson;
import com.example.movie_review.movieDetail.domain.Cast;
import com.example.movie_review.movieDetail.domain.Crew;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import com.example.movie_review.review.event.ReviewEvent;
import com.example.movie_review.user.DTO.UserMovieInfoDTO;
import com.example.movie_review.user.UserRole;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MovieDetailRepository movieDetailRepository;

    private final DbRatingRepository dbRatingRepository;

    /**
     * nickname 중복 체크
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * userId 입력받아 user return
     */
    public User getLoginUserById(Long userId) {
        if(userId == null) return null;

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    public Optional<User> getOptUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User Email"));
    }

    public Long getUserCount() {
        return userRepository.getAllUserCount();
    }

    @Transactional
    public void updateUserRole(User user) {
        UserRole newRole;
        if(user.getReviews().size() >= 100 && user.getDbRatings().size() >= 150 && user.getHearts().size() >= 100) {
            newRole = UserRole.CHALLENGER;
        }
        else if(user.getReviews().size() >= 40 && user.getDbRatings().size() >= 60 && user.getHearts().size() >= 40) {
            newRole = UserRole.MASTER;
        }
        else if(user.getReviews().size() >= 20 && user.getDbRatings().size() >= 30 && user.getHearts().size() >= 20) {
            newRole = UserRole.EMERALD;
        }
        else if(user.getReviews().size() >= 10 && user.getDbRatings().size() >= 15) {
            newRole = UserRole.SILVER;
        } else {
            newRole = UserRole.BRONZE;
        }

        if (user.getRole() != newRole) {
            user.updateRole(newRole);
            userRepository.save(user);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateUserRoles() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            updateUserRole(user);
        }
    }

    @Transactional
    /**
     * 모든 연관 엔티티에 대해 명시적으로 관계를 해제
     * 양방향 관계에 대해 양쪽 모두에서 관계를 제거
     */
    public void deleteUser(User user, HttpServletRequest request, HttpServletResponse response) {
        // PreferredMovies와 PreferredGenres 처리
        user.getPreferredMovies().clear();
        user.getPreferredGenres().clear();

        // DbRatings 처리
        user.getDbRatings().forEach(rating -> rating.setUser(null));
        user.getDbRatings().clear();

        // Subscription 처리 (양방향)
        user.getSubscriptions().forEach(subscription -> subscription.getSubscribed().getSubscribers().remove(subscription));
        user.getSubscriptions().clear();
        user.getSubscribers().forEach(subscription -> subscription.getSubscriber().getSubscriptions().remove(subscription));
        user.getSubscribers().clear();

        // Review 처리
        user.getReviews().forEach(review -> {
            review.getHearts().clear();
            review.getComments().clear();
            review.setUser(null);
        });
        user.getReviews().clear();

        // Heart 처리
        user.getHearts().forEach(heart -> {
            if (heart.getReview() != null) {
                heart.getReview().getHearts().remove(heart);
            }
            heart.setUser(null);
        });
        user.getHearts().clear();

        // UserFavoriteMovie 처리
        user.getUserFavoriteMovies().forEach(favorite -> favorite.setUser(null));
        user.getUserFavoriteMovies().clear();

        // Feedback 처리
        user.getUserFeedbacks().forEach(feedback -> feedback.setUser(null));
        user.getUserFeedbacks().clear();

        eventPublisher.publishEvent(new ReviewEvent(this, null, ReviewEvent.ReviewEventType.ACCOUNT));
        userRepository.delete(user);

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // JWT 토큰 삭제
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        // JSESSIONID 삭제
        Cookie jsessionidCookie = new Cookie("JSESSIONID", null);
        jsessionidCookie.setMaxAge(0);
        jsessionidCookie.setPath("/");
        response.addCookie(jsessionidCookie);
    }

    // 사용자가 좋게 평가한 영화들 중에서 키워드 뽑아내는 걸로
    public List<KeywordDTO> getTopKeywords(User user) {
        List<MovieDetails> movieDetails = getPreferMovies(user, 0.0);

        Map<String, Long> keywordCount = movieDetails.stream()
                .flatMap(movie -> movie.getMovieKeywords().stream())
                .map(keyword -> keyword.getKeyword().getName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<KeywordDTO> topKeywords = keywordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> new KeywordDTO(entry.getKey(), entry.getValue().intValue(), 0))
                .collect(Collectors.toList());

        if (!topKeywords.isEmpty()) {
            long maxCount = topKeywords.get(0).getCount();
            long minCount = topKeywords.get(topKeywords.size() - 1).getCount();

            for (KeywordDTO dto : topKeywords) {
                dto.setSize(calculateSize(dto.getCount(), minCount, maxCount));
            }
        }

        return topKeywords;
    }

    /**
     * 사용자 선호 영화에 대한 선호 감독
     */
    public List<PreferPerson> getPreferDirectors(User user) {
        List<MovieDetails> preferMovies = getPreferMovies(user, 0.0);
        Map<String, Crew> directorMap = new HashMap<>(); // 감독 이름, crew 정보 맵
        Map<String ,Integer> directorFrequency = new HashMap<>(); // 감독 이름, count

        preferMovies.forEach(movie -> {
            movie.getCredits().getCrew().stream()
                    .forEach(director -> {
                        String directorName = director.getName();
                        directorMap.put(directorName, director);
                        directorFrequency.merge(directorName, 1, Integer::sum);
                    });
        });

        return directorFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Crew director = directorMap.get(entry.getKey());
                    return new PreferPerson(
                            director.getId(),
                            director.getProfile_path(),
                            director.getOriginal_name(),
                            director.getName(),
                            entry.getValue()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * 사용자 선호 영화에 대한 선호 배우
     */
    public List<PreferPerson> getPreferActors(User user) {
        List<MovieDetails> preferMovies = getPreferMovies(user, 0.0);
        Map<String, Integer> actorFrequency = new HashMap<>();
        Map<String, Cast> actorMap = new HashMap<>();

        preferMovies.forEach(movie -> {
            movie.getCredits().getCast().stream()
                    .forEach(actor -> {
                        String actorName = actor.getName();
                        actorMap.put(actorName, actor);
                        actorFrequency.merge(actorName, 1, Integer::sum);
                    });
        });

        return actorFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    Cast actor = actorMap.get(entry.getKey());
                    return new PreferPerson(
                            actor.getId(),
                            actor.getProfile_path(),
                            actor.getOriginal_name(),
                            actor.getName(),
                            entry.getValue()
                    );
                })
                .collect(Collectors.toList());
    }

    public List<KeywordDTO> getPreferGenre(User user) {
        List<MovieDetails> preferMovies = getPreferMovies(user, 0.0);

        Map<String, Long> genreCount = preferMovies.stream()
                .flatMap(movie -> movie.getGenres().stream())
                .map(Genres::getName)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<KeywordDTO> topGenres = genreCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> new KeywordDTO(entry.getKey(), entry.getValue().intValue(), 0))
                .collect(Collectors.toList());

        if (!topGenres.isEmpty()) {
            long maxCount = topGenres.get(0).getCount();
            long minCount = topGenres.get(topGenres.size() - 1).getCount();

            for (KeywordDTO dto : topGenres) {
                dto.setSize(calculateSize(dto.getCount(), minCount, maxCount));
            }
        }

        return topGenres;

    }

    /**
     * 현재 사용자, 지금 보고 있는 사용자의 평가 높은 영화들 중 서로 공통적인 영화 뽑아내기
     */
    public List<MovieDetails> getCommonPreferMovies(User user, User currentUser) {
        List<MovieDetails> preferMovies = getPreferMovies(user, 4.0);
        List<MovieDetails> currentPreferMovies = getPreferMovies(currentUser, 4.0);

        return preferMovies.stream()
                .filter(currentPreferMovies::contains)
                .distinct()
                .limit(15)
                .collect(Collectors.toList());
    }

    /**
     * 현재 사용자, 지금 보고 있는 사용자의 상반된 선호 영화 뽑아내기
     */
    public List<MovieDetails> getOppositeMovies(User user, User currentUser) {
        // 1. 보고 있는 사용자는 높은 평점, 현재 나는 낮은 평점
        List<MovieDetails> preferMovies = getPreferMovies(user, 4.0);
        List<MovieDetails> curOppositeMovies = getOppositeMovies(currentUser, 2.5);
        List<MovieDetails> movie1 = preferMovies.stream()
                .filter(curOppositeMovies::contains)
                .distinct()
                .collect(Collectors.toList());

        // 2. 보고 있는 사용자는 낮은 평점, 현재 나는 높은 평점
        List<MovieDetails> oppositeMovies = getOppositeMovies(user, 2.5);
        List<MovieDetails> curPreferMovies = getPreferMovies(currentUser, 4.0);
        List<MovieDetails> movie2 = oppositeMovies.stream()
                .filter(curPreferMovies::contains)
                .distinct()
                .collect(Collectors.toList());

        movie1.addAll(movie2);


        return movie1.stream().distinct().collect(Collectors.toList());

    }



    private int calculateSize(long count, long minCount, long maxCount) {
        if (minCount == maxCount) return 3;
        return 1 + (int) ((count - minCount) * 4 / (maxCount - minCount));
    }

    private List<MovieDetails> getPreferMovies(User user, Double filter) {
        List<DbMovies> preferDbMovies = getPreferDbMovies(user, filter);
        return getMovieDetails(preferDbMovies);
    }

    private List<MovieDetails> getOppositeMovies(User user, Double filter) {
        List<DbMovies> preferDbMovies = getOppositeDbMovies(user, filter);
        return getMovieDetails(preferDbMovies);
    }

    private List<MovieDetails> getMovieDetails(List<DbMovies> movies) {
        return movies.stream()
                .map(movie -> movieDetailRepository.findBytId(movie.getTmdbId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<DbMovies> getPreferDbMovies(User user, Double filter) {
        return user.getDbRatings().stream()
                .filter(r -> r.getScore() >= filter)
                .map(DbRatings::getDbMovies)
                .collect(Collectors.toList());
    }

    private List<DbMovies> getOppositeDbMovies(User user, Double filter) {
        return user.getDbRatings().stream()
                .filter(r -> r.getScore() <= filter)
                .map(DbRatings::getDbMovies)
                .collect(Collectors.toList());
    }

    public UserMovieInfoDTO getUserMovieInfo(User user) {
        return UserMovieInfoDTO.builder()
                .mbti(user.getMbti())
                .department(user.getDepartment())
                .build();
    }
}
