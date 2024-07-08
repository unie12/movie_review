package com.example.movie_review.domain.review;

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

    private static final long serialVersionUID = -1023436870L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.example.movie_review.domain.QBaseEntity _super = new com.example.movie_review.domain.QBaseEntity(this);

    public final NumberPath<Integer> commentCnt = createNumber("commentCnt", Integer.class);

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath context = createString("context");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Integer> heartCnt = createNumber("heartCnt", Integer.class);

    public final ListPath<Heart, QHeart> hearts = this.<Heart, QHeart>createList("hearts", Heart.class, QHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final QReviewImage reviewImage;

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> uploadDate = createDateTime("uploadDate", java.time.LocalDateTime.class);

    public final com.example.movie_review.user.QUser user;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public final StringPath writer = createString("writer");

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
        this.reviewImage = inits.isInitialized("reviewImage") ? new QReviewImage(forProperty("reviewImage"), inits.get("reviewImage")) : null;
        this.user = inits.isInitialized("user") ? new com.example.movie_review.user.QUser(forProperty("user")) : null;
    }

}

