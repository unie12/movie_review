package com.example.movie_review.user;

import com.example.movie_review.genre.*;
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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final GenresRepository genresRepository;
    private final PreferredGenresRepository preferredGenresRepository;
    private final HttpSession httpSession;

    private final UserService userService;
    private final TmdbService tmdbService;
    private final PreferredMoviesService preferredMoviesService;
    private final PreferredGenresService preferredGenresService;

    /**
     * 사용자 추가 정보 처리
     */
    @GetMapping("/additional-info")
    public String additionalInfoPage(Model model) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Genres> allGenres = genresRepository.findAll(); // 모든 장르 데이터 (movielens + tmdb)
        List<Genres> tmdbGenres = allGenres.stream() // tmdb 장르 데이터만
                .filter(genre -> genre.getName().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))
                .collect(Collectors.toList());

        List<PreferredGenres> userPreferredGenres = preferredGenresService.findByUser(user); // 해당 사용자의 선호 장르

        // 사용자의 선호 장르 ID 목록을 생성
        List<Long> userPreferredGenreIds = userPreferredGenres.stream()
                .map(PreferredGenres::getGenreId)
                .collect(Collectors.toList());

        model.addAttribute("tmdbGenres", tmdbGenres);
        model.addAttribute("userPreferredGenreIds", userPreferredGenreIds);
        model.addAttribute("user", sessionUser);
        return "additional-info";
    }

    @Transactional
    @PostMapping("/additional-info")
    public String saveAdditionalInfo(@RequestParam String gender,
                                     @RequestParam String nickname,
                                     @RequestParam Long age,
                                     @RequestParam String mbti,
                                     @RequestParam String favoriteMovies,
                                     @RequestParam List<Long> preferredGenres) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        if(userRepository.findByNickname(nickname) != null && !user.getNickname().equals(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다");
        }
        user.update(nickname, gender, age, mbti);

        // 선호 장르 저장
        for(Long preferredGenreId : preferredGenres) {
            System.out.println("preferredGenreId = " + preferredGenreId);
            Genres genre = genresRepository.findById(preferredGenreId).orElseThrow(() -> new IllegalArgumentException("Invalid genre ID: " + preferredGenreId));


            PreferredGenres tmpPreGenre = PreferredGenres.builder()
                    .user(user)
                    .genreName(genre.getName())
                    .genreId(genre.getId())
                    .build();
            preferredGenresService.savePreferredGenre(tmpPreGenre);
        }

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
        for (PreferredMovies preferredMovie : preferredMoviesList) {
            preferredMoviesService.savePreferredMovie(preferredMovie);
        }

        userRepository.save(user);
        httpSession.setAttribute("user", new SessionUser(user)); // 세션 정보 업데이트

        return "redirect:/jwt-login";
    }

    @GetMapping("/check-nickname")
    @ResponseBody
    public String checkNickname(@RequestParam String nickname) {
        User user = userRepository.findByNickname(nickname);
        return user == null ? "available" : "unavailable";
    }

    @GetMapping("/search-movies")
    @ResponseBody
    public Mono<String> searchMovies(@RequestParam String query) {
        System.out.println("queryyyy = " + query);
        return tmdbService.searchMovies(query);
    }

    /**
     * 해당 사용자의 선호 영화, 장르 가져오기
     */
    @GetMapping("/favorite-movies")
    @ResponseBody
    public List<PreferredMovies> getFavoriteMovies() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));
        return preferredMoviesService.findByUser(user);
    }
    @GetMapping("/favorite-genres")
    @ResponseBody
    public List<PreferredGenres> getFavoriteGenres() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));
        return preferredGenresService.findByUser(user);
    }

}
