package com.example.movie_review.dbMovie;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMovieCache is a Querydsl query type for MovieCache
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovieCache extends EntityPathBase<MovieCache> {

    private static final long serialVersionUID = -151540092L;

    public static final QMovieCache movieCache = new QMovieCache("movieCache");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lastUpdated = createDateTime("lastUpdated", java.time.LocalDateTime.class);

    public final StringPath movieData = createString("movieData");

    public final EnumPath<MovieType> type = createEnum("type", MovieType.class);

    public QMovieCache(String variable) {
        super(MovieCache.class, forVariable(variable));
    }

    public QMovieCache(Path<? extends MovieCache> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMovieCache(PathMetadata metadata) {
        super(MovieCache.class, metadata);
    }

}

