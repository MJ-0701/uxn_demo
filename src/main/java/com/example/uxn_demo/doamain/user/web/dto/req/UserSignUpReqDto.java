package com.example.uxn_demo.doamain.user.web.dto.req;

import com.example.uxn_demo.doamain.user.domain.Gender;
import com.example.uxn_demo.doamain.user.domain.Role;
import com.example.uxn_demo.doamain.user.domain.User;
import com.example.uxn_demo.doamain.user.domain.UserAuthority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpReqDto {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_name")
    private String userName;

    private String email;

    private String password;

    private Gender gender;

    private String birth;

    private Set<UserAuthority> authorities;

    public User toEntity(){
        return User
                .builder()
                .userId(userId)
                .userName(userName)
                .email(email)
                .password(password)
                .gender(gender)
                .role(Role.USER)
                .birth(birth)
                .enabled(true)
                .authorities(authorities)
                .build();
    }

}
