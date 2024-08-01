package com.example.movie_review.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMIN", "관리자", "#CCCCFF"),
    USER("ROLE_USER", "사용자", "#000000"),
    BRONZE("ROLE_BRONZE", "브론즈", "#875858"),
    SILVER("ROLE_SILVER", "실버", "#BFC9CA"),
    EMERALD("ROLE_EMERALD", "에메랄드", "#58D68D"),
    MASTER("ROLE_MASTER", "마스터", "#A569BD"),
    CHALLENGER("ROLE_CHALLENGER", "챌린저", "#5FD0E8");

    private final String key;
    private final String title;
    private final String color;
}
