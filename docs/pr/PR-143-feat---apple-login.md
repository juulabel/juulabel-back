# 애플 로그인 추가 및 소셜 로그인 리팩토링 (PR [#143](https://github.com/juulabel/juulabel-back/pull/143))

## TL;DR

- **Apple OAuth 로그인 지원 추가**: JWT 기반 Apple Sign In 구현
- **OAuth 콜백 방식 변경**: 클라이언트→서버→클라이언트 흐름으로 개선
- **세션 기반 인증 시스템 도입**: JWT Access/Refresh Token에서 Redis 세션 기반으로 전환
- **PASETO 기반 회원가입 토큰**: JWT 대신 보안이 강화된 PASETO를 이용한 일회성 회원가입 토큰 구현
- **HttpOnly 쿠키 보안 강화**: 모든 인증 토큰을 HttpOnly 쿠키로 전송하여 XSS 공격 방지

## 🎯 주요 변경사항

### 1. Apple OAuth 구현

- **JWT 토큰 검증**: Apple의 Public Key를 이용한 ID Token 검증 로직 구현
- **RSA 암호화 지원**: Apple의 RSA 공개키를 통한 토큰 서명 검증
- **Apple API 클라이언트**: FeignClient를 활용한 Apple OAuth 서버 연동

### 2. 소셜 로그인 아키텍처 개선

- **Factory Pattern 도입**: `OAuthProviderFactory`로 프로바이더별 인스턴스 관리
- **Strategy Pattern 적용**: `OAuthProvider` 인터페이스를 통한 다형성 구현
- **확장성 확보**: 새로운 소셜 로그인 추가 시 최소한의 코드 변경으로 지원 가능

### 3. OAuth 콜백 플로우 개선

**기존 방식 (클라이언트 직접 처리):**

```
OAuth Provider → 클라이언트 → 서버 API (인가코드 전송)
```

**새로운 방식 (서버 중심 처리):**

```
OAuth Provider → 서버 콜백 엔드포인트 → 상태별 클라이언트 리다이렉트
```

### 4. 세션 기반 인증 시스템 전환

**기존**: JWT Access Token + Refresh Token  
**현재**: Redis 기반 세션 관리

### 5. PASETO 기반 회원가입 토큰 시스템

**기존**: JWT 기반 일회성 토큰  
**현재**: PASETO v2.local 기반 보안 강화된 회원가입 토큰

### 6. HttpOnly 쿠키 보안 강화

**모든 인증 관련 토큰을 HttpOnly 쿠키로 전송**:
- `auth_token`: 세션 기반 인증 토큰
- `sign_up_token`: PASETO 기반 회원가입 토큰

## 🔧 기술적 구현 세부사항

### OAuth 콜백 엔드포인트 구현 (/v1/api/auth/oauth/callback/{provider})

```24:29:src/main/java/com/juu/juulabel/auth/controller/AuthController.java
@Override
public ResponseEntity<CommonResponse<Void>> login(
        @PathVariable Provider provider,
        @RequestParam(required = true) String code,
        @RequestParam(required = true) String state) {

    authService.login(provider, code, state);
    return CommonResponse.success(SuccessCode.SUCCESS);
}
```

**콜백 및 클라이언트 리다이렉트 엔드포인트 설정:**

```142:148:src/main/resources/application.yml
app:
  redirect:
    base-server: http://localhost:8080
    base-client: http://localhost:3000
    callback: /v1/api/auth/oauth/callback
    login: /app/login/redirect
    signup: /app/sign-up/redirect
    error: /app/error
```

### 사용자 상태별 스마트 리다이렉트

