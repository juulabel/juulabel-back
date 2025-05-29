# 치명적인 보안 패치 및 인증/인가 리팩토링 (PR [#141](https://github.com/juulabel/juulabel-back/pull/143))

## TL;DR

이번 PR은 소셜 로그인 프로세스에 존재했던 **치명적인 보안 취약점**을 해결하고, 불필요한 데이터베이스 호출을 줄이며 도메인 책임을 명확히 했습니다.

| 항목                 | Before                    | After                  | 결과                      |
| :------------------- | :------------------------ | :--------------------- | :------------------------ |
| **보안 위험**        | **높음** (소셜 인증 우회) | **완화됨**             | **치명적 취약점 해결**  |
| **회원가입 DB 쿼리** | 4회                       | 1회                    | **75% 감소**            |
| **로그인 DB 쿼리**   | 2회                       | 1회                    | **50% 감소**           |
| **이메일 검증**        | 중복 이메일 처리 회원가입에서만                  | 로그인도 같이 | **유저 경험 개선**     |

---

## 💥 핵심 문제 해결

### 1. 소셜 로그인 우회 취약점 차단

**문제점:** 이전에는 사용자가 소셜 인증 핸드셰이크 과정을 완전히 우회할 수 있었습니다. `/v1/api/auth/sign-up` 엔드포인트에 조작된 데이터를 직접 호출함으로써, 실제 검증 없이 계정을 생성할 수 있었습니다.

```javascript
// 이전 취약점: 검증되지 않은 회원가입 허용
POST /v1/api/auth/sign-up
{
  "email": "compromised@email.com",
  "nickname": "fake_name",
  "provider": "GOOGLE",
  "providerId": "fake_google_id",
  ...
}
```

**해결**: TTL 기반 소셜 인증 상태 관리

- 소셜 로그인 시 로그인 정보와 request header로부터 받아올수있는 metatdata를 Redis에 30분 TTL로 저장
- 회원가입 시 로그인떄와 저장된 정보와 100% 일치하는지 검증
- 불일치하거나 TTL 만료 시 가입 차단

### 2. 인증 로직 보완

**Before**: 기존에는 이미 가입된 이메일의 다른 소셜 로그인을 통한 처리를 회원가입부분에서 검증.

```java
boolean isNewMember = !memberReader.existsByEmailAndProvider(email, provider);
```

**After**: 로그인 과정에서도 검증하여 에러 반환

```java
public void validateLoginMember(Provider provider, String providerId) {
    if (this.deletedAt != null) {
        throw new BaseException(ErrorCode.MEMBER_WITHDRAWN);
    }
    // 사용자가 등록한 *동일한* 제공업체를 통해 로그인하는지 확인
    if (!this.provider.equals(provider)) {
        throw new BaseException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
    }
    // 중요: *올바른* 제공업체의 고유 ID 확인
    if (!this.providerId.equals(providerId)) {
        throw new AuthException(ErrorCode.PROVIDER_ID_MISMATCH);
    }
}
```

## 3. 데이터베이스 성능 최적화

**Before**: 회원가입 과정에서는 INSERT를 시도하기 전에 닉네임 충돌, 이메일 충돌, 탈퇴 상태를 확인하기 위해 여러 번의 중복된 EXISTS 쿼리가 발생했습니다. 이로 인해 불필요한 데이터베이스 왕복이 발생했습니다.

```java
// 비효율적: 삽입 전 여러 사전 확인
boolean nicknameExists = memberRepository.existsByNickname(nickname); // 쿼리 1
boolean emailExists = memberRepository.existsByEmail(email);       // 쿼리 2
boolean isWithdrawn = withdrawalRepository.existsByEmail(email); // 쿼리 3
memberRepository.save(member);                                     // 쿼리 4
```

**After**: 데이터베이스의 내장된 제약 조건 위반 처리를 활용합니다. INSERT를 직접 시도하고 DataIntegrityViolationException (예: 고유 키 충돌)과 같은 잠재적인 예외 사례를 우아하게 처리합니다. 이 접근 방식은 일반적인 회원가입 흐름을 단일 INSERT 쿼리로 줄입니다.

```java
// 효율적: 단일 쿼리, DB 제약 조건 활용 (낙관적)
try {
    memberRepository.save(member); // 쿼리 1
} catch (DataIntegrityViolationException e) {
    // 특정 제약 조건 위반 처리 (예: 이메일/닉네임 중복)
    handleConstraintViolation(e);
}
```

## 기타 사항

### 불필요한 DTO 제거

```java
// 제거된 중간 변환 객체
public class OAuthLoginInfo { /* 불필요한 래핑 */ }

// 불필요한 변환 발생
public record OAuthLoginRequest(
    /* 불필요한 래핑 */
) {
    public OAuthLoginInfo toDto() {
        Map<String, String> propertyMap = Map.of(
            AuthConstants.CODE, code,
            AuthConstants.REDIRECT_URI, redirectUri
        );
        return new OAuthLoginInfo(provider, propertyMap);
    }
}

// 객체 변환 간소화
final OAuthUser oAuthUser = providerFactory.getOAuthUser(oAuthLoginRequest);
```

### AuthExcpetion 추가

기존에는 인증/인가 관련 예외(Authentication, Authorization)를 포함해 모든 예외를 동일한 수준에서 처리하고 있었습니다. 이 방식은 간단하지만 다음과 같은 단점이 있습니다:

❗ 문제점
• 문제 추적이 어렵다: 인증 관련 문제인지, 비즈니스 로직 문제인지 구분되지 않음
• 모니터링/알림 설정이 어려움: 특정 보안 이슈에 대한 빠른 탐지가 불가능
• 책임 경계 불분명: 도메인 계층과 인증 계층의 에러가 동일하게 처리됨

⸻

🎯 개선 방향: AuthException 정의 및 분리
• 인증/인가 실패 상황(providerId 불일치, 로그인되지 않은 사용자, 토큰 유효성 문제 등)에 대해 별도 예외 클래스를 정의
• BaseException으로부터 상속, ErrorCode를 명확히 지정
• 차후 로그 필터링/슬랙 알림/보안 모니터링 등에서 인증 이슈만 별도로 추적 가능


