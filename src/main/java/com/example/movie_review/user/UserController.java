package com.example.movie_review.user;

import com.example.movie_review.heart.Heart;
import com.example.movie_review.dbRating.DbRatingService;
import com.example.movie_review.genre.*;
import com.example.movie_review.movie.PreferredMovies;
import com.example.movie_review.movie.PreferredMoviesService;
import com.example.movie_review.oauth.SessionUser;
import com.example.movie_review.review.Review;
import com.example.movie_review.review.ReviewDTO;
import com.example.movie_review.review.ReviewService;
import com.example.movie_review.tmdb.TmdbService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
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
    private final ReviewService reviewService;
    private final DbRatingService dbRatingService;
    private final UserDTOService userDTOService;

    /**
     * 사용자 추가 정보 처리
     */
    @GetMapping("/additional-info")
    public String additionalInfoPage(Model model) {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userService.getUserByEmail(sessionUser.getEmail());

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
        User user = userService.getUserByEmail(sessionUser.getEmail());

        return preferredMoviesService.findByUser(user);
    }
    @GetMapping("/favorite-genres")
    @ResponseBody
    public List<PreferredGenres> getFavoriteGenres() {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userService.getUserByEmail(sessionUser.getEmail());
        return preferredGenresService.findByUser(user);
    }


    /**
     * 사용자 활동 내역 보기
     */
    @GetMapping("/info/{userEmail}")
    public String userInfo(@PathVariable String userEmail, Model model, Authentication auth) throws AccessDeniedException {
        UserDTO userDTO = userDTOService.getuserDTO(userEmail, auth);
        model.addAttribute("userDTO", userDTO);
        return "info";
    }

    /**
     * 찜한 영화 확인
     */
    @GetMapping("/info/{userEmail}/{category}")
    public String getUserInfo(@PathVariable String userEmail, @PathVariable String category, Model model, Authentication auth) throws AccessDeniedException {
        User user = userService.getUserByEmail(userEmail);

        if(!user.getEmail().equals(auth.getName())) {
            throw new AccessDeniedException("You don't have permission to view this user");
        }

        UserDTO userDTO = userDTOService.getuserDTO(userEmail, auth);
        model.addAttribute("userDTO", userDTO);

        switch (category) {
            case "favorite":
//                model.addAttribute("favoriteMovies", user.getUserFavoriteMovies());
                return "user-favorite-movies";
            case "review":
                List<Review> reviews = user.getReviews();
                List<ReviewDTO> reviewDTOS = reviewService.getReviewDTOs(reviews);
                model.addAttribute("reviewDTOs", reviewDTOS);
                return "user-reviews";
            case "rating":
//                model.addAttribute("dbRatings", user.getDbRatings());
                return "user-ratings";
            case "heart":
                List<Heart> hearts = user.getHearts();
                List<ReviewDTO> heartReviewDTOs = reviewService.getReviewDTOs(
                        hearts.stream().map(Heart::getReview).collect(Collectors.toList()));
                model.addAttribute("heartReviewDTOs", heartReviewDTOs);
                return "user-hearts";
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category");
        }
    }


}
