package com.example.movie_review.user.service;

import com.example.movie_review.dbMovie.MovieCommonDTO;
import com.example.movie_review.user.DTO.*;
import com.example.movie_review.user.SortOption;
import com.example.movie_review.user.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("favorite")
@RequiredArgsConstructor
public class FavoriteService implements UserActivityService {
    private final UserDTOService userDTOService;
    @Override
    public UserActivityDTO getUserActivity(String userEmail, String sort, int page, int size) {
        FavoriteMovieDTO dto = userDTOService.getUserFavoriteMoviesDTO(userEmail);

        return new UserActivityDTOAdapter(dto);
    }

    @Override
    public List<SortOption> getSortOptions() {
        return Arrays.asList(
                new SortOption("favorite_date_desc", "찜 최근에 담은순"),
                new SortOption("favorite_date_asc", "찜 과거에 담은순"),
                new SortOption("release_date_desc", "개봉일 최신순"),
                new SortOption("release_date_asc", "개봉일 과거순")
        );
    }
}