```153:198:src/main/java/com/juu/juulabel/auth/service/AuthService.java
private void handleExistingMember(Member member, OAuthUser oAuthUser) {
    // 기존 활성 사용자 → 세션 생성 후 로그인 페이지로
    sessionManager.createSession(member);
    httpResponseUtil.redirectToLogin();
}

private void handlePendingMember(Member member, OAuthUser oAuthUser) {
    // 가입 대기 사용자 → 회원가입 토큰 생성 후 회원가입 페이지로
    signupTokenProvider.createToken(oAuthUser, nonce);
    httpResponseUtil.redirectToSignup();
}

private void handleNewMember(OAuthUser oAuthUser) {
    // 신규 사용자 → 펜딩 멤버 생성 후 회원가입 페이지로
    signupTokenProvider.createToken(oAuthUser, nonce);
    Member newMember = Member.create(oAuthUser, nonce);
    memberWriter.store(newMember);
    httpResponseUtil.redirectToSignup();
}
```

### Redis 기반 세션 관리 시스템

```22:66:src/main/java/com/juu/juulabel/member/token/UserSession.java
@RedisHash(value = "user_session")
public class UserSession implements Serializable {
    @Id
    private String id;

    @Indexed
    private Long memberId;

    private String email;
    private MemberRole role;
    private String deviceId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;

    @TimeToLive
    private Long ttl; // 7 days
}
```

**세션 생성 및 관리:**

```53:75:src/main/java/com/juu/juulabel/redis/SessionManager.java
public void createSession(Member member) {
    String sessionId = generateUniqueSessionId();
    UserSession session = new UserSession(sessionId, member);

    userSessionRepository.save(session);
    cookieUtil.addCookie(AuthConstants.AUTH_TOKEN_NAME, sessionId,
                        AuthConstants.USER_SESSION_TTL);
}
```

### Apple JWT Token 검증 프로세스

```53:66:src/main/java/com/juu/juulabel/common/provider/token/jwt/AppleTokenProvider.java
public AppleUser getAppleUserFromToken(List<ApplePublicKey> publicKeys, OAuthToken oauthToken) {
    ApplePublicKey applePublicKey = getApplePublicKey(publicKeys, oauthToken);
    PublicKey publicKey = buildPublicKey(applePublicKey);

    // Set up JWT parser with the public key
    super.key = publicKey;
    super.jwtParser = Jwts.parser().verifyWith(publicKey).build();

    return extractFromClaims(oauthToken.idToken(), claims -> new AppleUser(
            claims.get(SUB_CLAIM, String.class),
            claims.get(EMAIL_CLAIM, String.class)));
}
```

**핵심 특징:**

- Apple의 동적 공개키 검증 (JWK 방식)
- JWT Header의 `kid` 값과 Apple 공개키 매칭
- RSA 공개키 재구성 및 서명 검증


**아키텍처 장점:**

- 각 프로바이더별 구현체의 느슨한 결합
- Open-Closed Principle 준수 (확장에는 열려있고 수정에는 닫혀있음)
- 런타임 프로바이더 선택 및 의존성 주입

### PASETO 기반 회원가입 토큰 시스템

**JWT 대신 PASETO를 선택한 이유:**

| 특성 | JWT | PASETO |
|------|-----|--------|
| **알고리즘 선택** | 개발자가 알고리즘 선택 (보안 위험) | 버전별 고정 알고리즘 (안전) |
| **암호화 방식** | 대칭/비대칭 선택 가능 | v2.local: ChaCha20-Poly1305 (대칭) |
| **보안성** | 알고리즘 혼동 공격 가능성 | 알고리즘 고정으로 공격 차단 |
| **성능** | RSA 서명 검증 시 느림 | 대칭키 암호화로 빠른 성능 |
| **용도** | 범용 토큰 | 특정 목적 (회원가입) 토큰 |

**PASETO 토큰 생성:**

```44:51:src/main/java/com/juu/juulabel/common/provider/token/paseto/SignupTokenProvider.java
public void createToken(OAuthUser oAuthUser, String nonce) {
    String token = builder()
            .claim(EMAIL_CLAIM, oAuthUser.email())
            .claim(PROVIDER_CLAIM, oAuthUser.provider().name())
            .claim(PROVIDER_ID_CLAIM, oAuthUser.id())
            .claim(NONCE_CLAIM, nonce)
            .claim(AUDIENCE_CLAIM_KEY, AUDIENCE_CLAIM)
            .compact();
    cookieUtil.addCookie(AuthConstants.SIGN_UP_TOKEN_NAME, token,
            (int) AuthConstants.SIGN_UP_TOKEN_DURATION.toSeconds());
}
```

