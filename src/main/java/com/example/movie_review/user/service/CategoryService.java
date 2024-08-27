package com.example.movie_review.user.service;

import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    public String getCategoryTitle(String category) {
        switch (category) {
            case "favorite": return "이 찜한 영화";
            case "heart": return "이 좋아요 누른 리뷰";
            case "rating": return "이 평점 매긴 영화";
            case "review": return "이 리뷰 남긴 영화";
            case "subscriber": return "을 구독한 사용자";
            case "subscription": return "이 구독한 사용자";
            default:
                return "category";
        }
    }

    public String getEmptyMessage(String category) {
        switch (category) {
            case "favorite": return "찜한 영화가 없습니다!";
            case "heart": return "좋아요 누른 리뷰가 없습니다!";
            case "rating": return "평점 매긴 영화가 없습니다!";
            case "review": return "리뷰 매긴 영화가 없습니다!";
            case "subscriber": return "나를 구독한 사용자가 없습니다!";
            case "subscription": return "내가 구독한 사용자가 없습니다!";
            default:
                return category;
        }
    }

    public String getDefaultSort(String category) {
        switch (category) {
            case "favorite": return "favorite_date_desc";
            case "heart": return "heart_date_desc";
            case "rating": return "rating_date_desc";
            case "review": return "review_date_desc";
            case "subscriber": return "subscriber_date_desc";
            case "subscription": return "subscription_date_desc";
            default:
                return category;
        }
    }

}
