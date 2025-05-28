# 비회원 사용자 GET 접근 정책 리팩터링 (PR [#140](https://github.com/juulabel/juulabel-back/pull/142))

## 개요

기존 보안 정책은 인증되지 않은 사용자(비회원)에 대해 사실상 모든 접근을 차단하는 구조였으며, 일부 공개 엔드포인트만 명시적으로 허용하고 있었습니다. 해당 방식은 강력한 보안성을 확보할 수 있는 반면, 신규 유입 사용자나 비회원의 콘텐츠 탐색 경험을 제한하는 문제가 있었습니다.

이번 리팩터링은 "비회원 사용자도 서비스 콘텐츠를 탐색할 수 있도록 한다"는 목표 아래, 명시된 엔드포인트만 차단, 나머지는 모두 허용하는 방향으로 접근 제어 로직을 개선하였습니다. 단, 상태를 변경하는 요청(POST, PUT, DELETE)은 기존대로 인증을 요구합니다.

## 변경 전 vs 변경 후

| 항목                       | 변경 전                                 | 변경 후                                                       |
| -------------------------- | --------------------------------------- | ------------------------------------------------------------- |
| 콘텐츠 접근성              | ❌ 비회원은 대부분의 콘텐츠에 접근 불가 | ✅ 주요 콘텐츠 열람 가능 (시음노트, 일상생활, 유저 프로필 등) |
| 읽기 작업 처리             | 허용된 소수 엔드포인트만 가능           | 명시된 엔드포인트만 차단, 나머지는 모두 허용              |
| 쓰기 작업 처리             | 인증 필요 (POST, PUT, DELETE 모두 차단) | 동일하게 유지                                                 |

---

## 보안 정책 요약

```java
// 완전 공개 엔드 포인트 (우선순위 최상)
private static final String[] PUBLIC_ENDPOINTS = {
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/error",
    "/favicon.ico",
    "/",
    "/actuator/**",
    "/v1/api/auth/login/**",
    "/v1/api/auth/sign-up"
};

// 관리자 전용 엔트포인트
private static final String[] ADMIN_ENDPOINTS = {
    "/v1/api/admins/permission/test"
};

// 인증/인가 필요한 특정 GET 엔드포인트
private static final String[] PROTECTED_GET_ENDPOINTS = {
    "/v1/api/members/my-info",
    "/v1/api/members/my-space",
    "/v1/api/members/tasting-notes/my",
    "/v1/api/members/daily-lives/my",
    "/v1/api/members/alcoholic-drinks/my",
};
```

```java
// SecurityConfig.java 내 접근 정책 설정 예시

    // 1️⃣ 완전 공개 엔드포인트 (우선순위 최상)
    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

    // 2️⃣ CORS preflight 요청
    .requestMatchers(OPTIONS, "**").permitAll()

    // 3️⃣ 관리자 전용 엔드포인트
    .requestMatchers(ADMIN_ENDPOINTS).hasAuthority(MemberRole.ROLE_ADMIN.name())

    // 4️⃣ 인증이 필요한 특정 GET 엔드포인트
    .requestMatchers(HttpMethod.GET, PROTECTED_GET_ENDPOINTS).authenticated()

    // 5️⃣ 나머지 GET 요청 (비인가 사용자에게 허용)
    .requestMatchers(HttpMethod.GET, "**").permitAll()

    // 6️⃣ 나머지 모든 요청 (POST, PUT, DELETE 등 인증 필요)
    .anyRequest().authenticated();
```

## 새롭게 허용된 비회원 접근 허용 리소스 목록

### 시음노트 API

| 메서드 | 경로                                                    | 설명                           |
| ------ | ------------------------------------------------------- | ------------------------------ |
| GET    | `/shared-space/tasting-notes`                           | 전체 시음노트 목록 조회        |
| GET    | `/shared-space/tasting-notes/by-alcoholicDrinks/{id}`   | 특정 전통주 관련 시음노트 목록 |
| GET    | `/shared-space/tasting-notes/{id}`                      | 시음노트 상세 정보             |
| GET    | `/shared-space/tasting-notes/{id}/comments`             | 댓글 목록                      |
| GET    | `/shared-space/tasting-notes/{id}/comments/{commentId}` | 답글 목록                      |

### 일상생활 API

| 메서드 | 경로                                     | 설명               |
| ------ | ---------------------------------------- | ------------------ |
| GET    | `/daily-lives`                           | 일상생활 목록 조회 |
| GET    | `/daily-lives/{id}`                      | 일상생활 상세 정보 |
| GET    | `/daily-lives/{id}/comments`             | 댓글 목록          |
| GET    | `/daily-lives/{id}/comments/{commentId}` | 답글 목록          |

### 회원 API

| 메서드 | 경로                          | 설명                      |
| ------ | ----------------------------- | ------------------------- |
| GET    | `/members/{id}/profile`       | 특정 유저의 공개 프로필   |
| GET    | `/members/{id}/tasting-notes` | 특정 유저의 시음노트 목록 |
| GET    | `/members/{id}/daily-lives`   | 특정 유저의 일상생활 목록 |
| GET    | `/members/{id}/followings`    | 팔로잉 목록               |
| GET    | `/members/{id}/followers`     | 팔로워 목록               |
| GET    | `/members/search`             | 사용자 검색               |
| GET    | `/members/recommendations`    | 추천 사용자 조회          |

## 향후 고려사항

- 비회원 트래픽 추이 및 콘텐츠 소비 패턴 모니터링 필요
- 공개된 GET 리소스에 대해 Abuse 방지를 위한 Rate Limit 적용 검토
- API 응답 데이터의 개인정보 포함 여부 재점검 및 마스킹 처리 필요
