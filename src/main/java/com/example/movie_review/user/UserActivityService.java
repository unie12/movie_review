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

}
