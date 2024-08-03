package com.example.movie_review.dbMovie;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDbMovies is a Querydsl query type for DbMovies
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDbMovies extends EntityPathBase<DbMovies> {

    private static final long serialVersionUID = 91940403L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDbMovies dbMovies = new QDbMovies("dbMovies");

    public final ListPath<com.example.movie_review.dbRating.DbRatings, com.example.movie_review.dbRating.QDbRatings> dbRatings = this.<com.example.movie_review.dbRating.DbRatings, com.example.movie_review.dbRating.QDbRatings>createList("dbRatings", com.example.movie_review.dbRating.DbRatings.class, com.example.movie_review.dbRating.QDbRatings.class, PathInits.DIRECT2);

    public final ListPath<com.example.movie_review.favoriteMovie.UserFavoriteMovie, com.example.movie_review.favoriteMovie.QUserFavoriteMovie> favoritedByUsers = this.<com.example.movie_review.favoriteMovie.UserFavoriteMovie, com.example.movie_review.favoriteMovie.QUserFavoriteMovie>createList("favoritedByUsers", com.example.movie_review.favoriteMovie.UserFavoriteMovie.class, com.example.movie_review.favoriteMovie.QUserFavoriteMovie.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.movie_review.movieDetail.domain.QMovieDetails movieDetails;

    public final ListPath<com.example.movie_review.review.Review, com.example.movie_review.review.QReview> reviews = this.<com.example.movie_review.review.Review, com.example.movie_review.review.QReview>createList("reviews", com.example.movie_review.review.Review.class, com.example.movie_review.review.QReview.class, PathInits.DIRECT2);

    public final NumberPath<Long> tmdbId = createNumber("tmdbId", Long.class);

    public QDbMovies(String variable) {
        this(DbMovies.class, forVariable(variable), INITS);
    }

    public QDbMovies(Path<? extends DbMovies> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDbMovies(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDbMovies(PathMetadata metadata, PathInits inits) {
        this(DbMovies.class, metadata, inits);
    }

    public QDbMovies(Class<? extends DbMovies> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movieDetails = inits.isInitialized("movieDetails") ? new com.example.movie_review.movieDetail.domain.QMovieDetails(forProperty("movieDetails"), inits.get("movieDetails")) : null;
    }

}

