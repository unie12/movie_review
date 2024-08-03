package com.example.movie_review.user.service;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractUserActivityService implements UserActivityService {
    protected final UserDTOService userDTOService;

    @Override
    public abstract UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size);

    @Override
    public abstract List<SortOption> getSortOptions();

//    @Override
//    public String getDefaultSortOption() {
//        List<SortOption> options = getSortOptions();
//        return options.isEmpty() ? "default" : options.get(0).getKey();
//    }

    protected List<SortOption> getCommonSortOptions() {
        return Arrays.asList(
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순"),
                new SortOption("runtime_desc", "상영 시간 긴순"),
                new SortOption("runtime_asc", "상영 시간 짧은순"),
                new SortOption("ajou_rating_desc", "아주대 평점 높은순"),
                new SortOption("ajou_rating_asc", "아주대 평점 낮은순")
        );
    }

    protected <T> List<T> sortByCommonMovieOptions(List<T> items, String sort, Function<T, MovieCommonDTO> movieGetter) {
        switch (sort) {
            case "release_date_desc":
                return items.stream()
                        .sorted((a, b) -> movieGetter.apply(b).getRelease_date().compareTo(movieGetter.apply(a).getRelease_date()))
                        .collect(Collectors.toList());
            case "release_date_asc":
                return items.stream()
                        .sorted(Comparator.comparing(item -> movieGetter.apply(item).getRelease_date()))
                        .collect(Collectors.toList());
            case "runtime_desc":
                return items.stream()
                        .sorted((a, b) -> movieGetter.apply(b).getRuntime().compareTo(movieGetter.apply(a).getRuntime()))
                        .collect(Collectors.toList());
            case "runtime_asc":
                return items.stream()
                        .sorted(Comparator.comparing(item -> movieGetter.apply(item).getRuntime()))
                        .collect(Collectors.toList());
            case "ajou_rating_desc":
                return items.stream()
                        .sorted((a, b) -> movieGetter.apply(b).getAjou_rating().compareTo(movieGetter.apply(a).getAjou_rating()))
                        .collect(Collectors.toList());
            case "ajou_rating_asc":
                return items.stream()
                        .sorted(Comparator.comparing(item -> movieGetter.apply(item).getAjou_rating()))
                        .collect(Collectors.toList());
            default:
                return items;
        }
    }
}
