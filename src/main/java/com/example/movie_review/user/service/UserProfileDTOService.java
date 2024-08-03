package com.example.movie_review.user.service;

import com.example.movie_review.genre.Genres;
import com.example.movie_review.genre.GenresRepository;
import com.example.movie_review.genre.PreferredGenres;
import com.example.movie_review.genre.PreferredGenresService;
import com.example.movie_review.movieDetail.DTO.GenreDto;
import com.example.movie_review.movieDetail.DTO.MovieDTO;
import com.example.movie_review.user.DTO.UserProfileDTO;
import com.example.movie_review.user.DTO.UserProfileUpdateRequest;
import com.example.movie_review.user.domain.PreferredMovies;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileDTOService {

    private final UserService userService;
    private final PreferredGenresService preferredGenresService;
    private final PreferredMoviesService preferredMoviesService;

    private final GenresRepository genresRepository;
    private final UserRepository userRepository;

    public UserProfileDTO getUserProfileDTO(String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        List<Genres> allGenres = genresRepository.findAll();

        List<Genres> filteredGenres = allGenres.stream()
                .filter(genre -> genre.getName().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") ||
                        genre.getName().equals("SF"))
                .collect(Collectors.toList());

        List<PreferredGenres> userPreferredGenres = preferredGenresService.findByUser(user);
        List<PreferredMovies> userPreferredMovies = preferredMoviesService.findByUser(user);

        return UserProfileDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .age(user.getAge())
                .gender(user.getGender())
                .mbti(user.getMbti())
                .tmdbGenres(filteredGenres.stream().map(this::convertToGenreDTO).collect(Collectors.toList()))
                .userPreferredGenreIds(userPreferredGenres.stream().map(PreferredGenres::getGenreId).collect(Collectors.toList()))
                .favoriteMovies(userPreferredMovies.stream().map(this::convertToMovieDTO).collect(Collectors.toList()))
                .build();
    }

    private MovieDTO convertToMovieDTO(PreferredMovies preferredMovies) {
        return MovieDTO.builder()
                .id(preferredMovies.getMovieId())
                .title(preferredMovies.getMovieTitle())
                .build();
    }

    private GenreDto convertToGenreDTO(Genres genres) {
        return GenreDto.builder()
                .id(genres.getId())
                .name(genres.getName())
                .build();
    }

    public UserProfileDTO updateUserProfile(String userEmail, UserProfileUpdateRequest updateRequest) {
        User user = userService.getUserByEmail(userEmail);
        user.update(updateRequest.getNickname(), updateRequest.getGender(), updateRequest.getAge(), updateRequest.getMbti());

        preferredGenresService.updatePreferredGenres(user, updateRequest.getPreferredGenreIds());
        preferredMoviesService.updatePreferredMovies(user, updateRequest.getFavoriteMovies());

        userRepository.save(user);

        return getUserProfileDTO(userEmail);
    }

    public Boolean isNicknameAvailable(String nickname) {
        return userRepository.findByNickname(nickname) == null;
    }

    public List<PreferredGenres> getFavoriteGenres(String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        return preferredGenresService.findByUser(user);
    }
}
