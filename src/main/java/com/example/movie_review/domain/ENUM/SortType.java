package com.example.movie_review.domain.ENUM;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
public enum SortType {

    GOOD_ASC("좋아요 많은순"),
    GOOD_DESC("좋아요 적은순"),

    DATE_ASC("최근 등록순"),
    DATE_DESC("나중 등록순"),

    SCORE_ASC("평점 높은순"),
    SCORE_DESC("평점 낮은순"),

    VIEW_ASC("조회수 많은순"),
    VIEW_DESC("조회수 적은순");

    private final String description;

    public static Sort getSort(String description) {
        return switch (description) {
            case "좋아요 많은순" -> Sort.by(Sort.Order.desc("heartCnt"), Sort.Order.desc("uploadDate"));
            case "좋아요 적은순" -> Sort.by(Sort.Order.asc("heartCnt"), Sort.Order.desc("uploadDate"));

//            case "좋아요 적은순" -> Sort.by("good").ascending();
            case "최근 등록순" -> Sort.by("uploadDate").descending();
            case "나중 등록순" -> Sort.by("uploadDate").ascending();

            case "평점 높은순" -> Sort.by(Sort.Order.desc("score"), Sort.Order.desc("uploadDate"));
            case "평점 낮은순" -> Sort.by(Sort.Order.asc("score"), Sort.Order.desc("uploadDate"));
//            case "평점 높은순" -> Sort.by("score").descending();
//            case "평점 낮은순" -> Sort.by("score").ascending();

            case "조회수 많은순" -> Sort.by(Sort.Order.desc("viewCount"), Sort.Order.desc("uploadDate"));
            case "조회수 적은순" -> Sort.by(Sort.Order.asc("viewCount"), Sort.Order.desc("uploadDate"));
            default -> Sort.by("uploadDate").descending();
        };
    }
}
