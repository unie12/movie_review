package com.example.movie_review.controller.login;

import com.example.movie_review.domain.DTO.JoinRequest;
import com.example.movie_review.domain.DTO.LoginRequest;
import com.example.movie_review.user.User;
import com.example.movie_review.user.UserRole;
import com.example.movie_review.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

/**
 * 쿠키 로그인
 * 문제점: 쿠키에 유저 정보가 담겨 있어 보안에 매우 취약함
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/cookie-login")
public class CookieLoginController {

    private final UserService userService;

    /**
     * 초기 화면
     */
    @GetMapping({"", "/"})
    public String home(@CookieValue(name = "userId", required = false) Long userId, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        User loginUserById = userService.getLoginUserById(userId);

        if(loginUserById != null){
            model.addAttribute("nickname", loginUserById.getNickname());
        }
        return "home";
    }

    /**
     * 회원 가입
     */
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    /**
     * 중복 체크
     */
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        // loginId 중복
        if(userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loignId", "로그인 아이디가 중복됩니다."));
        }

        // 닉네임 중복
        if(userService.checkLoginIdDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }

        // password, passwordCheck 동일 체크
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()) {
            return "join";
        }

        userService.join(joinRequest);
        return "redirect:/cookie-login";
    }

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    /**
     * 로그인
     * 아이디 비밀번호 일치 확인
     * 쿠키 생성
     */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
                        HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        User user = userService.login(loginRequest);

        if(user == null) {
            bindingResult.reject("loginFall", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if(bindingResult.hasErrors()) {
            return "login";
        }

        Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);

        return "redirect:/cookie-login";
    }

    /**
     * 로그아웃
     * 쿠키 삭제
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/cookie-login";
    }

    /**
     * 마이 페이지
     */
    @GetMapping("/info")
    public String userInfo(@CookieValue(name = "userId", required = false) Long userId, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        User loginUserById = userService.getLoginUserById(userId);

        if(loginUserById == null) {
            return "redirect:/cookie-login/login";
        }

        model.addAttribute("user", loginUserById);
        return "info";
    }

    /**
     * 관리자 권한 실행
     */
    @GetMapping("/admin")
    public String adminPage(@CookieValue(name = "userId", required = false) Long userId, Model model) {
        model.addAttribute("loginType", "cookie-login");
        model.addAttribute("pageName", "쿠키 로그인");

        User loginUserById = userService.getLoginUserById(userId);

        if(loginUserById == null) {
            return "redirect:/cookie-login/login";
        }

        if(!loginUserById.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/cookie-login";
        }

        return "admin";
    }
}
