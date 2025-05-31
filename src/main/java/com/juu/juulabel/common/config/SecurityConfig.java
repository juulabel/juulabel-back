package com.juu.juulabel.common.config;

import com.juu.juulabel.common.filter.JwtAuthorizationFilter;
import com.juu.juulabel.common.filter.JwtExceptionFilter;
import com.juu.juulabel.member.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthorizationFilter jwtAuthorizationFilter;
        private final JwtExceptionFilter jwtExceptionFilter;

        // 완전 공개 엔드 포인트 (우선순위 최상)
        private static final String[] PUBLIC_ENDPOINTS = {
                        "/swagger-ui/**", "/v3/api-docs/**", "/error", "/favicon.ico", "/", "/actuator/**",
                        "/v1/api/auth/refresh", "/v1/api/auth/login/**"
        };

        // 관리자 전용 엔드포인트
        private static final String[] ADMIN_ENDPOINTS = {
                        "/v1/api/admins/permission/test"
        };

        // 인증/인가 필요한 특정 GET 엔드포인트
        private static final String[] PROTECTED_GET_ENDPOINTS = {
                        "/v1/api/members/my-info",
                        "/v1/api/members/my-space",
                        "/v1/api/members/tasting-notes/my",
                        "/v1/api/members/daily-lives/my",
                        "/v1/api/members/alcoholic-drinks/my"
        };

        // CORS 허용 원본
        private static final String[] ALLOWED_ORIGINS = {
                        "http://localhost:8084",
                        "http://localhost:8080",
                        "http://localhost:5173",
                        "http://localhost:3000",
                        "https://api.juulabel.com",
                        "https://dev.juulabel.com",
                        "https://qa.juulabel.com",
                        "https://juulabel.com",
                        "https://d3jwyw9rpnxu8p.cloudfront.net"
        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http

                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                                                .requireCsrfProtectionMatcher(request -> request.getServletPath()
                                                                .equals("/v1/api/auth/refresh")))

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
                                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class)

                                .build();
        }

        // Spring Security processes authorization rules in order, and the first match
        // wins
        private void configureAuthorization(
                        org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
                authorize
                                // 1️⃣ 완전 공개 엔드포인트 (우선순위 최상)
                                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                                // 2️⃣ CORS preflight 요청
                                .requestMatchers(OPTIONS, "**").permitAll()

                                // 3️⃣ 관리자 전용 엔드포인트
                                .requestMatchers(ADMIN_ENDPOINTS).hasAuthority(MemberRole.ROLE_ADMIN.name())

                                // 4️⃣ 인증이 필요한 특정 GET 엔드포인트
                                .requestMatchers(HttpMethod.GET, PROTECTED_GET_ENDPOINTS).authenticated();

                // 5️⃣ 나머지 GET 요청 (비인가 사용자에게 허용)
                authorize.requestMatchers(HttpMethod.GET, "**").permitAll();

                // 6️⃣ 나머지 POST, PUT, DELETE 요청 (기본적으로 인증 필요)
                authorize.anyRequest().authenticated();
        }

        @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration config = new CorsConfiguration();

                // Configure CORS settings
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.setAllowedOrigins(List.of(ALLOWED_ORIGINS));
                config.addExposedHeader(HttpHeaders.AUTHORIZATION);
                config.addExposedHeader("X-XSRF-TOKEN");
                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return source;
        }
}
