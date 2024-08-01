package com.example.movie_review.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMIN", "관리자", "#CCCCFF"),
    USER("ROLE_USER", "사용자", "#000000"),
    BRONZE("ROLE_BRONZE", "브론즈", "#000000"), // 검정
    SILVER("ROLE_SILVER", "실버", "#797d7f"), // 실버
    EMERALD("ROLE_EMERALD", "에메랄드", "#58D68D"), // 초록
    MASTER("ROLE_MASTER", "마스터", "#f8f06a"), // 노랑
    CHALLENGER("ROLE_CHALLENGER", "챌린저", "#6af8f0"); // 파랑

    private final String key;
    private final String title;
    private final String color;
}
