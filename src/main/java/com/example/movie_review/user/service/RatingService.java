package com.example.movie_review.user.service;

import com.example.movie_review.dbRating.DbRatingDTO;
import com.example.movie_review.user.DTO.RatingDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserActivityDTOAdapter;
import com.example.movie_review.user.DTO.UserDTOService;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("rating")
@RequiredArgsConstructor
public class RatingService implements UserActivityService {
    private final UserDTOService userDTOService;

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        RatingDTO dto = userDTOService.getRatingsDTO(userEmail);
        List<DbRatingDTO> sortedDTO = sortRating(dto.getRatings(), sort);

        dto.setRatings(sortedDTO);
        return new UserActivityDTOAdapter(dto);
    }

    private List<DbRatingDTO> sortRating(List<DbRatingDTO> ratings, String sort) {
        switch (sort) {
            case "release_date_desc":
                return ratings.stream()
                        .sorted((a, b) -> b.getMovie().getRelease_date().compareTo(a.getMovie().getRelease_date()))
                        .collect(Collectors.toList());
            case "release_date_asc":
                return ratings.stream()
                        .sorted(Comparator.comparing(r -> r.getMovie().getRelease_date()))
                        .collect(Collectors.toList());
            default:
                return ratings;
        }    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("rating_date_desc", "평가 최근순"),
                new SortOption("rating_date_asc", "평가 과거순"),
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순"),
                new SortOption("runtime_desc", "상영 시간 긴순"),
                new SortOption("runtime_asc", "상영 시간 짧은순"),
                new SortOption("ajou-rating_desc", "아주대 평점 높은순"),
                new SortOption("ajou-rating_asc", "아주대 평점 낮은순")
        );
    }
}
