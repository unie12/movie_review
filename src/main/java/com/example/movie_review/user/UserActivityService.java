package com.example.movie_review.user;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.dbRating.DbRatingDTO;
import com.example.movie_review.review.ReviewMovieDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserCommonDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public interface UserActivityService {
    UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size);
    List<SortOption> getSortOptions();

    /**
     * 공통 정렬 옵션
     */
    default List<SortOption> getCommonSortOptions() {
        return Arrays.asList(
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순"),
//                new SortOption("release_date_desc", "평균 별점 높은순"),
//                new SortOption("release_date_asc", "평균 별점 낮은순"),
                new SortOption("alphabet_desc", "가나다 순"),
                new SortOption("alphabet_asc", "가나다 역순")
        );
    }

    /**
     * 정렬 로직
     */
    default List<?> sortActivityItems(List<?> items, String sort) {
        switch (sort) {
            case "release_date_desc":
                return items.stream()
                        .sorted(Comparator.comparing(item -> getReleaseDateFromItem(item), Comparator.nullsLast(Comparator.reverseOrder())))
                        .collect(Collectors.toList());
            case "release_date_asc":
                return items.stream()
                        .sorted(Comparator.comparing(item -> getReleaseDateFromItem(item), Comparator.nullsFirst(Comparator.naturalOrder())))
                        .collect(Collectors.toList());
//            case "alphabet_desc":
//                return items.stream()
//                        .sorted(Comparator.comparing(item -> getReleaseDateFromItem(item), Comparator.nullsFirst(Comparator.naturalOrder())))
//                        .collect(Collectors.toList());
//            case "alphabet_asc":
//                return items.stream()
//                        .sorted(Comparator.comparing(item -> getReleaseDateFromItem(item), Comparator.nullsFirst(Comparator.naturalOrder())))
//                        .collect(Collectors.toList());

            default:
                return items;
        }
    }

    default String getReleaseDateFromItem(Object item) {
        if (item instanceof ReviewMovieDTO) {
            return ((ReviewMovieDTO) item).getMovieCommonDTO().getRelease_date();
        } else if(item instanceof MovieCommonDTO) {
            return ((MovieCommonDTO) item).getRelease_date();
        } else if (item instanceof DbRatingDTO) {
        }
        return null;
    }

}
