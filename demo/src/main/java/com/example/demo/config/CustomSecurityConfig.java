package com.example.demo.config;

import com.example.demo.security.filter.JWTCheckFilter;
import com.example.demo.security.handler.APILoginFailHandler;
import com.example.demo.security.handler.APILoginSuccessHandler;
import com.example.demo.security.handler.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("----------security config ------------");

        //CROSS ORIGIN 활성화
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        // CSRF공격 (CROSS-SITE REQUEST FORGERY) 보호 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        //로그인시 API요청
        http.formLogin(config -> {
            config.loginPage("/api/member/login");
            config.successHandler(new APILoginSuccessHandler());
            config.failureHandler(new APILoginFailHandler());
        });

        //USERNAME 어쩌구 필터 실행되기 전에 JWTCheckFilter 먼저 실행해줘
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        //session 사용안함
        http.sessionManagement((httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
        }));

        http.exceptionHandling(config -> {
            config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
