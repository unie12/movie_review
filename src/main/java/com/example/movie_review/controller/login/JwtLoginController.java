package com.example.movie_review.controller.login;

import com.example.movie_review.auth.JwtTokenUtil;
import com.example.movie_review.domain.DTO.JoinRequest;
import com.example.movie_review.domain.DTO.LoginRequest;
import com.example.movie_review.movie.ActorDetails;
import com.example.movie_review.movie.MovieDetails;
import com.example.movie_review.movie.MovieService;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.User;
import com.example.movie_review.service.ReviewService;
import com.example.movie_review.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import com.example.movie_review.kobis.KobisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final TmdbService tmdbService;
    private final KobisService kobisService;
    private final MovieService movieService;

    private final ObjectMapper objectMapper;
    @GetMapping({"", "/"})
    public String home(Model model, Authentication auth) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        if (auth != null) {
            User loginUser = userService.getLoginUserByLoginId(auth.getName());
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }
        // 동기적으로 영화 데이터를 가져와서 모델에 추가
        String popularMovies = "";
        String trendingMovies = "";
        String dailyBoxOfficeMovies = "";
        String weeklyBoxOfficeMovies = "";
        try {
            popularMovies = tmdbService.getPopularMovies().block();
            trendingMovies = tmdbService.getTrendingMovies().block();
            dailyBoxOfficeMovies = kobisService.getDailyBoxOfficeMovies().block();
            weeklyBoxOfficeMovies = kobisService.getWeeklyBoxOfficeMovies().block();
        } catch (Exception e) {
            log.error("Error fetching movie data", e);
        }

        model.addAttribute("popularMovies", popularMovies);
        model.addAttribute("trendingMovies", trendingMovies);

        model.addAttribute("dBOM", dailyBoxOfficeMovies);
        model.addAttribute("wBOM", weeklyBoxOfficeMovies);
        return "home";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam String query, Model model) {
        String searchResults = tmdbService.searchMovies(query).block();
        model.addAttribute("movies", searchResults);
        System.out.println("jwt searchhhh");
        return "home";
    }

    @GetMapping("/contents/{movieId}")
    public String movieDetail(@PathVariable Long movieId, Model model) {
        String movieDetailsJson = tmdbService.getMovieDetails(movieId).block();
        try {
            MovieDetails movieDetails = objectMapper.readValue(movieDetailsJson, MovieDetails.class);

            // 감독 정보 추출
            List<MovieDetails.Credits.Crew> directors = new ArrayList<>();
            if (movieDetails.getCredits() != null && movieDetails.getCredits().getCrew() != null) {
                directors = movieDetails.getCredits().getCrew().stream()
                        .filter(crew -> "Director".equals(crew.getJob()))
//                        .map(MovieDetails.Credits.Crew::getName)
                        .collect(Collectors.toList());
            }
            model.addAttribute("movieDetails", movieDetails);
            model.addAttribute("directors", directors);
            System.out.println("directors = " + directors);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "movieDetail";
    }

    @GetMapping("/people/{actorId}")
    public String actorDetail(@PathVariable Long actorId, Model model) {
        String actorDetailsJson = tmdbService.getActorDetails(actorId).block();
        System.out.println("actorDetailsJson = " + actorDetailsJson);

        // popularity sort, media_type = movie만 일단
        try {
            if (actorDetailsJson == null || actorDetailsJson.isEmpty()) {
                throw new Exception("Empty or null JSON data");
            }
            ActorDetails actorDetails = objectMapper.readValue(actorDetailsJson, ActorDetails.class);
            System.out.println("actorDetailss = " + actorDetails);

            List<ActorDetails.Cast> sortedCast = actorDetails.getCast().stream()
                            .filter(cast -> "movie".equals(cast.getMedia_type()))
                            .sorted((cast1, cast2) -> Double.compare(cast2.getPopularity(), cast1.getPopularity()))
                            .collect(Collectors.toList());
            actorDetails.setCast(sortedCast);
            System.out.println("actorDetaisl = " + actorDetails);
            model.addAttribute("actorDetails", actorDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "actorDetail";
    }




    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        // loginID 중복 체크
        if (userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }
        // 닉네임 중복 체크
        if (userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }
        // password passwordCheck 동일 확인
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userService.join(joinRequest);
        return "redirect:/jwt-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
                        HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        User user = userService.login(loginRequest);
        if (user == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "login";
        }

        // 로그인 성공 -> Jwt toekn 발급
        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 1000 * 60 * 60;

        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);

        // 발급한 토큰을 cookie를 통해 전송
        // 클라이언트는 다음 요청부터 토큰이 담긴 쿠키를 통해 인증 짆ㅇ
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setPath("/"); // 경로 설정 추가
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        return "redirect:/jwt-login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        // 쿠키 파기
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/jwt-login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        return "admin";
    }

    @GetMapping("/authentication-fail")
    public String authenticationFail(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        return "errorPage/authenticationFail";
    }

    @GetMapping("/authorization-fail")
    public String authorizationFail(Model model) {
        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "Jwt Token 화면 로그인");

        return "errorPage/authorizationFail";
    }
}