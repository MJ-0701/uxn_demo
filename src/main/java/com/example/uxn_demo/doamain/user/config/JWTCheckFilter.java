package com.example.uxn_demo.doamain.user.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.uxn_demo.doamain.user.domain.User;
import com.example.uxn_demo.doamain.user.service.UserService;
import com.example.uxn_demo.doamain.user.web.dto.res.TokenVerifyResult;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private final UserService userService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, UserService userService){
        super(authenticationManager);
        this.userService = userService;
    }

    // 토큰검사
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION); //
        if(bearer == null || !bearer.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }
        String token = bearer.substring("Bearer ".length());
        TokenVerifyResult result = JWTUtil.verify(token);
        if(result.isSuccess()){
            User user = (User) userService.loadUserByUsername(result.getUserId());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(userToken);
            chain.doFilter(request, response);
        }else{
            throw new TokenExpiredException("Token is not valid");
        }
    }
}
