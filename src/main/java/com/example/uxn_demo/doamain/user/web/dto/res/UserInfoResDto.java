package com.example.uxn_demo.doamain.user.web.dto.res;

import com.example.uxn_demo.doamain.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class UserInfoResDto {

    private String name;

    private String email;

    public UserInfoResDto(User entity){
        this.name = entity.getUsername();

        this.email = entity.getEmail();
    }

}
