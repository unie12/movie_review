package com.example.movie_review.movieLens;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.movie_review.movieDetail.Crew;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCrew is a Querydsl query type for Crew
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCrew extends EntityPathBase<Crew> {

    private static final long serialVersionUID = 685103509L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCrew crew = new QCrew("crew");

    public final BooleanPath adult = createBoolean("adult");

    public final StringPath credit_id = createString("credit_id");

    public final QCredits credits;

    public final NumberPath<Long> crew_id = createNumber("crew_id", Long.class);

    public final StringPath department = createString("department");

    public final NumberPath<Integer> gender = createNumber("gender", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath job = createString("job");

    public final StringPath known_for_department = createString("known_for_department");

    public final StringPath name = createString("name");

    public final StringPath original_name = createString("original_name");

    public final NumberPath<Double> popularity = createNumber("popularity", Double.class);

    public final StringPath profile_path = createString("profile_path");

    public final NumberPath<Integer> tId = createNumber("tId", Integer.class);

    public QCrew(String variable) {
        this(Crew.class, forVariable(variable), INITS);
    }

    public QCrew(Path<? extends Crew> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCrew(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCrew(PathMetadata metadata, PathInits inits) {
        this(Crew.class, metadata, inits);
    }

    public QCrew(Class<? extends Crew> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.credits = inits.isInitialized("credits") ? new QCredits(forProperty("credits"), inits.get("credits")) : null;
    }

}

