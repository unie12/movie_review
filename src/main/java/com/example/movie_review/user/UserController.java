package com.example.movie_review.user;

import com.example.movie_review.movie.PreferredMovies;
import com.example.movie_review.movie.PreferredMoviesService;
import com.example.movie_review.oauth.SessionUser;
import com.example.movie_review.tmdb.TmdbService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    private final UserService userService;
    private final TmdbService tmdbService;
    private final PreferredMoviesService preferredMoviesService;

    @GetMapping("/additional-info")
    public String additionalInfoPage(Model model) {
//        User user = (User) httpSession.getAttribute("user");
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        model.addAttribute("user", sessionUser);
        return "additional-info";
    }

    @Transactional
    @PostMapping("/additional-info")
    public String saveAdditionalInfo(@RequestParam String gender,
                                     @RequestParam String nickname,
                                     @RequestParam Long age,
                                     @RequestParam String mbti,
                                     @RequestParam String favoriteMovies) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        user.setGender(gender);
        user.setNickname(nickname);
        user.setAge(age);
        user.setMbti(mbti);
//        user.setPreferGenres(preferGenres);
//        user.setPreferMovies(preferMovies);


        // 선호 영화 목록 저장
        String[] moviesArray = favoriteMovies.split(",");
        List<PreferredMovies> preferredMoviesList = new ArrayList<>();

        for (int i = 0; i < moviesArray.length; i += 2) {
            String movieTitle = moviesArray[i].trim();
            String movieId = moviesArray[i + 1].trim();

            PreferredMovies preferredMovie = PreferredMovies.builder()
                    .user(user)
                    .movieTitle(movieTitle)
                    .movieId(movieId)
                    .build();

            preferredMoviesList.add(preferredMovie);
        }

        // 선호 영화 목록을 저장
        for (PreferredMovies preferredMovie : preferredMoviesList) {
            preferredMoviesService.savePreferredMovie(preferredMovie);
        }

        userRepository.save(user);
        System.out.println("usercontrollerr = " + user.getNickname());

        // 세션 정보 업데이트
        httpSession.setAttribute("user", new SessionUser(user));

        return "redirect:/jwt-login";
    }

    @GetMapping("/search-movies")
    @ResponseBody
    public Mono<String> searchMovies(@RequestParam String query) {
        System.out.println("queryyyy = " + query);
        return tmdbService.searchMovies(query);
    }

    @GetMapping("/favorite-movies")
    @ResponseBody
    public List<PreferredMovies> getFavoriteMovies() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));
        return preferredMoviesService.findByUser(user);
    }

}
