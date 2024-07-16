//package com.example.movie_review;
//
//import com.example.movie_review.oauth.SessionUser;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class HomeController {
////    @RequestMapping("/")
////    public String home() {
////        return "home";
////    }
//
//    private final HttpSession httpSession;
//
//    @GetMapping("/")
//    public String postList(Pageable pageable, Model model) {
//        // 세션에서 사용자 정보 꺼내기
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//        if (user != null) {
////            model.addAttribute("userName", user.getName());
//            model.addAttribute("userName", user.getName());
//        }
//        return "posts/list";
//    }
//
//}
