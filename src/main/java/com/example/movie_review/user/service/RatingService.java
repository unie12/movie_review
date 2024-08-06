package com.example.movie_review.user.service;

import com.example.movie_review.dbRating.DbRatingDTO;
import com.example.movie_review.user.DTO.RatingDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserActivityDTOAdapter;
import com.example.movie_review.user.SortOption;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("rating")
public class RatingService extends AbstractUserActivityService {
    public RatingService(UserDTOService userDTOService) {
        super(userDTOService);
    }

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        RatingDTO dto = userDTOService.getRatingsDTO(userEmail);

        int start = page * size;
        int end = Math.min(start + size, dto.getRatings().size());

        List<DbRatingDTO> sortedDTO = sortRating(dto.getRatings(), sort);
        dto.setRatings(sortedDTO.subList(start, end));

        return new UserActivityDTOAdapter(dto);
    }

    private List<DbRatingDTO> sortRating(List<DbRatingDTO> ratings, String sort) {
        switch (sort) {
            case "rating_date_desc":
                return ratings.stream()
                        .sorted((a, b) -> b.getRatingDate().compareTo(a.getRatingDate()))
                        .collect(Collectors.toList());
            case "rating_date_asc":
                return ratings.stream()
                        .sorted(Comparator.comparing(DbRatingDTO::getRatingDate))
                        .collect(Collectors.toList());
            case "rating_desc":
                return ratings.stream()
                        .sorted((a, b) -> b.getScore().compareTo(a.getScore()))
                        .collect(Collectors.toList());
            case "rating_asc":
                return ratings.stream()
                        .sorted(Comparator.comparing(DbRatingDTO::getScore))
                        .collect(Collectors.toList());
            default:
                return sortByCommonMovieOptions(ratings, sort, DbRatingDTO::getMovie);

        }
    }

    @Override
    public List<SortOption> getSortOptions() {
        List<SortOption> options = new ArrayList<>(Arrays.asList(
                new SortOption("rating_date_desc", "평가 최근순"),
                new SortOption("rating_date_asc", "평가 과거순"),
        new SortOption("rating_desc", "평가 높은순"),
        new SortOption("rating_asc", "평가 낮은순")
        ));
        options.addAll(getCommonSortOptions());
        return options;
    }
}
