package com.example.uxn_demo.global.config;

import com.example.uxn_demo.doamain.user.config.jwt.oauth.JWTCheckFilter;
import com.example.uxn_demo.doamain.user.config.jwt.oauth.JWTLoginFilter;
import com.example.uxn_demo.doamain.user.config.jwt.okta.JwtAuthenticationFilter;
import com.example.uxn_demo.doamain.user.config.jwt.okta.JwtTokenProvider;
import com.example.uxn_demo.doamain.user.domain.Role;
import com.example.uxn_demo.doamain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), userService); // 로그인을 처리하는 로그인 필터
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), userService); // 로그인된 토큰을 매번 리퀘스트마다 체크해줄 체크 필터

        http
                .cors().
                configurationSource(corsConfigurationSource()).
                and()
                .csrf().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함 -> jwt 토큰을 사용하기 때문. -> 세션을 사용하지 않기 때문에 Authentication / Authorization 문제가 생김
                .authorizeRequests()
                .antMatchers("/swagger-ui.html/**").permitAll()
                .antMatchers("/api/v1/user/signup/**").permitAll()
                .antMatchers("/api/v1/user/login/**").permitAll()
                .antMatchers(("/greeting")).hasRole(Role.USER.name())
                .antMatchers("/api/v1/user/info/**").hasRole(Role.USER.name())
//                .antMatchers("/greeting").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .and()
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class) // 세션을 사용하지 않고 토큰을 사용하여 인증. -> 로그인 처리
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class) // -> 토큰 검증
//                .addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
