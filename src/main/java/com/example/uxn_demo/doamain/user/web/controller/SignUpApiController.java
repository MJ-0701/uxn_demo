package com.example.uxn_demo.doamain.user.web.controller;

import com.example.uxn_demo.doamain.user.domain.User;
import com.example.uxn_demo.doamain.user.service.UserService;
import com.example.uxn_demo.doamain.user.web.dto.req.AdminSignUpReqDto;
import com.example.uxn_demo.doamain.user.web.dto.req.UserSignUpReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/signup")
@RequiredArgsConstructor
public class SignUpApiController {

    private final UserService userService;

    @PostMapping("/user/save")
    public ResponseEntity<User> userSave(
            @RequestBody @Valid UserSignUpReqDto reqDto
            ){
       User user = userService.saveUser(reqDto.toEntity());
       userService.addAuthority(user.getIdx(), "ROLE_USER");

        return ResponseEntity.ok(user);
    }

    @PostMapping("/admin/save")
    public ResponseEntity<User> adminSave(
            @RequestBody @Valid AdminSignUpReqDto reqDto
            ){
        User user = userService.saveUser(reqDto.toEntity());
        userService.addAuthority(user.getIdx(), "ROLE_ADMIN");
        return ResponseEntity.ok(user);

    }

}
