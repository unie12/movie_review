package com.example.movie_review.genre;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPreferredGenres is a Querydsl query type for PreferredGenres
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPreferredGenres extends EntityPathBase<PreferredGenres> {

    private static final long serialVersionUID = 1660237520L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPreferredGenres preferredGenres = new QPreferredGenres("preferredGenres");

    public final NumberPath<Long> genreId = createNumber("genreId", Long.class);

    public final StringPath genreName = createString("genreName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.movie_review.user.QUser user;

    public QPreferredGenres(String variable) {
        this(PreferredGenres.class, forVariable(variable), INITS);
    }

    public QPreferredGenres(Path<? extends PreferredGenres> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPreferredGenres(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPreferredGenres(PathMetadata metadata, PathInits inits) {
        this(PreferredGenres.class, metadata, inits);
    }

    public QPreferredGenres(Class<? extends PreferredGenres> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.movie_review.user.QUser(forProperty("user")) : null;
    }

}

