package com.example.movie_review.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Securiy 설정 활성화
@Configuration
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보안 설정 사용 안 함
//                .logout().disable() // 로그아웃 사용 안 함
                .formLogin().disable() // 폼 로그인 사용 안 함

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/oauth2/**", "/api/movies/**", "/jwt-login/**", "/jwt-login", "/home", "/login-failure").permitAll()
                        .anyRequest().authenticated() // 나머지 요청들은 모두 인증 절차 수행해야 함
                )

                .oauth2Login(oauth2 -> oauth2 // OAuth2를 통한 로그인 사용
                    .defaultSuccessUrl("/jwt-login", true) // 로그인 성공 시 이동할 URL
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2Service) // 해당 서비스 로직을 타도록 설정
                    )
                    .failureUrl("/login-failure") // 로그인 실패 시 이동할 URL
                )
                .logout(
                        (logout) -> logout
                                .logoutSuccessUrl("/jwt-login")
                                .deleteCookies("JSESSIONID", "jwtToken")
                );

        return http.build();
    }

}