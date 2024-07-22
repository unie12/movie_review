package com.example.movie_review.user.service;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.user.DTO.*;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("favorite")
@RequiredArgsConstructor
public class FavoriteService implements UserActivityService {
    private final UserDTOService userDTOService;
    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        FavoriteMovieDTO dto = userDTOService.getUserFavoriteMoviesDTO(userEmail);

        List<MovieCommonDTO> movie = dto.getFavoriteMovies();
        List<MovieCommonDTO> sortedMovie = sortFavorite(movie, sort);

        dto.setFavoriteMovies(sortedMovie);

        return new UserActivityDTOAdapter(dto);
    }

    private List<MovieCommonDTO> sortFavorite(List<MovieCommonDTO> movie, String sort) {
        switch (sort) {
            case "release_date_desc":
                return movie.stream()
                        .sorted((a, b) -> b.getRelease_date().compareTo(a.getRelease_date()))
                        .collect(Collectors.toList());
            case "release_date_asc":
                return movie.stream()
                        .sorted(Comparator.comparing(r -> r.getRelease_date()))
                        .collect(Collectors.toList());
            default:
                return movie;
        }    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순"),
                new SortOption("favorite_date_desc", "찜 최근에 담은순"),
                new SortOption("favorite_date_asc", "찜 과거에 담은순"),
                new SortOption("runtime_desc", "상영 시간 긴순"),
                new SortOption("runtime_asc", "상영 시간 짧은순"),
                new SortOption("ajou-rating_desc", "아주대 평점 높은순"),
                new SortOption("ajou-rating_asc", "아주대 평점 낮은순")
        );
    }
}
