package com.example.movie_review.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "사용자");

//    BRONZE, GOLD, DIAMOND, BLACKLIST;

    private final String key;
    private final String title;
}
