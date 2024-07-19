package com.example.movie_review.user.service;

import com.example.movie_review.user.DTO.RatingDTO;
import com.example.movie_review.user.DTO.UserActivityDTO;
import com.example.movie_review.user.DTO.UserActivityDTOAdapter;
import com.example.movie_review.user.DTO.UserDTOService;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("rating")
@RequiredArgsConstructor
public class RatingService implements UserActivityService {
    private final UserDTOService userDTOService;

    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        RatingDTO dto = userDTOService.getRatingsDTO(userEmail);
        return new UserActivityDTOAdapter(dto);
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("rating_date_desc", "평가 최근순"),
                new SortOption("rating_date_asc", "평가 과거순"),
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순")
        );
    }
}
