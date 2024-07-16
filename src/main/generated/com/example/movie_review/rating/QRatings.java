package com.example.movie_review.rating;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRatings is a Querydsl query type for Ratings
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRatings extends EntityPathBase<Ratings> {

    private static final long serialVersionUID = 1879156763L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRatings ratings = new QRatings("ratings");

    public final com.example.movie_review.movieLens.QMovies movie;

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final NumberPath<Long> ratingId = createNumber("ratingId", Long.class);

    public final NumberPath<Long> timestamp = createNumber("timestamp", Long.class);

    public final com.example.movie_review.user.QUser user;

    public QRatings(String variable) {
        this(Ratings.class, forVariable(variable), INITS);
    }

    public QRatings(Path<? extends Ratings> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRatings(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRatings(PathMetadata metadata, PathInits inits) {
        this(Ratings.class, metadata, inits);
    }

    public QRatings(Class<? extends Ratings> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movie = inits.isInitialized("movie") ? new com.example.movie_review.movieLens.QMovies(forProperty("movie")) : null;
        this.user = inits.isInitialized("user") ? new com.example.movie_review.user.QUser(forProperty("user")) : null;
    }

}

