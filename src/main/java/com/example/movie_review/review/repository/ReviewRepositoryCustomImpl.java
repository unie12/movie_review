package com.example.movie_review.review.repository;

import com.example.movie_review.review.DTO.ReviewCommonDTO;
import com.example.movie_review.review.DTO.ReviewDTO;
import com.example.movie_review.user.DTO.UserCommonDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.movie_review.review.QReview.review;
import static com.example.movie_review.user.domain.QUser.user;

public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ReviewDTO> findReviewByMovieId(Long movieId, Pageable pageable, String sortBy, Sort.Direction direction) {
        JPAQuery<ReviewDTO> query = queryFactory
                .select(Projections.constructor(ReviewDTO.class,
                        Expressions.asNumber(0.0).as("userRating"), // userRating은 나중에 설정
                        review.hearts.size().as("heartCnt"),
                        Projections.constructor(UserCommonDTO.class,
                                user.id,
                                user.email,
                                user.nickname,
                                user.picture,
                                user.role),
                        Projections.constructor(ReviewCommonDTO.class,
                                review.id,
                                review.context),
                        Expressions.asBoolean(false).as("isLikedByCurrentUser"))) // isLikedByCurrentUser는 나중에 설정
                .from(review)
                .leftJoin(review.user, user)
                .where(review.dbMovies.movieDetails.id.eq(movieId));

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, direction);
        if (orderSpecifier != null) {
            query.orderBy(orderSpecifier);
        }

        List<ReviewDTO> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .where(review.dbMovies.movieDetails.id.eq(movieId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, Sort.Direction direction) {
        switch (sortBy) {
            case "heartCount":
                return direction == Sort.Direction.ASC ? review.hearts.size().asc() : review.hearts.size().desc();
            case "createdDate":
                return direction == Sort.Direction.ASC ? review.uploadDate.asc() : review.uploadDate.desc();
            default:
                return review.id.desc(); // 기본 정렬
        }
    }

}
