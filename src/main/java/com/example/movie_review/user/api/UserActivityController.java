package com.example.movie_review.user.api;

import com.example.movie_review.dbRating.DbRatings;
import com.example.movie_review.movieDetail.DTO.KeywordDTO;
import com.example.movie_review.movieDetail.DTO.PreferPerson;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserActivityService;
import com.example.movie_review.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserActivityController {

    private final Map<String, UserActivityService> activityServices;
    private final UserService userService;
    private final MovieCommonDTOService movieCommonDTOService;

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
     * 인생 영화 가져오기
     */
    @GetMapping("/{userEmail}/lifeMovies")
    public ResponseEntity<List<LifeMovie>> getLifeMovies(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        List<PreferredMovies> preferredMovies = user.getPreferredMovies();
        List<LifeMovie> lifeMovies = preferredMovies.stream()
                .map(movie -> new LifeMovie(movie.getMovieId(), movieCommonDTOService.getMoviePoster(Long.valueOf(movie.getMovieId()))))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lifeMovies);
    }

    /**
     * 사용자 평가 분포 가져오기
     */
    @GetMapping("/{userEmail}/ratings")
    public ResponseEntity<List<Double>> getRatings(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        List<Double> ratings = user.getDbRatings().stream()
                .map(DbRatings::getScore)
                .toList();
        return ResponseEntity.ok(ratings);
    }

    /**
     * 사용자 선호 키워드 가져오기
     */
    @GetMapping("/{userEmail}/keywords")
    public ResponseEntity<List<KeywordDTO>> getUserPreferKeywords(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        List<KeywordDTO> topKeywords = userService.getTopKeywords(user);
        return ResponseEntity.ok(topKeywords);
    }

    /**
     * 사용자 선호 감독 가져오기
     */
    @GetMapping("/{userEmail}/directors")
    public ResponseEntity<List<PreferPerson>> getUserPreferDirectors(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);

        List<PreferPerson> preferDirectors = userService.getPreferDirectors(user);

        return ResponseEntity.ok(preferDirectors);
    }

    /**
     * 사용자 선호 배우 가져오기
     */
    @GetMapping("/{userEmail}/actors")
    public ResponseEntity<List<PreferPerson>> getUserPreferActors(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);

        List<PreferPerson> preferActors = userService.getPreferActors(user);
        return ResponseEntity.ok(preferActors);
    }
}

@Data
@AllArgsConstructor
class DeleteAccountResponse {
    private Long userId;
    private String userNickname;

}

@Data
@AllArgsConstructor
class LifeMovie {
    private String tid;
    private String poster_path;
}