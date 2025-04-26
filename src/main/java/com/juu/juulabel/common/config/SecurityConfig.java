package com.juu.juulabel.common.config;

import com.juu.juulabel.common.filter.JwtAuthorizationFilter;
import com.juu.juulabel.common.filter.JwtExceptionFilter;
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

    private final JwtAuthorizationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] PERMIT_PATHS = {
        "/swagger-ui/**", "/v3/api-docs/**", "/error", "/favicon.ico", "/", "/actuator/**",
        "/v1/api/alcohols/**", "/v1/api/terms/**", "/v1/api/images",
        "/v1/api/members/**", "/v1/api/shared-space/tasting-notes/**", "/v1/api/notifications/**",
        "/v1/api/daily-lives/**", "/v1/api/alcoholicDrinks/**", "v1/api/follow" , "/**", "/v1/api/reports"
    };

    private static final String[] ALLOW_ORIGINS = {
        "http://localhost:8084",
        "http://localhost:8080",
        "http://localhost:5173",
        "http://localhost:3000",
        "https://api.juulabel.com",
        "https://dev.juulabel.com",
        "https://qa.juulabel.com",
        "https://juulabel.com",
        "https://juulabel.shop",
        "https://juulabel-front.vercel.app/",
        "https://juulabel-front-seven.vercel.app/",
        "https://d3jwyw9rpnxu8p.cloudfront.net"
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

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/v1/api/members/logout").authenticated()
                .requestMatchers(OPTIONS, "**").permitAll()
                .requestMatchers(PERMIT_PATHS).permitAll()
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class)

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
