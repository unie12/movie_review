package com.example.movie_review.movieDetail.repository;

import com.example.movie_review.dbMovie.DTO.MovieCommonDTO;
import com.example.movie_review.movieDetail.domain.MovieDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface MovieDetailRepository extends JpaRepository<MovieDetails, Long> {
    List<MovieDetails> findTop10ByTitleContainingIgnoreCase(String title);

    MovieDetails findBytId(Long tId);

    // 투표 수를 기준으로 인기 영화 찾기
    @Query("SELECT m FROM MovieDetails m WHERE m.vote_count >= :minVotes ORDER BY RANDOM()")
    List<MovieDetails> findRandomPopularMovies(@Param("minVotes") Long minVotes, Pageable pageable);

    // 특정 장르의 영화 찾기
    @Query("SELECT DISTINCT m FROM MovieDetails m JOIN m.genres g WHERE g.id = :genreId AND m.vote_count >= :minVotes")
    List<MovieDetails> findByGenreId(@Param("genreId") Long genreId, @Param("minVotes") Long minVotes, Pageable pageable);
}
