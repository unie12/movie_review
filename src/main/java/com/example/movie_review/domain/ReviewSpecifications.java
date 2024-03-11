package com.example.movie_review.domain;

import com.example.movie_review.domain.review.Review;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecifications {
    public static Specification<Review> hasKeyword(String keyword, String searchType) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.isTrue(cb.literal(true));
            } else {
                return switch (searchType) {
                    case "title" -> cb.like(root.get("title"), "%" + keyword + "%");
                    case "content" -> cb.like(root.get("content"), "%" + keyword + "%");
                    case "nickname" -> cb.like(root.join("user").get("nickname"), "%" + keyword + "%");
                    default ->
                        // 검색 유형이 지원되지 않는 경우, 빈 결과를 반환
                            cb.conjunction();
                };
            }
        };
    }
}
