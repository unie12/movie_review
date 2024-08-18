package com.example.movie_review.dbMovie.repository;

import com.example.movie_review.dbMovie.DTO.MoviePopularityDTO;
import com.example.movie_review.dbMovie.QDbMovies;
import com.example.movie_review.dbRating.QDbRatings;
import com.example.movie_review.favoriteMovie.QUserFavoriteMovie;
import com.example.movie_review.movieDetail.domain.QMovieDetails;
import com.example.movie_review.review.QReview;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DbMovieRepositoryImpl implements DbMovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DbMovieRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<MoviePopularityDTO> findAjouPopularMovies(LocalDateTime startDate, double minRating) {
        QDbMovies m = QDbMovies.dbMovies;
        QMovieDetails md = QMovieDetails.movieDetails;
        QUserFavoriteMovie f = QUserFavoriteMovie.userFavoriteMovie;
        QDbRatings r = QDbRatings.dbRatings;
        QReview rev = QReview.review;

        NumberExpression<Double> avgRating = r.score.avg().coalesce(0.0); // rating 값 가져와서 평균내는데 아무값 없는애 0.0으로
        NumberExpression<Long> ratingCount = r.count(); // 총 레이팅 수

        return queryFactory
                // MoviePopularityDTO 객체 생성
                .select(Projections.constructor(MoviePopularityDTO.class,
                        md.tId,
                        md.poster_path,
                        md.title,
                        f.favoriteDate.goe(startDate).count(),
                        r.uploadRating.goe(startDate).count(),
                        rev.uploadDate.goe(startDate).count(),
                        avgRating,
                        ratingCount))
                // 테이블 간의 조인 정의, DbMovies를 기준으로 조인 조건에 따라(startDate)
                .from(m)
                .join(m.movieDetails, md)
                .leftJoin(m.favoritedByUsers, f).on(f.favoriteDate.goe(startDate))
                .leftJoin(m.dbRatings, r).on(r.uploadRating.goe(startDate))
                .leftJoin(m.reviews, rev).on(rev.uploadDate.goe(startDate))
                // 결과를 세 가지 변수로 그룹화
                .groupBy(m.id, md.poster_path, md.title)
                // 평균 평점이 minRatung 이상이고 최근 활동인 경우
                .having(avgRating.goe(minRating)
                        .and(f.favoriteDate.goe(startDate).count()
                                .add(r.count())
                                .add(rev.uploadDate.goe(startDate).count()).gt(0)))
                // 최근 활동 총합을 기준으로 내림차순
                .orderBy(f.favoriteDate.goe(startDate).count()
                        .add(r.count())
                        .add(rev.uploadDate.goe(startDate).count()).desc())
                .limit(20)
                .fetch();
    }
}
