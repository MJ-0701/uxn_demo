package com.example.uxn_demo.doamain.user.web.controller;

import com.example.uxn_demo.doamain.user.service.UserService;
import com.example.uxn_demo.doamain.user.web.dto.res.UserInfoResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/info")
public class UserInfoApiController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResDto> getUserInfo(@PathVariable Long id){

        return ResponseEntity.ok(userService.findByIdx(id));
    }

    @GetMapping("/search/{userId}")
    public ResponseEntity<UserInfoResDto> getUserInfoByUserId(@PathVariable String userId){
        return ResponseEntity.ok(userService.findByUserId(userId));
    }


}
