package com.example.movie_review.movie;

import com.example.movie_review.dbMovie.DbMovieService;
import com.example.movie_review.dbMovie.DbMovies;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieDetailDTOService {
    private final DbMovieService dbMovieService;

    public MovieDetailDTO getMovieDetailDTO(Long movieTId) {
        DbMovies dbMovie = dbMovieService.findOrCreateMovie(movieTId);
        MovieDetailDTO movieDetailDTO = MovieDetailDTO.builder()
                .ratingCnt(dbMovie.getDbRatingCount())
                .ratingAvg(dbMovie.getDbRatingAvg())
                .reviewCnt(dbMovie.getReviewCount())
                .build();

        return movieDetailDTO;
    }
}
