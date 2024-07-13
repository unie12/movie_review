package com.example.movie_review.heart;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHeart is a Querydsl query type for Heart
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHeart extends EntityPathBase<Heart> {

    private static final long serialVersionUID = 362471208L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHeart heart = new QHeart("heart");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.movie_review.review.QReview review;

    public final com.example.movie_review.user.QUser user;

    public QHeart(String variable) {
        this(Heart.class, forVariable(variable), INITS);
    }

    public QHeart(Path<? extends Heart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHeart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHeart(PathMetadata metadata, PathInits inits) {
        this(Heart.class, metadata, inits);
    }

    public QHeart(Class<? extends Heart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new com.example.movie_review.review.QReview(forProperty("review"), inits.get("review")) : null;
        this.user = inits.isInitialized("user") ? new com.example.movie_review.user.QUser(forProperty("user")) : null;
    }

}

