package com.example.movie_review.user.service;

import com.example.movie_review.user.DTO.*;
import com.example.movie_review.user.SortOption;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("favorite")
public class FavoriteService extends AbstractUserActivityService {
    public FavoriteService(UserDTOService userDTOService) {
        super(userDTOService);
    }

    @Override
    public UserActivityDTO getUserActivity(String authEmail, String userEmail, String sort, int page, int size) {
        FavoriteMovieDTO dto = userDTOService.getUserFavoriteMoviesDTO(authEmail, userEmail);
        List<FavoriteMovieItem> movie = dto.getFavoriteMovies();

        int start = page * size;
        int end = Math.min(start + size, movie.size());

        List<FavoriteMovieItem> sortedMovie = sortFavorite(movie, sort).subList(start, end);
        dto.setFavoriteMovies(sortedMovie);

        return new UserActivityDTOAdapter(dto);
    }

    private List<FavoriteMovieItem> sortFavorite(List<FavoriteMovieItem> movie, String sort) {
        switch (sort) {
            case "favorite_date_desc":
                return movie.stream()
                        .sorted((a, b) -> b.getFavoriteDate().compareTo(a.getFavoriteDate()))
                        .collect(Collectors.toList());
            case "favorite_date_asc":
                return movie.stream()
                        .sorted(Comparator.comparing(FavoriteMovieItem::getFavoriteDate))
                        .collect(Collectors.toList());
            default:
                return sortByCommonMovieOptions(movie, sort, FavoriteMovieItem::getMovieCommonDTO);
        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        List<SortOption> options = new ArrayList<>(Arrays.asList(
                new SortOption("favorite_date_desc", "찜 최근에 담은순"),
                new SortOption("favorite_date_asc", "찜 과거에 담은순")
        ));
        options.addAll(getCommonSortOptions());
        return options;
    }
}
