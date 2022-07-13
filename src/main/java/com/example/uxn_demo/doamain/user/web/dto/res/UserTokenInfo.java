package com.example.uxn_demo.doamain.user.web.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTokenInfo {

    @JsonProperty("auth_token")
    private String authToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
