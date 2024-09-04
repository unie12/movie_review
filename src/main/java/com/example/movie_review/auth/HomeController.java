package com.example.movie_review.auth;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.movieDetail.DTO.MovieSearchDTO;
import com.example.movie_review.movieDetail.service.MovieCommonDTOService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserSearchDTO;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.RealTimeDataService;
import com.example.movie_review.user.service.UserDTOService;
import com.example.movie_review.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    private final TmdbService tmdbService;
    private final UserDTOService userDTOService;
    private final MovieCommonDTOService movieCommonDTOService;
    private final RealTimeDataService realTimeDataService;


    /**
     * 아주대 인기 영화 리스트...
     */
    @GetMapping("/home/ajouPopularMovies")
    public ResponseEntity<List<MoviePopularityDTO>> getAjouPopularMovies() {
        List<MoviePopularityDTO> movies = movieCommonDTOService.getAjouPopularMovies();
        Collections.shuffle(movies);
        return ResponseEntity.ok(movies.stream().limit(20).collect(Collectors.toList()));
    }

    /**
     * 현재 사용자와 동일한 mbti를 가진 다른 사용자의 선호 영화
     */
    @GetMapping("/home/mbtiMovies")
    public ResponseEntity<List<MoviePopularityDTO>> getSameMbtiMovies(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<MoviePopularityDTO> movies = movieCommonDTOService.getSameMbtiMovies(user);
        Collections.shuffle(movies);
        return ResponseEntity.ok(movies.stream().limit(20).collect(Collectors.toList()));
    }

    /**
     * 현재 사용자와 동일한 학과인 다른 사용자의 선호 영화
     */
    @GetMapping("/home/departmentMovies")
    public ResponseEntity<List<MoviePopularityDTO>> getSameDepartmentMovies(Authentication auth) {
        User user = userService.getUserByEmail(auth.getName());
        List<MoviePopularityDTO> movies = movieCommonDTOService.getSameDepartmentMovies(user);
        Collections.shuffle(movies);
        return ResponseEntity.ok(movies.stream().limit(20).collect(Collectors.toList()));
    }

    /**
     * 실시간 데이터 보내주기 
     */
    @GetMapping("/home/realtime-data")
    public ResponseEntity<RealTimeData> getRealTimeData() {
        RealTimeData data = realTimeDataService.getRealTimeData();

        return ResponseEntity.ok(data);
    }

    /**
     * tmdb 영화 검색
     */
    @GetMapping("/search/movies")
    public ResponseEntity<Page<MovieSearchDTO>> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page) throws JsonProcessingException {
        Page<MovieSearchDTO> results = tmdbService.searchMovies(query, page);
        return ResponseEntity.ok(results);
    }

    /**
     * db에서 해당 query가 들어간 사용자 찾기
     */
    @GetMapping("/search/users")
    public ResponseEntity<Page<UserSearchDTO>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) throws JsonProcessingException {
        String currentEmail = auth != null ? auth.getName() : null;
        Page<UserSearchDTO> results = userDTOService.searchUsers(query, page, size, currentEmail);
        return ResponseEntity.ok(results);
    }

    /**
     * 디바운싱 적용 - db에 있는 관련 영화, 사용자 보여주기
     */
    @GetMapping("/search/suggestions")
    @ResponseBody
    public ResponseEntity<List<String>> getSuggestions(
            @RequestParam String query,
            @RequestParam String searchType) {
        List<String> suggestions;

        if (searchType.equals("movieTitle")) {
            suggestions = movieCommonDTOService.getMovieTitleSuggestions(query);
        } else if (searchType.equals("userNickname")) {
            suggestions = userDTOService.getUserNicknameSuggestions(query);
        } else {
            return ResponseEntity.badRequest().build();
        }
        // 일단 10개로 제한은 해놓을게
        return ResponseEntity.ok(suggestions.stream().limit(10).collect(Collectors.toList()));
    }

}
