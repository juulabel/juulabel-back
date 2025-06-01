# 인증 시스템 보안 강화 및 아키텍처 리팩토링 (PR [#142](https://github.com/juulabel/juulabel-back/pull/144))

## TL;DR

본 PR은 소셜 인증 시스템의 종합적인 보안 개선을 구현하여, 여러 중요한 보안 취약점을 해결하고 전체적인 아키텍처를 개선합니다. CSRF 보호, 토큰 분리, 그리고 간소화된 API 계약을 도입합니다.

1. **보안 강화**: CSRF 보호 및 안전한 쿠키 처리 구현
2. **아키텍처 단순화**: API 표면 영역 축소 및 미사용 필드 제거
3. **토큰 보안**: 토큰 시크릿 분리 및 적절한 토큰 생명주기 관리
4. **사용자 경험**: 보안 강화를 유지하면서 원활한 인증 플로우 제공

## 🔧 기술적 변경사항

### 1. 로그인 엔드포인트 리팩토링 (`/v1/api/auth/login/{provider}`)

#### 응답 바디 최적화

**변경 전**: 미사용 필드가 포함된 복잡한 응답  
**변경 후**: 필수 데이터 플로우에 집중한 간소화된 응답

```java
public record LoginResponse(
    String accessToken,     // 기존 사용자의 경우 제공, 신규 사용자의 경우 null
    String signUpToken,     // 신규 사용자의 경우 제공, 기존 사용자의 경우 null
    String email            // 식별을 위해 항상 제공
) {}
```

**근거**:

- 미사용 필드를 통한 데이터 누출 방지
- 기존 사용자 로그인과 신규 사용자 회원가입 플로우의 명확한 분리
- 클라이언트 측 상태 관리 단순화

#### CSRF 보호 구현

- **쿠키**: 로그인 시 `CSRF-TOKEN`을 쿠키로 저장
- **사용처**: 후속 토큰 갱신 작업에 필요
- **보안**: 민감한 토큰 작업에 대한 CSRF 공격 방지

### 2. 회원가입 엔드포인트 강화 (`/v1/api/auth/sign-up`)

#### Authorization 헤더 요구사항

```http
Authorization: Bearer {signUpToken}
```

**보안 기능**:

- **토큰 검증**: 회원가입 토큰이 로그인 세션 데이터와 일치하는지 확인
- **시간 검증**: 15분 만료 기간 (초과 시 401 반환)
- **무결성 검사**: 토큰 불일치/변조 시 403 반환

#### 요청 바디 단순화

**제거된 필드** (현재 signUpToken 페이로드에 포함):

- `email` - 토큰 페이로드에서 추출
- `provider` - 토큰 페이로드에서 추출
- `providerId` - 토큰 페이로드에서 추출

**이점**:

- 요청과 토큰 간의 데이터 불일치 방지
- 데이터 조작에 대한 공격 표면 축소
- 단일 진실 소스 원칙 적용

#### 응답 최적화

```java
public record SignUpMemberResponse(
    Long memberId,
    String accessToken
) {}
```

### 3. 토큰 갱신 보안 (`/v1/api/auth/refresh`)

#### CSRF 헤더 검증

```http
X-CSRF-TOKEN: {csrfToken}
```

**구현 세부사항**:

- 쿠키 값과 헤더 값이 일치해야 함
- 각 요청마다 토큰 자동 갱신
- 토큰 누락/무효 시 403 오류 반환

#### 보안 설정

```java
.csrf(csrf -> csrf
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
    .requireCsrfProtectionMatcher(request ->
        request.getServletPath().equals("/v1/api/auth/refresh")))
```

## 🔐 보안 강화사항

### 토큰 시크릿 분리

- **Access Token**: 사용자 세션 전용 시크릿
- **Refresh Token**: 토큰 갱신 전용 시크릿
- **Signup Token**: 등록 플로우 전용 시크릿

**이점**: 시크릿 침해 시 피해 범위 제한

## 🔄 마이그레이션 전략

### Breaking Changes

1. **API 계약 변경**: 클라이언트 애플리케이션이 새 응답 형식으로 업데이트 필요
2. **헤더 요구사항**: 회원가입 요청에 Authorization 헤더 필요
3. **CSRF 헤더**: 갱신 요청에 X-CSRF-TOKEN 헤더 필요

### 호환성 고려사항

- 전환 기간 동안 기존 액세스 토큰 유효 유지
- 프로덕션 배포 시 점진적 롤아웃 권장
- 헤더 관리를 위한 클라이언트 측 변경 필요

## 🚀 향후 고려사항

### OAuth 2.0 PKCE 구현

- **상태**: 다음 반복에서 계획됨
- **접근방식**: 프론트엔드 팀과 협력하여 클라이언트 측 구현
- **이점**: 공개 OAuth 클라이언트에 대한 보안 강화

### 쿠키 보안 강화

**현재 구현**: 도메인 마이그레이션 전략

- **기존**: `juulabel.com/app`
- **변경**: `m.juulabel.com`
- **API 도메인**: `api.juulabel.com`
- **SameSite**: CSRF 보호 강화를 위한 Strict 정책

---

**리뷰 체크리스트**:

- [ ] CSRF 보호 검증, 엔드투엔드 인증 플로우
- [ ] 도메인 마이그레이션 전략 검토
- [ ] 모든 인증 플로우에 대한 QA 검증
