package com.example.uxn_demo.doamain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자");
//    GUEST("ROLE_GUEST", "비회원");


    private final String key;

    private final String value;
}
