//package com.example.movie_review.controller.login;
//
//import com.example.movie_review.auth.JwtTokenUtil;
//import com.example.movie_review.domain.DTO.JoinRequest;
//import com.example.movie_review.domain.DTO.LoginRequest;
//import com.example.movie_review.user.User;
//import com.example.movie_review.user.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/jwt-api-login")
//public class JwtLoginApiController {
//
//    private final UserService userService;
//
//    @PostMapping("/join")
//    public String join(@RequestBody JoinRequest joinRequest) {
//
//        // loginId 중복 체크
//        if(userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
//            return "로그인 아이디가 중복됩니다.";
//        }
//
//        // 닉네임 중복 체크
//        if(userService.checkNicknameDuplicate(joinRequest.getNickname())) {
//            return "닉네임이 중복됩니다.";
//        }
//
//        // password passwordCheck 동일 확인
//        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
//            return "비밀번호가 일치하지 않습니다.";
//        }
//
//        userService.join2(joinRequest);
//        return "회원가입 성공";
//    }
//
//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequest loginRequest) {
//
//        User user = userService.login(loginRequest);
//
//        // 로그인 아이디 비밀번호 틀린 경우
//        if(user == null) {
//            return "로그인 아이디 또는 비밀번호가 틀렸습니다.";
//        }
//
//        // 로그인 성공 -> Jwt 토큰 발급
//        String secretKey = "my-secret-key-123123";
//        long expireTimeMs = 1000 * 60 * 60;
//
//        String jwtToken = JwtTokenUtil.createToken(user.getLoginId(), secretKey, expireTimeMs);
//
//        return jwtToken;
//    }
//
//    @GetMapping("/info")
//    public String userInfo(Authentication auth) {
//        User loginUSer = userService.getLoginUserByLoginId(auth.getName());
//
//        return String.format("loginId : %s\nnickname : %s\nrole : %s",
//                loginUSer.getLoginId(), loginUSer.getNickname(), loginUSer.getRole().name());
//    }
//
//    @GetMapping("/admin")
//    public String adminPage() {
//        return "관리자 페이지 접근 성공";
//    }
//}
