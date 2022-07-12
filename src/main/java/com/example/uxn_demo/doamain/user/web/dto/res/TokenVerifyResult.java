package com.example.uxn_demo.doamain.user.web.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVerifyResult {

    private boolean success;

    @JsonProperty("user_id")
    private String userId;
}
