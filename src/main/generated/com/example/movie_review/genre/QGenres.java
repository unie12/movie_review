package com.example.movie_review.genre;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGenres is a Querydsl query type for Genres
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGenres extends EntityPathBase<Genres> {

    private static final long serialVersionUID = -437678895L;

    public static final QGenres genres = new QGenres("genres");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<com.example.movie_review.movieDetail.domain.MovieDetails, com.example.movie_review.movieDetail.domain.QMovieDetails> movies = this.<com.example.movie_review.movieDetail.domain.MovieDetails, com.example.movie_review.movieDetail.domain.QMovieDetails>createSet("movies", com.example.movie_review.movieDetail.domain.MovieDetails.class, com.example.movie_review.movieDetail.domain.QMovieDetails.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public QGenres(String variable) {
        super(Genres.class, forVariable(variable));
    }

    public QGenres(Path<? extends Genres> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGenres(PathMetadata metadata) {
        super(Genres.class, metadata);
    }

}

