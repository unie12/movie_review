package com.example.movie_review.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.awt.*;
import java.util.Date;

/**
 * Jwt token 방식을 사용할 떄 필요한 기능들
 *
 * 새로운 Jwt Token 발급
 * Jwt Token Claim에서 LoginId 꺼내기
 * 만료 시간 체크
 *
 */
public class JwtTokenUtil {

    // JWT token 발급
    public static String createToken(String loginId, String key, long expireTimeMs) {
        // claim = jwt token에 들어갈 정보

        Claims claims = Jwts.claims();
        claims.put("loginId", loginId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // claims에서 loginid 꺼내기
    public static String getLoginId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("loginId").toString();
    }

    // 발급되s token 의 만료 시간 체크
    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    // secretKey를 사용해 token parsing
    private static Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
