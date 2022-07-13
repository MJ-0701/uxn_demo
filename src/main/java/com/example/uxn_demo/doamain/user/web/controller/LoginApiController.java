package com.example.uxn_demo.doamain.user.web.controller;

import com.example.uxn_demo.doamain.user.config.jwt.oauth.JWTUtil;
import com.example.uxn_demo.doamain.user.config.jwt.okta.JwtTokenProvider;
import com.example.uxn_demo.doamain.user.domain.User;
import com.example.uxn_demo.doamain.user.service.UserService;
import com.example.uxn_demo.doamain.user.web.dto.req.UserLoginReqDto;
import com.example.uxn_demo.doamain.user.web.dto.res.UserTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/login")
@RequiredArgsConstructor
public class LoginApiController {
    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/web-login")
    public ResponseEntity<User> webLogin(@RequestBody UserLoginReqDto loginReqDto){
        return ResponseEntity.ok(userService.findByUserIdAndPassword(loginReqDto.getUserId(), loginReqDto.getPassword()));
    }

    @PostMapping("/mobile-login")
    public ResponseEntity<User> mobileLogin(@RequestBody UserLoginReqDto loginReqDto){
        return ResponseEntity.ok(userService.findByUserIdAndPassword(loginReqDto.getUserId(), loginReqDto.getPassword())); // local storage 에 저장
    }


    @PostMapping("/make-token")
    public ResponseEntity<String> makeToken(@RequestBody UserLoginReqDto loginReqDto){

        User user = userService.findByUserIdAndPassword(loginReqDto.getUserId(), loginReqDto.getPassword());
        String authToken = JWTUtil.makeAuthToken(user);

        return ResponseEntity.ok().body(authToken); // 토큰 직접 발급
    }

    @PostMapping("/generate-token")
    public ResponseEntity<UserTokenInfo> generateToken(@RequestBody UserLoginReqDto loginReqDto){
        User user = userService.findByUserIdAndPassword(loginReqDto.getUserId(), loginReqDto.getPassword());
        return ResponseEntity.ok().body(UserTokenInfo
                .builder()
                .authToken(JWTUtil.makeAuthToken(user))
                .refreshToken(JWTUtil.makeRefreshToken(user))
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<UserTokenInfo> refreshToken(@RequestBody UserTokenInfo tokenInfo){

        return ResponseEntity.ok().body(UserTokenInfo
                .builder()
                .refreshToken(tokenInfo.getRefreshToken())
                .build());


    }

    @PostMapping("/make-auth-token")
    public ResponseEntity<String> login(@RequestBody UserLoginReqDto reqDto){
        User user = userService.findByUserIdAndPassword(reqDto.getUserId(), reqDto.getPassword());
        return ResponseEntity.ok(jwtTokenProvider.createToken(user.getUserId(), user.getRole()));
    }
}