**PASETO 토큰 검증 및 보안 기능:**

```59:95:src/main/java/com/juu/juulabel/common/provider/token/paseto/SignupTokenProvider.java
public Member verifyToken(String token) {
    Claims claims = parseClaims(token);

    // Extract and validate all claims at once
    TokenClaims tokenClaims = extractTokenClaims(claims);

    // Validate audience first (fast check)
    if (!AUDIENCE_CLAIM.equals(tokenClaims.audience())) {
        throw new AuthException("Invalid token audience", ErrorCode.INVALID_AUTHENTICATION);
    }

    // Get member and validate
    Member member = memberReader.getByEmail(tokenClaims.email());
    validateMemberAgainstToken(member, tokenClaims);

    return member;
}

private void validateMemberAgainstToken(Member member, TokenClaims tokenClaims) {
    // Check provider and provider ID
    if (member.getProvider() != tokenClaims.provider()) {
        throw new AuthException("Provider mismatch", ErrorCode.PROVIDER_ID_MISMATCH);
    }

    if (!member.getProviderId().equals(tokenClaims.providerId())) {
        throw new AuthException("Provider ID mismatch", ErrorCode.PROVIDER_ID_MISMATCH);
    }

    if (!member.getNickname().equals(tokenClaims.nonce())) {
        throw new AuthException("Token validation failed", ErrorCode.INVALID_AUTHENTICATION);
    }

    // Check member status
    if (member.getStatus() != MemberStatus.PENDING) {
        throw new AuthException("Member already completed signup", ErrorCode.INVALID_AUTHENTICATION);
    }
}
```

### HttpOnly 쿠키 보안 시스템

**포괄적 보안 설정:**

```102:125:src/main/java/com/juu/juulabel/common/util/CookieUtil.java
private Cookie createSecureCookie(String name, String value, int maxAge) {
    boolean isSecure = cookieProperties.isSecure();
    Cookie cookie = new Cookie(name, value);

    // Set domain only for production/secure environments
    if (isSecure) {
        cookie.setDomain(cookieProperties.getDomain());
    }

    cookie.setPath(cookieProperties.getPath());
    cookie.setHttpOnly(cookieProperties.isHttpOnly());
    cookie.setSecure(isSecure);
    cookie.setMaxAge(maxAge);

    // Set SameSite attribute based on security requirements
    String sameSite = isSecure ? cookieProperties.getSameSiteSecure() : cookieProperties.getSameSiteNonSecure();
    cookie.setAttribute("SameSite", sameSite);

    return cookie;
}
```

**쿠키 보안 속성:**

```47:50:src/main/java/com/juu/juulabel/common/properties/CookieProperties.java
/**
 * Whether to set HttpOnly flag on cookies by default.
 * Default: true (recommended for security)
 */
private boolean httpOnly = true;
```

**세션 토큰과 회원가입 토큰 모두 HttpOnly 쿠키로 처리:**

```64:71:src/main/java/com/juu/juulabel/common/filter/AuthorizationFilter.java
private void handleSignUpRequest() {
    String signupToken = cookieUtil.getCookie(AuthConstants.SIGN_UP_TOKEN_NAME);

    if (!StringUtils.hasText(signupToken)) {
        throw new AuthException(ErrorCode.SIGN_UP_SESSION_EXPIRED);
    }

    processSignUpToken(signupToken);
}
```

```73:80:src/main/java/com/juu/juulabel/common/filter/AuthorizationFilter.java
private void handleRegularRequest() {
    String authToken = cookieUtil.getCookie(AuthConstants.AUTH_TOKEN_NAME);

    if (StringUtils.hasText(authToken)) {
        processUserSession(authToken);
    }
}
```

