package com.example.uxn_demo.doamain.user.web.controller;

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

    @PostMapping("/web-login")
    public ResponseEntity<User> webLogin(@RequestBody UserLoginReqDto loginReqDto){
        return ResponseEntity.ok(userService.findByUserIdAndPassword(loginReqDto.getUserId(), loginReqDto.getPassword()));
    }

    @PostMapping("/mobile-login")
    public ResponseEntity<User> mobileLogin(@RequestBody UserLoginReqDto loginReqDto){
        return ResponseEntity.ok(userService.findByUserIdAndPassword(loginReqDto.getUserId(), loginReqDto.getPassword()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody UserTokenInfo userTokenInfo){
        String refreshToken = userTokenInfo.getRefreshToken();
        return ResponseEntity.ok(refreshToken);

    }
}
