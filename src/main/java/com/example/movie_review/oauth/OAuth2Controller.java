package com.example.movie_review.oauth;

import com.example.movie_review.auth.JwtTokenUtil;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {

    private final UserService userService;

    @GetMapping("/oauth2-login")
    public String oauth2LoginSuccess(Authentication auth, HttpServletResponse response, Model model) {
        OAuth2User oAuth2User = (OAuth2User) auth.getPrincipal();
        String email = oAuth2User.getAttribute("email"); // 구글의 기본 식별자는 이메일입니다.

        Optional<User> userOptional = userService.getOptUserByEmail(email);

        if (userOptional.isEmpty()) {
            // 사용자가 데이터베이스에 존재하지 않는 경우
            // 로그인 실패 처리 또는 에러 페이지로 리다이렉트
            return "redirect:/login-failure";
        }

        User user = userOptional.get();
        // 기존 사용자 객체를 사용하여 추가 정보가 유지되도록 합니다.

        // 로그인 성공 -> Jwt 토큰 발급
        String secretKey = "my-secret-key-123123";
        long expireTimeMs = 1000 * 60 * 60;

        String jwtToken = JwtTokenUtil.createToken(user.getEmail(), secretKey, expireTimeMs);

        // 발급한 토큰을 쿠키를 통해 전송
        Cookie cookie = new Cookie("jwtToken", jwtToken);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return "redirect:/jwt-login";
    }
}