## 🛡️ 보안 및 안정성 강화

### 1. PASETO 토큰 보안 이점

**JWT 대비 PASETO의 보안 장점:**

- **알고리즘 고정**: `v2.local`에서 ChaCha20-Poly1305 암호화 고정 사용
- **인증된 암호화**: 암호화와 인증을 동시에 제공하여 변조 방지
- **키 관리 단순화**: 대칭키만 사용으로 키 관리 복잡성 감소
- **타이밍 공격 방지**: 내장된 상수 시간 비교 연산

```46:51:src/main/java/com/juu/juulabel/common/provider/token/paseto/PasetoTokenProvider.java
protected PasetoV2LocalBuilder builder() {
    return Pasetos.V2.LOCAL.builder()
            .setSharedSecret(this.key)
            .setIssuer(ISSUER)
            .setAudience("juu-label-client")
            .setIssuedAt(Instant.now())
            .setExpiration(Instant.now().plus(this.duration));
}
```

### 2. HttpOnly 쿠키 보안 강화

**XSS 공격 방지:**
- JavaScript를 통한 토큰 접근 완전 차단
- 브라우저가 자동으로 쿠키를 HTTP 요청에 포함
- 클라이언트 측 토큰 저장소 관리 불필요

**CSRF 공격 대응:**
- SameSite 속성을 통한 크로스사이트 요청 제한
- 개발 환경: `Lax` (기능성과 보안의 균형)
- 프로덕션 환경: `Strict` 또는 `None` (HTTPS 필수)

### 3. 회원가입 토큰 특화 보안

**제한된 권한과 생명주기:**

```13:14:src/main/java/com/juu/juulabel/common/constants/AuthConstants.java
public static final String SIGN_UP_TOKEN_NAME = "sign_up_token";
public static final Duration SIGN_UP_TOKEN_DURATION = Duration.ofMinutes(15);
```

**단일 목적 토큰:**
- 회원가입 완료 전용 토큰 (`audience: user-signup-completion`)
- 15분 짧은 만료 시간으로 공격 시간 윈도우 최소화
- 사용자 상태(`PENDING`) 검증으로 중복 사용 방지

## 🔄 아키텍처 변경의 핵심 이점

### 1. OAuth 콜백 플로우 개선

**기존 문제점:**

- 클라이언트에서 인가코드 처리 → CORS 이슈
- 프론트엔드에 OAuth 로직 분산 → 복잡성 증가
- 에러 처리의 일관성 부족

**개선된 방식:**

```147:152:src/main/java/com/juu/juulabel/auth/service/AuthService.java
private OAuthUser getOAuthUser(Provider provider, String code) {
    String redirectUrl = redirectProperties.getRedirectUrl(provider);
    return providerFactory.getOAuthUser(provider, code, redirectUrl);
}
```

**장점:**

- 서버에서 OAuth 플로우 완전 제어
- 사용자 상태별 최적화된 리다이렉트
- 통일된 에러 처리 및 로깅

### 2. 토큰 시스템 이원화 전략

| 토큰 종류 | 기술 | 용도 | 생명주기 | 보안 특성 |
|-----------|------|------|----------|-----------|
| **세션 토큰** | Redis 세션 | 로그인 사용자 인증 | 7일 | 즉시 무효화 가능 |
| **회원가입 토큰** | PASETO v2.local | 회원가입 완료 | 15분 | 암호화된 일회성 토큰 |

**이원화 선택 이유:**

- **목적별 최적화**: 각 용도에 맞는 최적의 기술 선택
- **보안 계층화**: 서로 다른 보안 메커니즘으로 공격 벡터 분산
- **성능 최적화**: 세션은 Redis 캐시, 회원가입은 암호화 토큰

### 3. 세션 vs JWT 토큰 비교

