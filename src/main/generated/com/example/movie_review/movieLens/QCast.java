package com.example.movie_review.movieLens;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.movie_review.movieDetail.domain.Cast;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCast is a Querydsl query type for Cast
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCast extends EntityPathBase<Cast> {

    private static final long serialVersionUID = 685087603L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCast cast = new QCast("cast");

    public final BooleanPath adult = createBoolean("adult");

    public final NumberPath<Long> cast_id = createNumber("cast_id", Long.class);

    public final StringPath character_name = createString("character_name");

    public final StringPath credit_id = createString("credit_id");

    public final QCredits credits;

    public final NumberPath<Integer> gender = createNumber("gender", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath known_for_department = createString("known_for_department");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> order_number = createNumber("order_number", Integer.class);

    public final StringPath original_name = createString("original_name");

    public final NumberPath<Double> popularity = createNumber("popularity", Double.class);

    public final StringPath profile_path = createString("profile_path");

    public final NumberPath<Integer> tId = createNumber("tId", Integer.class);

    public QCast(String variable) {
        this(Cast.class, forVariable(variable), INITS);
    }

    public QCast(Path<? extends Cast> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCast(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCast(PathMetadata metadata, PathInits inits) {
        this(Cast.class, metadata, inits);
    }

    public QCast(Class<? extends Cast> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.credits = inits.isInitialized("credits") ? new QCredits(forProperty("credits"), inits.get("credits")) : null;
    }

}

