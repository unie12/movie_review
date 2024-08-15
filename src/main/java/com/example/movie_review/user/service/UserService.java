package com.example.movie_review.user.service;

import com.example.movie_review.dbMovie.DbMovies;
import com.example.movie_review.dbMovie.service.DbMovieService;
import com.example.movie_review.dbRating.DbRatingRepository;
import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.movieDetail.DTO.KeywordDTO;
import com.example.movie_review.movieDetail.domain.Keyword;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.domain.MovieKeyword;
import com.example.movie_review.movieDetail.repository.MovieDetailRepository;
import com.example.movie_review.movieDetail.service.MovieDetailDTOService;
import com.example.movie_review.review.event.ReviewEvent;
import com.example.movie_review.user.repository.UserRepository;
import com.example.movie_review.user.UserRole;
import com.example.movie_review.user.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    public void deleteUser(User user, HttpServletRequest request, HttpServletResponse response) {
        // 연관된 데이터 처리
        user.getReviews().clear();
        user.getHearts().clear();
        user.getDbRatings().clear();
        // ... 다른 연관 데이터들도 비슷하게 처리 ...

        // 구독 관계 처리 (양방향이므로 주의 필요)
        user.getSubscriptions().clear();
        user.getSubscribers().clear();

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
        List<DbMovies> preferMovies = user.getDbRatings().stream()
                .filter(r -> r.getScore() >= 3.5)
                .map(DbRatings::getDbMovies)
                .collect(Collectors.toList());

        List<MovieDetails> movieDetails = preferMovies.stream()
                .map(movie -> movieDetailRepository.findBytId(movie.getTmdbId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

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

    private int calculateSize(long count, long minCount, long maxCount) {
        if (minCount == maxCount) return 3;
        return 1 + (int) ((count - minCount) * 4 / (maxCount - minCount));
    }
}
