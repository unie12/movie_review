package com.example.movie_review.domain.DTO;

import com.example.movie_review.domain.User;
import com.example.movie_review.domain.ENUM.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class JoinRequest {

    @NotBlank(message =  "아이디가 비어있습니다.")
    private String loginId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임이 비어있습니다")
    private String nickname;

    /**
     * 비밀번호 암호화 X
     */
    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .receivedHeartCnt(0)
                .build();
    }

    /**
     * 비밀번호 암호화
     */
    public User toEntity(String encodedPassword) {
        return User.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .nickname(this.nickname)
                .role(UserRole.USER)
                .build();
    }
}
