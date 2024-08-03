package com.example.movie_review.movieLens;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.movie_review.movieDetail.domain.MovieDetails;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMovieDetails is a Querydsl query type for MovieDetails
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMovieDetails extends EntityPathBase<MovieDetails> {

    private static final long serialVersionUID = 957251878L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMovieDetails movieDetails = new QMovieDetails("movieDetails");

    public final BooleanPath adult = createBoolean("adult");

    public final StringPath backdrop_path = createString("backdrop_path");

    public final QCredits credits;

    public final com.example.movie_review.dbMovie.QDbMovies dbMovie;

    public final StringPath first_air_date = createString("first_air_date");

    public final SetPath<com.example.movie_review.genre.Genres, com.example.movie_review.genre.QGenres> genres = this.<com.example.movie_review.genre.Genres, com.example.movie_review.genre.QGenres>createSet("genres", com.example.movie_review.genre.Genres.class, com.example.movie_review.genre.QGenres.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath media_type = createString("media_type");

    public final StringPath name = createString("name");

    public final StringPath original_title = createString("original_title");

    public final StringPath overview = createString("overview");

    public final StringPath poster_path = createString("poster_path");

    public final StringPath release_date = createString("release_date");

    public final NumberPath<Long> runtime = createNumber("runtime", Long.class);

    public final StringPath tagline = createString("tagline");

    public final NumberPath<Integer> tId = createNumber("tId", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Double> vote_average = createNumber("vote_average", Double.class);

    public final NumberPath<Long> vote_count = createNumber("vote_count", Long.class);

    public QMovieDetails(String variable) {
        this(MovieDetails.class, forVariable(variable), INITS);
    }

    public QMovieDetails(Path<? extends MovieDetails> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMovieDetails(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMovieDetails(PathMetadata metadata, PathInits inits) {
        this(MovieDetails.class, metadata, inits);
    }

    public QMovieDetails(Class<? extends MovieDetails> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.credits = inits.isInitialized("credits") ? new QCredits(forProperty("credits"), inits.get("credits")) : null;
        this.dbMovie = inits.isInitialized("dbMovie") ? new com.example.movie_review.dbMovie.QDbMovies(forProperty("dbMovie"), inits.get("dbMovie")) : null;
    }

}

