package com.example.movie_review.favoriteMovie;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserFavoriteMovie is a Querydsl query type for UserFavoriteMovie
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserFavoriteMovie extends EntityPathBase<UserFavoriteMovie> {

    private static final long serialVersionUID = -488720903L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserFavoriteMovie userFavoriteMovie = new QUserFavoriteMovie("userFavoriteMovie");

    public final com.example.movie_review.dbMovie.QDbMovies dbMovie;

    public final DateTimePath<java.time.LocalDateTime> favoriteDate = createDateTime("favoriteDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.movie_review.user.QUser user;

    public QUserFavoriteMovie(String variable) {
        this(UserFavoriteMovie.class, forVariable(variable), INITS);
    }

    public QUserFavoriteMovie(Path<? extends UserFavoriteMovie> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserFavoriteMovie(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserFavoriteMovie(PathMetadata metadata, PathInits inits) {
        this(UserFavoriteMovie.class, metadata, inits);
    }

    public QUserFavoriteMovie(Class<? extends UserFavoriteMovie> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dbMovie = inits.isInitialized("dbMovie") ? new com.example.movie_review.dbMovie.QDbMovies(forProperty("dbMovie"), inits.get("dbMovie")) : null;
        this.user = inits.isInitialized("user") ? new com.example.movie_review.user.QUser(forProperty("user")) : null;
    }

}