| 특성       | JWT 토큰                   | 세션 기반             | PASETO (회원가입) |
| ---------- | -------------------------- | --------------------- | ----------------- |
| **확장성** | Stateless (서버 부하 적음) | Stateful (Redis 의존) | Stateless |
| **보안성** | 토큰 탈취 시 만료까지 유효 | 즉시 세션 무효화 가능 | 암호화된 일회성 토큰 |
| **추적성** | 토큰 사용 추적 어려움      | 세션 활동 완전 추적   | 단일 목적 추적 |
| **복잡성** | 토큰 관리 로직 복잡        | 세션 관리 직관적      | 단순한 검증 로직 |

**세션 방식 선택 이유:**

- **보안 우선**: 토큰 탈취 시 즉시 무효화 가능
- **확장성 확보**: 추후 멀티 디바이스 로그인 제어 용이
- **감사 로그**: 세션 기반 사용자 활동 추적

## 📋 설정 및 환경 구성

**실제 리다이렉트 플로우:**

1. `OAuth Provider` → `http://localhost:8080/v1/api/auth/oauth/callback/{provider}`
2. 서버에서 사용자 상태 확인 후 적절한 클라이언트 페이지로 리다이렉트
3. `http://localhost:3000/app/{login|signup|error}`

### Redis 세션 저장소 설정

```12:18:src/main/resources/application.yml
data:
  redis:
    host: localhost
    port: 6379
    ssl:
      enabled: true
```

### Apple OAuth 설정

```68:72:src/main/resources/application.yml
apple:
  clientId: your-apple-client-id
  clientSecret: your-apple-client-secret
  authorization-grant-type: authorization_code
  redirectUri: "http://localhost:3000/login/oauth2/code/apple"
```

### 쿠키 보안 설정

```yaml
app:
  cookie:
    secure: false # 개발 환경, 프로덕션에서는 true
    domain: juulabel.com
    path: /
    sameSiteSecure: None # HTTPS 환경용
    sameSiteNonSecure: Lax # HTTP 환경용  
    httpOnly: true # XSS 방지를 위해 항상 true
```

## 🚀 성능 최적화

### 1. 세션 관리 최적화

```128:138:src/main/java/com/juu/juulabel/redis/SessionManager.java
private void updateSessionActivity(UserSession session) {
    try {
        session.updateLastAccessed();
        userSessionRepository.save(session);
    } catch (Exception e) {
        log.warn("Failed to update session activity for session: {}", session.getId(), e);
        // Non-critical operation, don't throw exception
    }
}
```

### 2. Redis 인덱스 최적화

```18:20:src/main/java/com/juu/juulabel/member/token/UserSession.java
@Indexed
private Long memberId; // 사용자별 세션 조회 최적화
```

### 3. PASETO 성능 최적화

**ObjectMapper 및 상수 재사용:**

```34:34:src/main/java/com/juu/juulabel/common/provider/token/jwt/AppleTokenProvider.java
private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
```

**대칭키 암호화 성능:**
- ChaCha20-Poly1305: RSA 대비 약 10배 빠른 암호화/복호화
- 메모리 사용량 감소: 큰 RSA 키 대신 32바이트 대칭키 사용

### 4. 쿠키 처리 최적화

**쿠키 존재 여부 빠른 확인:**

```96:99:src/main/java/com/juu/juulabel/common/util/CookieUtil.java
public boolean cookieExists(String name) {
    return getCookie(name) != null;
}
```

## 🧪 테스트 전략

### 단위 테스트 고려사항

- Apple JWT 토큰 검증 로직
- 세션 생성 및 검증
- Factory Pattern의 프로바이더 선택 로직
- **PASETO 토큰 생성 및 검증**
- **HttpOnly 쿠키 설정 확인**

### 통합 테스트 권장사항

- OAuth Provider별 End-to-End 플로우
- 세션 기반 인증 통합 테스트
- 리다이렉트 시나리오 검증
- **PASETO 회원가입 플로우 전체 테스트**
- **쿠키 보안 속성 검증**

---
