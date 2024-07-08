package com.example.movie_review.movie;

import com.example.movie_review.genre.Genres;
import com.example.movie_review.genre.QGenres;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Set;

public class MovieRepositoryExtensionImpl extends QuerydslRepositorySupport implements MovieRepositoryExtension {

    public MovieRepositoryExtensionImpl() {
        super(Movies.class);
    }

    @Override
    public List<Movies> findByGenres(Set<Genres> genres, RecommendationDto recommendationDto) {
        QMovies movies
                = QMovies.movies;

        BooleanBuilder containGenres = new BooleanBuilder();
        genres.forEach(genre -> {
            containGenres.and(movies.genres.contains(genre));
        });

        BooleanBuilder notInRecommendation = new BooleanBuilder();
        recommendationDto.getPickedMovies().forEach(movieData -> {
            notInRecommendation.and(movies.id.notIn(movieData.getMovieId()));
        });

        JPQLQuery<Movies> query = from(movies)
                .where(containGenres)
                .where(notInRecommendation)
                .leftJoin(movies.genres, QGenres.genres).fetchJoin()
                .distinct().limit(10);


        return query.fetch();
    }
}
