package com.example.movie_review.user.api;

import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.feedback.FeedbackRepository;
import com.example.movie_review.feedback.FeedbackService;
import com.example.movie_review.movieDetail.DTO.KeywordDTO;
import com.example.movie_review.movieDetail.DTO.PreferPerson;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserActivityService;
import com.example.movie_review.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserActivityController {

    private final Map<String, UserActivityService> activityServices;
    private final UserService userService;
    private final MovieCommonDTOService movieCommonDTOService;
    private final FeedbackService feedbackService;

    /**
     * 해당 사용자의 각 카테고리별 활동 내역 리스트 보여주기
     */
    @GetMapping("/{userEmail}/activities")
    public ResponseEntity<?> getUserActivities(
            @PathVariable String userEmail,
            @RequestParam String category,
            @RequestParam(defaultValue = "createdAt_desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Authentication auth) {

        UserActivityService service = activityServices.get(category);
        if(service == null) {
            return ResponseEntity.badRequest().body("Invalid category");
        }

        UserActivityDTO activities = service.getUserActivity(auth.getName(), userEmail, sort, page, size);

        Map<String, Object> response = activities.toMap();
        response.put("category", category);
        response.put("hasMore", activities.getActivityItems().size() == size);

        return ResponseEntity.ok(response);
    }

    /**
     * 각 카테고리별 sort-option 리스트 보여주기
     */
    @GetMapping("/{userEmail}/sort-options")
    public ResponseEntity<Map<String , List<SortOption>>> getSortOptions(@PathVariable String userEmail) {
        Map<String, List<SortOption>> sortOptions = new HashMap<>();
        for (Map.Entry<String, UserActivityService> entry : activityServices.entrySet()) {
            sortOptions.put(entry.getKey(), entry.getValue().getSortOptions());
        }

        return ResponseEntity.ok(sortOptions);
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/{currentUserEmail}/deleteAccount")
    public ResponseEntity<?> deleteAccount(@PathVariable String currentUserEmail, Authentication auth,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        try {
            User userByEmail = userService.getUserByEmail(currentUserEmail);
            userService.deleteUser(userByEmail, request, response);
            return ResponseEntity.ok(new DeleteAccountResponse(userByEmail.getId(), userByEmail.getNickname()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 사용자 선호 활동 내역 선택적 반환
     * Map Object로 처리하는게 맞을까??
     */
    @GetMapping("/{userEmail}/preferences")
    public ResponseEntity<Map<String ,Object>> getUserPreference(
            @PathVariable String userEmail,
            @RequestParam(required = false) List<String> fields) {
        User user = userService.getUserByEmail(userEmail);
        Map<String, Object> preferences = new HashMap<>();

        if (fields == null || fields.isEmpty()) {
            fields = Arrays.asList("lifeMovies", "ratings", "keywords", "directors", "actors", "genres");
        }

        if (fields.contains("lifeMovies")) {
            preferences.put("lifeMovies", getLifeMovies(user));
        }
        if (fields.contains("ratings")) {
            preferences.put("ratings", getRatings(user));
        }
        if (fields.contains("keywords")) {
            preferences.put("keywords", getTopKeywords(user));
        }
        if (fields.contains("directors")) {
            preferences.put("directors", getPreferDirectors(user));
        }
        if (fields.contains("actors")) {
            preferences.put("actors", getPreferActors(user));
        }
        if (fields.contains("genres")) {
            preferences.put("genres", getPreferGenre(user));
        }

        return ResponseEntity.ok(preferences);
    }

    /**
     * 두 사용자의 유사한 선호 영화 가져오기
     */
    @GetMapping("/{userEmail}/similarMovies")
    public ResponseEntity<List<LifeMovie>> getCommonPreferMovies(@PathVariable String userEmail, @RequestParam String currentUserEmail) {
        return ResponseEntity.ok(getMoviesBasedOnPreference(userEmail, currentUserEmail, userService::getCommonPreferMovies));
    }

    /**
     * 두 사용자의 상반된 선호 영화 가져오기
     */
    @GetMapping("/{userEmail}/disSimilarMovies")
    public ResponseEntity<List<LifeMovie>> getOppositePreferMovies(@PathVariable String userEmail, @RequestParam String currentUserEmail) {
        return ResponseEntity.ok(getMoviesBasedOnPreference(userEmail, currentUserEmail, userService::getOppositeMovies));
    }

    /**
     * 사용자 피드백 작성
     */
    @PostMapping("/{userEmail}/feedback")
    public ResponseEntity<?> saveFeedback(@PathVariable String userEmail, @RequestBody FeedbackRequest feedbackRequest) {
        try {
            User user = userService.getUserByEmail(userEmail);
            feedbackService.save(user, feedbackRequest.getFeedback());
            return ResponseEntity.ok().body("피드백이 성공적으로 저장되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("피드백 저장 중 오류가 발생했습니다.");
        }
    }


    private List<LifeMovie> getLifeMovies(User user) {
        List<PreferredMovies> preferredMovies = user.getPreferredMovies();
        List<LifeMovie> lifeMovies = preferredMovies.stream()
                .map(movie -> new LifeMovie(movie.getMovieId(), movieCommonDTOService.getMoviePoster(Long.valueOf(movie.getMovieId()))))
                .collect(Collectors.toList());
        return lifeMovies;
    }

    private List<Double> getRatings(User user) {
        return user.getDbRatings().stream()
            .map(DbRatings::getScore)
            .toList();
    }

    private List<KeywordDTO> getTopKeywords(User user) {
        return userService.getTopKeywords(user);
    }

    private List<PreferPerson> getPreferDirectors(User user) {
        return userService.getPreferDirectors(user);
    }

    private List<PreferPerson> getPreferActors(User user) {
        return userService.getPreferActors(user);
    }

    private List<KeywordDTO> getPreferGenre(User user) {
        return userService.getPreferGenre(user);
    }

    private List<LifeMovie> getMoviesBasedOnPreference(String userEmail, String currentUserEmail,
                                                       BiFunction<User, User, List<MovieDetails>> preferenceFunction) {
        User user = userService.getUserByEmail(userEmail);
        User currentUser = userService.getUserByEmail(currentUserEmail);

        List<MovieDetails> preferMovies = preferenceFunction.apply(user, currentUser);
        return preferMovies.stream()
                .map(movie -> new LifeMovie(movie.getTId(), movie.getPoster_path()))
                .collect(Collectors.toList());
    }

//    /**
//     * 인생 영화 가져오기
//     */
//    @GetMapping("/{userEmail}/lifeMovies")
//    public ResponseEntity<List<LifeMovie>> getLifeMovies(@PathVariable String userEmail) {
//        User user = userService.getUserByEmail(userEmail);
//        List<PreferredMovies> preferredMovies = user.getPreferredMovies();
//        List<LifeMovie> lifeMovies = preferredMovies.stream()
//                .map(movie -> new LifeMovie(movie.getMovieId(), movieCommonDTOService.getMoviePoster(Long.valueOf(movie.getMovieId()))))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(lifeMovies);
//    }
//
//    /**
//     * 사용자 평가 분포 가져오기
//     */
//    @GetMapping("/{userEmail}/ratings")
//    public ResponseEntity<List<Double>> getRatings(@PathVariable String userEmail) {
//        User user = userService.getUserByEmail(userEmail);
//        List<Double> ratings = user.getDbRatings().stream()
//                .map(DbRatings::getScore)
//                .toList();
//        return ResponseEntity.ok(ratings);
//    }
//
//    /**
//     * 사용자 선호 키워드 가져오기
//     */
//    @GetMapping("/{userEmail}/keywords")
//    public ResponseEntity<List<KeywordDTO>> getUserPreferKeywords(@PathVariable String userEmail) {
//        User user = userService.getUserByEmail(userEmail);
//        List<KeywordDTO> topKeywords = userService.getTopKeywords(user);
//        return ResponseEntity.ok(topKeywords);
//    }
//
//    /**
//     * 사용자 선호 감독 가져오기
//     */
//    @GetMapping("/{userEmail}/directors")
//    public ResponseEntity<List<PreferPerson>> getUserPreferDirectors(@PathVariable String userEmail) {
//        User user = userService.getUserByEmail(userEmail);
//
//        List<PreferPerson> preferDirectors = userService.getPreferDirectors(user);
//
//        return ResponseEntity.ok(preferDirectors);
//    }
//
//    /**
//     * 사용자 선호 배우 가져오기
//     */
//    @GetMapping("/{userEmail}/actors")
//    public ResponseEntity<List<PreferPerson>> getUserPreferActors(@PathVariable String userEmail) {
//        User user = userService.getUserByEmail(userEmail);
//
//        List<PreferPerson> preferActors = userService.getPreferActors(user);
//        return ResponseEntity.ok(preferActors);
//    }
//
//    /**
//     * 사용자 선호 장르 가져오기
//     */
//    @GetMapping("/{userEmail}/genres")
//    public ResponseEntity<List<KeywordDTO>> getUserPreferGenres(@PathVariable String userEmail) {
//        User user = userService.getUserByEmail(userEmail);
//
//        List<KeywordDTO> preferGenres = userService.getPreferGenre(user);
//        return ResponseEntity.ok(preferGenres);
//    }
//



}

@Data
@AllArgsConstructor
class DeleteAccountResponse {
    private Long userId;
    private String userNickname;

}

@Data
class LifeMovie {
    private String tid;
    private String poster_path;

    public LifeMovie(Integer tid, String poster_path) {
        this.tid = String.valueOf(tid);
        this.poster_path = poster_path;
    }

    public LifeMovie(String tid, String poster_path) {
        this.tid = tid;
        this.poster_path = poster_path;
    }
}

@Getter @Setter
class FeedbackRequest {
    private String feedback;

    public FeedbackRequest() {}
    public FeedbackRequest(String feedback) {
        this.feedback = feedback;
    }
}