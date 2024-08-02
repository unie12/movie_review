package com.example.movie_review.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 2068507384L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final ListPath<com.example.movie_review.comment.Comment, com.example.movie_review.comment.QComment> comments = this.<com.example.movie_review.comment.Comment, com.example.movie_review.comment.QComment>createList("comments", com.example.movie_review.comment.Comment.class, com.example.movie_review.comment.QComment.class, PathInits.DIRECT2);

    public final StringPath context = createString("context");

    public final com.example.movie_review.dbMovie.QDbMovies dbMovies;

    public final ListPath<com.example.movie_review.heart.Heart, com.example.movie_review.heart.QHeart> hearts = this.<com.example.movie_review.heart.Heart, com.example.movie_review.heart.QHeart>createList("hearts", com.example.movie_review.heart.Heart.class, com.example.movie_review.heart.QHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> uploadDate = createDateTime("uploadDate", java.time.LocalDateTime.class);

    public final com.example.movie_review.user.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dbMovies = inits.isInitialized("dbMovies") ? new com.example.movie_review.dbMovie.QDbMovies(forProperty("dbMovies"), inits.get("dbMovies")) : null;
        this.user = inits.isInitialized("user") ? new com.example.movie_review.user.QUser(forProperty("user")) : null;
    }

}

