package com.example.movie_review.dbRating;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDbRatings is a Querydsl query type for DbRatings
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDbRatings extends EntityPathBase<DbRatings> {

    private static final long serialVersionUID = -1099367525L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDbRatings dbRatings = new QDbRatings("dbRatings");

    public final com.example.movie_review.dbMovie.QDbMovies dbMovies;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public final DateTimePath<java.time.LocalDateTime> uploadRating = createDateTime("uploadRating", java.time.LocalDateTime.class);

    public final com.example.movie_review.user.domain.QUser user;

    public QDbRatings(String variable) {
        this(DbRatings.class, forVariable(variable), INITS);
    }

    public QDbRatings(Path<? extends DbRatings> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDbRatings(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDbRatings(PathMetadata metadata, PathInits inits) {
        this(DbRatings.class, metadata, inits);
    }

    public QDbRatings(Class<? extends DbRatings> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dbMovies = inits.isInitialized("dbMovies") ? new com.example.movie_review.dbMovie.QDbMovies(forProperty("dbMovies"), inits.get("dbMovies")) : null;
        this.user = inits.isInitialized("user") ? new com.example.movie_review.user.domain.QUser(forProperty("user")) : null;
    }

}

