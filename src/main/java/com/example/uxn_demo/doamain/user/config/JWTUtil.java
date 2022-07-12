package com.example.uxn_demo.doamain.user.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.uxn_demo.doamain.user.domain.User;
import com.example.uxn_demo.doamain.user.web.dto.res.TokenVerifyResult;

import java.time.Instant;

public class JWTUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("SpringBoot-JWTToken-Login");
    private static final long AUTH_TIME = 20*60; // 20분

//    private static final long AUTH_TIME = 2;
    private static final long REFRESH_TIME = 60*60*24*7; // 1주일

//    private static final long REFRESH_TIME = 2;

    // 토큰 생성
    public static String makeAuthToken(User user){
        return JWT.create()
                .withSubject(user.getUserId()) // user_id 만 넣어서 생성
                .withClaim("exp", Instant.now().getEpochSecond()+AUTH_TIME) // 토큰 유효 시간 -> Date 클래스 사용 안하고
                .sign(ALGORITHM);
    }

    // 리프레시 토큰 생성
    public static String makeRefreshToken(User user){
        return JWT.create()
                .withSubject(user.getUserId())
                .withClaim("exp", Instant.now().getEpochSecond()+REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static TokenVerifyResult verify(String token){ // 토큰 유효성 검사

        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return TokenVerifyResult.builder()
                    .success(true) // 유효 하다면 성공
                    .userId(verify.getSubject()) // 누가 요청했는지
                    .build();
        }catch (Exception e){
            DecodedJWT decode = JWT.require(ALGORITHM).build().verify(token);
            return TokenVerifyResult.builder()
                    .success(false) // 유효 하지 않다면 실패
                    .userId(decode.getSubject())
                    .build();
        }

    }

}
