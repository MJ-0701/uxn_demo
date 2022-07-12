package com.example.uxn_demo.doamain.user.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.uxn_demo.doamain.user.domain.User;
import com.example.uxn_demo.doamain.user.service.UserService;
import com.example.uxn_demo.doamain.user.web.dto.req.UserLoginReqDto;
import com.example.uxn_demo.doamain.user.web.dto.res.TokenVerifyResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService;

    public JWTLoginFilter(AuthenticationManager authenticationManager, UserService userService){
        super(authenticationManager);
        this.userService = userService;
        setFilterProcessesUrl("/api/v1/user/login/*");

    }


    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException { // 사용자 인증 처리

        UserLoginReqDto userLogin = objectMapper.readValue(request.getInputStream(), UserLoginReqDto.class);
        if(userLogin.getRefreshToken() == null){
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken( // 토큰  인증되기 전이기 때문에  Authorities null
                    userLogin.getUserId(), userLogin.getPassword(),null
            );
            // user details...
            return getAuthenticationManager().authenticate(token); // getAuthenticationManager 토큰 검증 요청
        }else{ // 리프레쉬 토큰으로 들어오면 토큰이 유효한지 검증 한다.
            TokenVerifyResult verify = JWTUtil.verify(userLogin.getRefreshToken());
            if(verify.isSuccess()){
                User user = (User) userService.loadUserByUsername(verify.getUserId());
                return new UsernamePasswordAuthenticationToken(
                        user,user.getAuthorities()
                );
            }else{
                throw new TokenExpiredException("refresh token expired");
            }
        }


    }

    @Override
    protected void successfulAuthentication( // 검증이 제대로 됐다면 해당 메소드가 호출됨.
                                             HttpServletRequest request,
                                             HttpServletResponse response,
                                             FilterChain chain,
                                             Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer"); // 인증 테스트 -> KEY : Authorization Value : Bearer + 토큰 값.
        response.setHeader("auth_token", JWTUtil.makeAuthToken(user));
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(user));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(user)); // 인증된 토큰을 유저객체에 발행

    }
}
