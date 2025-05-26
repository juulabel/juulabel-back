package com.juu.juulabel.common.config;

import com.juu.juulabel.common.filter.JwtAuthorizationFilter;
import com.juu.juulabel.common.filter.JwtExceptionFilter;
import com.juu.juulabel.member.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthorizationFilter jwtAuthenticationFilter;
        private final JwtExceptionFilter jwtExceptionFilter;

        // Public endpoints that don't require authentication
        private static final String[] PUBLIC_ENDPOINTS = {
                        "/swagger-ui/**", "/v3/api-docs/**", "/error", "/favicon.ico", "/", "/actuator/**",
                        "/v1/api/alcohols/**", "/v1/api/terms/**", "/v1/api/images",
                        "/v1/api/auth/**", "/v1/api/shared-space/tasting-notes/**", "/v1/api/notifications/**",
                        "/v1/api/daily-lives/**", "/v1/api/alcoholicDrinks/**", "v1/api/follow", "/**",
                        "/v1/api/reports"
        };

        // Admin-only endpoints
        private static final String[] ADMIN_ENDPOINTS = {
                        "/v1/api/admins/permission/test"
        };

        // Allowed origins for CORS
        private static final String[] ALLOWED_ORIGINS = {
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
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http
                                // Disable unnecessary features for stateless API
                                .csrf(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .formLogin(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Configure headers
                                .headers(headers -> headers
                                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                                // Configure CORS
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                // Configure authorization rules
                                .authorizeHttpRequests(this::configureAuthorization)

                                // Add custom filters
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class)

                                .build();
        }

        private void configureAuthorization(
                        org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
                authorize
                                // Allow OPTIONS requests for CORS preflight
                                .requestMatchers(OPTIONS, "**").permitAll()

                                // Public endpoints
                                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                                // Admin endpoints
                                .requestMatchers(ADMIN_ENDPOINTS).hasAuthority(MemberRole.ROLE_ADMIN.name())

                                // Specific authenticated endpoints
                                .requestMatchers("/v1/api/members/logout").authenticated()

                                // All other requests require authentication
                                .anyRequest().authenticated();
        }

        @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();

                // Configure CORS settings
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedOrigins(List.of(ALLOWED_ORIGINS));
                config.addExposedHeader(HttpHeaders.AUTHORIZATION);
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}
