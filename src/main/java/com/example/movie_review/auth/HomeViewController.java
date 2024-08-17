package com.example.movie_review.auth;

import com.example.movie_review.dbMovie.repository.MovieCacheRepository;
import com.example.movie_review.dbMovie.service.MovieCacheService;
import com.example.movie_review.movieDetail.DTO.MovieSearchDTO;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.DTO.UserSearchDTO;
import com.example.movie_review.user.service.UserDTOService;
import com.example.movie_review.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class HomeViewController {

    private final TmdbService tmdbService;
    private final MovieCacheService movieCacheService;
    private final UserDTOService userDTOService;
    private final UserService userService;
    private final MovieCacheRepository movieCacheRepository;

    private final ObjectMapper objectMapper;


    @Timed(value = "home.request", description = "Time taken to return the home page")
    @GetMapping({"", "/"})
    public String home(@CookieValue(name = "jwtToken", required = false) String token, Model model, HttpServletResponse response) throws JsonProcessingException {
//        movieCacheService.updateDailyMovieCache();
//        movieCacheService.updateWeeklyMovieCache();
//        userService.updateUserRoles();

        model.addAttribute("popularMovies", movieCacheService.getPopularMovies());
        model.addAttribute("trendingMovies", movieCacheService.getTrendingMovies());
        model.addAttribute("dBOM", movieCacheService.getDailyBoxOffice());
        model.addAttribute("wBOM", movieCacheService.getWeeklyBoxOffice());
        model.addAttribute("ratingKings", userDTOService.getWeeklyRatingUsers());
        model.addAttribute("reviewKings", userDTOService.getWeeklyReviewUsers());

        return "home";

//        if (token != null && !JwtTokenUtil.isExpired(token, "my-secret-key-123123")) {
//            try {
//                String email = JwtTokenUtil.getLoginId(token, "my-secret-key-123123");

                // 영화 데이터 로딩
//                MovieCache dailyCache = movieCacheRepository.findByType(MovieType.DBOM)
//                        .orElseThrow(() -> new RuntimeException("Daily cache not found"));
//                MovieCache weeklyCache = movieCacheRepository.findByType(MovieType.WBOM)
//                        .orElseThrow(() -> new RuntimeException("Weekly cache not found"));
//                MovieCache popularCache = movieCacheRepository.findByType(MovieType.POPULAR)
//                        .orElseThrow(() -> new RuntimeException("Popular cache not found"));
//                MovieCache trendingCache = movieCacheRepository.findByType(MovieType.TRENDING)
//                        .orElseThrow(() -> new RuntimeException("Trending cache not found"));
//
//                model.addAttribute("popularMovies", popularCache.getMovieData());
//                model.addAttribute("trendingMovies", trendingCache.getMovieData());
//                model.addAttribute("dBOM", dailyCache.getMovieData());
//                model.addAttribute("wBOM", weeklyCache.getMovieData());
//
//        List<WeeklyUserDTO> weeklyRatingUsers = userDTOService.getWeeklyRatingUsers();
//        List<WeeklyUserDTO> weeklyReviewUsers = userDTOService.getWeeklyReviewUsers();
//
//        model.addAttribute("ratingKings", weeklyRatingUsers);
//        model.addAttribute("reviewKings", weeklyReviewUsers);

//            } catch (Exception e) {
                // JWT 파싱 중 예외 발생 시
//                System.out.println("Error parsing JWT token: " + e.getMessage());
//                return "redirect:/jwt-login";
//            }
//        } else {
//             토큰이 없거나 만료된 경우
//            return "redirect:/jwt-login";
//        }
    }

    /**
     * 서버에서 초기에 검색 결과에 대한 리스트 보내주고 이후
     * HomeController에서 api를 통해 추가 정보 업로드
     */
    @GetMapping("/search")
    public String searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "movieTitle") String searchType,
            Model model, Authentication auth) throws JsonProcessingException {
        if(searchType.equals("movieTitle")) {
            Page<MovieSearchDTO> searchResults = tmdbService.searchMovies(query, page);
            model.addAttribute("searchResults", searchResults);
        } else if(searchType.equals("userNickname")) {
            String currentEmail = auth != null ? auth.getName() : null;
            Page<UserSearchDTO> searchResults = userDTOService.searchUsers(query, page, 10, currentEmail);
            model.addAttribute("searchResults", searchResults);
        }

        model.addAttribute("query", query);
        model.addAttribute("currentPage", page);
        model.addAttribute("searchType", searchType);

        return "search";
    }
}