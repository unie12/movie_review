package com.example.movie_review.oauth;

import com.example.movie_review.auth.JwtTokenUtil;
import com.example.movie_review.user.domain.User;
import com.example.movie_review.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {

    private final UserService userService;

    @GetMapping("/oauth2-login")
    public String oauth2LoginSuccess(Authentication auth, HttpServletRequest request, HttpServletResponse response, Model model) {
        OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
        String email = oAuth2User.getAttribute("email"); // 구글의 기본 식별자는 이메일입니다.

        User user = userService.getUserByEmail(email);
        // 새 사용자
        if (user.getNickname().startsWith("temp_")) {
            // 사용자가 데이터베이스에 존재하지 않는 경우
            return "redirect:/additional-info/" + email;
        }

        // 로그인 성공 -> Jwt 토큰 발급
        String secretKey = "my-secret-key-123123";
        String keepLoggedIn = request.getParameter("keepLoggedIn");
        long expireTimeMs = "true".equals(keepLoggedIn) ? 1000 * 60 * 60 * 24 * 30 : 1000 * 60 * 60 * 4; // 30일 또는 4시간
//        long expireTimeMs = 1000 * 60 * 60;

        String jwtToken = JwtTokenUtil.createToken(user.getEmail(), secretKey, expireTimeMs);

        // 발급한 토큰을 쿠키를 통해 전송
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setPath("/");
        cookie.setMaxAge((int) (expireTimeMs / 1000));
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return "redirect:/jwt-login";
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        request.getSession().invalidate();

        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setPath("/");
        jsessionCookie.setMaxAge(0);
        jsessionCookie.setHttpOnly(true);
        response.addCookie(jsessionCookie);

        response.sendRedirect("/jwt-login");
        return ResponseEntity.ok().body("Logged out successfully");
    }
}
