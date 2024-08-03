package com.example.movie_review.movieLens;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.movie_review.movieDetail.domain.Cast;
import com.example.movie_review.movieDetail.domain.Credits;
import com.example.movie_review.movieDetail.domain.Crew;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCredits is a Querydsl query type for Credits
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCredits extends EntityPathBase<Credits> {

    private static final long serialVersionUID = 233584614L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCredits credits = new QCredits("credits");

    public final ListPath<Cast, QCast> cast = this.<Cast, QCast>createList("cast", Cast.class, QCast.class, PathInits.DIRECT2);

    public final ListPath<Crew, QCrew> crew = this.<Crew, QCrew>createList("crew", Crew.class, QCrew.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMovieDetails movieDetails;

    public QCredits(String variable) {
        this(Credits.class, forVariable(variable), INITS);
    }

    public QCredits(Path<? extends Credits> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCredits(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCredits(PathMetadata metadata, PathInits inits) {
        this(Credits.class, metadata, inits);
    }

    public QCredits(Class<? extends Credits> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.movieDetails = inits.isInitialized("movieDetails") ? new QMovieDetails(forProperty("movieDetails"), inits.get("movieDetails")) : null;
    }

}

