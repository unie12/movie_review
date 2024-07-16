package com.example.movie_review.movieLens;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.movie_review.movieDetail.MovieGenre;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovieGenre is a Querydsl query type for MovieGenre
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovieGenre extends EntityPathBase<MovieGenre> {

    private static final long serialVersionUID = -416349849L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovieGenre movieGenre = new QMovieGenre("movieGenre");

    public final com.example.movie_review.genre.QGenres genres;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMovieDetails movieDetails;

    public QMovieGenre(String variable) {
        this(MovieGenre.class, forVariable(variable), INITS);
    }

    public QMovieGenre(Path<? extends MovieGenre> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovieGenre(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovieGenre(PathMetadata metadata, PathInits inits) {
        this(MovieGenre.class, metadata, inits);
    }

    public QMovieGenre(Class<? extends MovieGenre> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.genres = inits.isInitialized("genres") ? new com.example.movie_review.genre.QGenres(forProperty("genres")) : null;
        this.movieDetails = inits.isInitialized("movieDetails") ? new QMovieDetails(forProperty("movieDetails"), inits.get("movieDetails")) : null;
    }

}

