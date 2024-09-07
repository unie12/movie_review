package com.example.movie_review.subscription;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubscription is a Querydsl query type for Subscription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscription extends EntityPathBase<Subscription> {

    private static final long serialVersionUID = -1525689448L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubscription subscription = new QSubscription("subscription");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.movie_review.user.domain.QUser subscribed;

    public final com.example.movie_review.user.domain.QUser subscriber;

    public final DateTimePath<java.time.LocalDateTime> subscriptionDate = createDateTime("subscriptionDate", java.time.LocalDateTime.class);

    public QSubscription(String variable) {
        this(Subscription.class, forVariable(variable), INITS);
    }

    public QSubscription(Path<? extends Subscription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubscription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubscription(PathMetadata metadata, PathInits inits) {
        this(Subscription.class, metadata, inits);
    }

    public QSubscription(Class<? extends Subscription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.subscribed = inits.isInitialized("subscribed") ? new com.example.movie_review.user.domain.QUser(forProperty("subscribed")) : null;
        this.subscriber = inits.isInitialized("subscriber") ? new com.example.movie_review.user.domain.QUser(forProperty("subscriber")) : null;
    }

}

