package com.example.movie_review.domain.ENUM;

public enum ReviewCategory {
    FREE, AFTERSCENE, BEFORESCENE, NOTICE;

    public static ReviewCategory of(String category) {
        if (category.equalsIgnoreCase("free")) return ReviewCategory.FREE;
        else if(category.equalsIgnoreCase("afterscene")) return ReviewCategory.AFTERSCENE;
        else if(category.equalsIgnoreCase("beforescene")) return ReviewCategory.BEFORESCENE;
        else if(category.equalsIgnoreCase("notice")) return ReviewCategory.NOTICE;
        else return null;
    }
}
