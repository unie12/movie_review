package com.example.movie_review.user.api;

import com.example.movie_review.genre.Genres;
import com.example.movie_review.genre.GenresRepository;
import com.example.movie_review.genre.PreferredGenres;
import com.example.movie_review.genre.PreferredGenresService;
import com.example.movie_review.oauth.SessionUser;
import com.example.movie_review.tmdb.TmdbService;
import com.example.movie_review.user.*;
import com.example.movie_review.user.DTO.UserProfileDTO;
import com.example.movie_review.user.DTO.UserProfileDTOService;
import com.example.movie_review.user.DTO.UserProfileUpdateRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/additional-info/{userEmail}")
public class UserProfileController {
    private final TmdbService tmdbService;
    private final UserProfileDTOService userProfileDTOService;

    @GetMapping
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String userEmail) {
        ResponseEntity<UserProfileDTO> ok = ResponseEntity.ok(userProfileDTOService.getUserProfileDTO(userEmail));
        System.out.println("ok = " + ok);
        return ok;
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateUserProfile(@PathVariable String userEmail, @RequestBody UserProfileUpdateRequest updateRequest) {
        System.out.println("Update request details: " + updateRequest);
        return ResponseEntity.ok(userProfileDTOService.updateUserProfile(userEmail, updateRequest));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameAvailability(@RequestParam String nickname) {
        return ResponseEntity.ok(userProfileDTOService.isNicknameAvailable(nickname));
    }

    @GetMapping("/search-movies")
    public Mono<ResponseEntity<String>> searchMovies(@RequestParam String query) {
        return tmdbService.searchMovies(query)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/favorite-movies")
    public ResponseEntity<List<PreferredMovies>> getFavoriteMovies(@PathVariable String userEmail) {
        return ResponseEntity.ok(userProfileDTOService.getFavoriteMovies(userEmail));
    }

    @GetMapping("/favorite-genres")
    public ResponseEntity<List<PreferredGenres>> getFavoriteGenres(@PathVariable String userEmail) {
        ResponseEntity<List<PreferredGenres>> ok = ResponseEntity.ok(userProfileDTOService.getFavoriteGenres(userEmail));
        System.out.println("PreferGenre ok = " + ok);
        return ok;
    }


//    private final HttpSession httpSession;
//
//    private final UserRepository userRepository;
//    private final GenresRepository genresRepository;
//
//    private final PreferredGenresService preferredGenresService;
//    private final PreferredMoviesService preferredMoviesService;
//    private final TmdbService tmdbService;
//    private final UserService userService;
//
//    @Transactional
//    @PostMapping("/{userEmail}")
//    public String saveAdditionalInfo(@RequestParam String gender,
//                                     @RequestParam String nickname,
//                                     @RequestParam Long age,
//                                     @RequestParam String mbti,
//                                     @RequestParam String favoriteMovies,
//                                     @RequestParam List<Long> preferredGenres) {
//        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
//        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
//
//        if(userRepository.findByNickname(nickname) != null && !user.getNickname().equals(nickname)) {
//            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다");
//        }
//        user.update(nickname, gender, age, mbti);
//
//        // 선호 장르 저장
//        for(Long preferredGenreId : preferredGenres) {
//            System.out.println("preferredGenreId = " + preferredGenreId);
//            Genres genre = genresRepository.findById(preferredGenreId).orElseThrow(() -> new IllegalArgumentException("Invalid genre ID: " + preferredGenreId));
//
//
//            PreferredGenres tmpPreGenre = PreferredGenres.builder()
//                    .user(user)
//                    .genreName(genre.getName())
//                    .genreId(genre.getId())
//                    .build();
//            preferredGenresService.savePreferredGenre(tmpPreGenre);
//        }
//
//        // 선호 영화 목록 저장
//        String[] moviesArray = favoriteMovies.split(",");
//        List<PreferredMovies> preferredMoviesList = new ArrayList<>();
//
//        for (int i = 0; i < moviesArray.length; i += 2) {
//            String movieTitle = moviesArray[i].trim();
//            String movieId = moviesArray[i + 1].trim();
//
//            PreferredMovies preferredMovie = PreferredMovies.builder()
//                    .user(user)
//                    .movieTitle(movieTitle)
//                    .movieId(movieId)
//                    .build();
//
//            preferredMoviesList.add(preferredMovie);
//        }
//        for (PreferredMovies preferredMovie : preferredMoviesList) {
//            preferredMoviesService.savePreferredMovie(preferredMovie);
//        }
//
//        userRepository.save(user);
//        httpSession.setAttribute("user", new SessionUser(user)); // 세션 정보 업데이트
//
//        return "redirect:/jwt-login";
//    }
//
//    @GetMapping("/check-nickname")
//    @ResponseBody
//    public String checkNickname(@RequestParam String nickname) {
//        User user = userRepository.findByNickname(nickname);
//        return user == null ? "available" : "unavailable";
//    }
//
//    @GetMapping("/search-movies")
//    @ResponseBody
//    public Mono<String> searchMovies(@RequestParam String query) {
//        System.out.println("queryyyy = " + query);
//        return tmdbService.searchMovies(query);
//    }
//
//
//    /**
//     * 해당 사용자의 선호 영화, 장르 가져오기
//     */
//    @GetMapping("/favorite-movies")
//    @ResponseBody
//    public List<PreferredMovies> getFavoriteMovies() {
//        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
//        User user = userService.getUserByEmail(sessionUser.getEmail());
//
//        return preferredMoviesService.findByUser(user);
//    }
//    @GetMapping("/favorite-genres")
//    @ResponseBody
//    public List<PreferredGenres> getFavoriteGenres() {
//        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
//        User user = userService.getUserByEmail(sessionUser.getEmail());
//        return preferredGenresService.findByUser(user);
//    }

}
