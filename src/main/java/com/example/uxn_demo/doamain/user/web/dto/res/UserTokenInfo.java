package com.example.uxn_demo.doamain.user.web.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class UserTokenInfo {

    @JsonProperty("refresh_token")
    private String refreshToken;
}
