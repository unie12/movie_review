package com.example.movie_review.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -178207400L;

    public static final QUser user = new QUser("user");

    public final NumberPath<Long> age = createNumber("age", Long.class);

    public final ListPath<com.example.movie_review.comment.Comment, com.example.movie_review.comment.QComment> comments = this.<com.example.movie_review.comment.Comment, com.example.movie_review.comment.QComment>createList("comments", com.example.movie_review.comment.Comment.class, com.example.movie_review.comment.QComment.class, PathInits.DIRECT2);

    public final ListPath<com.example.movie_review.dbRating.DbRatings, com.example.movie_review.dbRating.QDbRatings> dbRatings = this.<com.example.movie_review.dbRating.DbRatings, com.example.movie_review.dbRating.QDbRatings>createList("dbRatings", com.example.movie_review.dbRating.DbRatings.class, com.example.movie_review.dbRating.QDbRatings.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final StringPath gender = createString("gender");

    public final ListPath<com.example.movie_review.heart.Heart, com.example.movie_review.heart.QHeart> hearts = this.<com.example.movie_review.heart.Heart, com.example.movie_review.heart.QHeart>createList("hearts", com.example.movie_review.heart.Heart.class, com.example.movie_review.heart.QHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mbti = createString("mbti");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath picture = createString("picture");

    public final ListPath<com.example.movie_review.genre.PreferredGenres, com.example.movie_review.genre.QPreferredGenres> preferredGenres = this.<com.example.movie_review.genre.PreferredGenres, com.example.movie_review.genre.QPreferredGenres>createList("preferredGenres", com.example.movie_review.genre.PreferredGenres.class, com.example.movie_review.genre.QPreferredGenres.class, PathInits.DIRECT2);

    public final ListPath<com.example.movie_review.movie.PreferredMovies, com.example.movie_review.movie.QPreferredMovies> preferredMovies = this.<com.example.movie_review.movie.PreferredMovies, com.example.movie_review.movie.QPreferredMovies>createList("preferredMovies", com.example.movie_review.movie.PreferredMovies.class, com.example.movie_review.movie.QPreferredMovies.class, PathInits.DIRECT2);

    public final ListPath<com.example.movie_review.review.Review, com.example.movie_review.review.QReview> reviews = this.<com.example.movie_review.review.Review, com.example.movie_review.review.QReview>createList("reviews", com.example.movie_review.review.Review.class, com.example.movie_review.review.QReview.class, PathInits.DIRECT2);

    public final EnumPath<UserRole> role = createEnum("role", UserRole.class);

    public final ListPath<com.example.movie_review.favoriteMovie.UserFavoriteMovie, com.example.movie_review.favoriteMovie.QUserFavoriteMovie> userFavoriteMovies = this.<com.example.movie_review.favoriteMovie.UserFavoriteMovie, com.example.movie_review.favoriteMovie.QUserFavoriteMovie>createList("userFavoriteMovies", com.example.movie_review.favoriteMovie.UserFavoriteMovie.class, com.example.movie_review.favoriteMovie.QUserFavoriteMovie.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

