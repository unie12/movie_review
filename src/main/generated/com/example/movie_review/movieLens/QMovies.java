package com.example.movie_review.movieLens;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovies is a Querydsl query type for Movies
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovies extends EntityPathBase<Movies> {

    private static final long serialVersionUID = 489551001L;

    public static final QMovies movies = new QMovies("movies");

    public final SetPath<com.example.movie_review.genre.Genres, com.example.movie_review.genre.QGenres> genres = this.<com.example.movie_review.genre.Genres, com.example.movie_review.genre.QGenres>createSet("genres", com.example.movie_review.genre.Genres.class, com.example.movie_review.genre.QGenres.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<com.example.movie_review.rating.Ratings, com.example.movie_review.rating.QRatings> ratings = this.<com.example.movie_review.rating.Ratings, com.example.movie_review.rating.QRatings>createSet("ratings", com.example.movie_review.rating.Ratings.class, com.example.movie_review.rating.QRatings.class, PathInits.DIRECT2);

    public final NumberPath<Long> tId = createNumber("tId", Long.class);

    public final StringPath title = createString("title");

    public QMovies(String variable) {
        super(Movies.class, forVariable(variable));
    }

    public QMovies(Path<? extends Movies> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMovies(PathMetadata metadata) {
        super(Movies.class, metadata);
    }

}

