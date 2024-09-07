package com.example.movie_review.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPreferredMovies is a Querydsl query type for PreferredMovies
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPreferredMovies extends EntityPathBase<PreferredMovies> {

    private static final long serialVersionUID = -753294841L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPreferredMovies preferredMovies = new QPreferredMovies("preferredMovies");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath movieId = createString("movieId");

    public final StringPath movieTitle = createString("movieTitle");

    public final QUser user;

    public QPreferredMovies(String variable) {
        this(PreferredMovies.class, forVariable(variable), INITS);
    }

    public QPreferredMovies(Path<? extends PreferredMovies> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPreferredMovies(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPreferredMovies(PathMetadata metadata, PathInits inits) {
        this(PreferredMovies.class, metadata, inits);
    }

    public QPreferredMovies(Class<? extends PreferredMovies> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

