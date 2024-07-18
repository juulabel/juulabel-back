package com.juu.juulabel.api.config;

import com.juu.juulabel.api.filter.JwtAuthenticationFilter;
import com.juu.juulabel.api.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
//    private final OAuth2UserService oAuth2UserService;
//    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;

    private static final String[] PERMIT_PATHS = {
        "/swagger-ui/**", "/api-docs/**", "/error", "/favicon.ico", "/", "/v1/api/members/login/**",
        "/v1/api/members/sign-up/**", "/v1/api/members/nicknames/{nickname}/exists", "/v1/api/alcohols/types",
        "/v1/api/terms/**",
        "/*", "/**" // 배포 시 제거
    };

    private static final String[] ALLOW_ORIGINS = {
        "http://localhost:8084",
        "http://localhost:8080",
        "http://localhost:5173",
        "http://localhost:3000",
        "https://api.juulabel.com",
        "https://juulabel.com"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .headers(AbstractHttpConfigurer::disable)
//            .logout(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/v1/api/members/logout").authenticated()
                .requestMatchers(OPTIONS, "**").permitAll()
                .requestMatchers(PERMIT_PATHS).permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)

            // OAuth2 로그인
//            .oauth2Login(oauth2 -> oauth2
//                .userInfoEndpoint(
//                    userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserService))
//                .successHandler(oAuthLoginSuccessHandler)
//            )

            .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowedOrigins(List.of(ALLOW_ORIGINS));
        config.addExposedHeader(HttpHeaders.AUTHORIZATION);
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
