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

        List<FavoriteMovieItem> movie = dto.getFavoriteMovies();
        List<FavoriteMovieItem> sortedMovie = sortFavorite(movie, sort);

        dto.setFavoriteMovies(sortedMovie);

        return new UserActivityDTOAdapter(dto);
    }

    private List<FavoriteMovieItem> sortFavorite(List<FavoriteMovieItem> movie, String sort) {
        switch (sort) {
            default:
            case "favorite_date_desc":
                return movie.stream()
                        .sorted((a, b) -> b.getFavoriteDate().compareTo(a.getFavoriteDate()))
                        .collect(Collectors.toList());
            case "favorite_date_asc":
                return movie.stream()
                        .sorted(Comparator.comparing(FavoriteMovieItem::getFavoriteDate))
                        .collect(Collectors.toList());
            case "release_date_desc":
                return movie.stream()
                        .sorted((a, b) -> b.getMovieCommonDTO().getRelease_date().compareTo(a.getMovieCommonDTO().getRelease_date()))
                        .collect(Collectors.toList());
            case "release_date_asc":
                return movie.stream()
                        .sorted(Comparator.comparing(r -> r.getMovieCommonDTO().getRelease_date()))
                        .collect(Collectors.toList());
            case "runtime_desc":
                return movie.stream()
                        .sorted((a, b) -> b.getMovieCommonDTO().getRuntime().compareTo(a.getMovieCommonDTO().getRuntime()))
                        .collect(Collectors.toList());
            case "runtime_asc":
                return movie.stream()
                        .sorted(Comparator.comparing(r -> r.getMovieCommonDTO().getRuntime()))
                        .collect(Collectors.toList());
            case "ajou_rating_desc":
                return movie.stream()
                        .sorted((a, b) -> b.getMovieCommonDTO().getAjou_rating().compareTo(a.getMovieCommonDTO().getAjou_rating()))
                        .collect(Collectors.toList());
            case "ajou_rating_asc":
                return movie.stream()
                        .sorted(Comparator.comparing(r -> r.getMovieCommonDTO().getAjou_rating()))
                        .collect(Collectors.toList());
        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("favorite_date_desc", "찜 최근에 담은순"),
                new SortOption("favorite_date_asc", "찜 과거에 담은순"),
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순"),
                new SortOption("runtime_desc", "상영 시간 긴순"),
                new SortOption("runtime_asc", "상영 시간 짧은순"),
                new SortOption("ajou-rating_desc", "아주대 평점 높은순"),
                new SortOption("ajou-rating_asc", "아주대 평점 낮은순")
        );
    }
}
