# Redis 로컬 개발 환경 연결 설정 가이드 (with AWS ElastiCache)

> 운영 환경의 Redis(VPC 내부)와 로컬 개발 환경을 동일하게 연결하기 위한 구성 절차를 정리한 문서입니다.  
> 비용 효율성과 실용성을 위해 별도의 Bastion Host 없이 EC2 백엔드 인스턴스를 포트 포워딩용 중계 노드로 활용합니다.

---

## 1. 기본 개념 및 요구 사항

- **Redis 위치**: AWS ElastiCache for Redis (VPC 내부, 퍼블릭 액세스 불가)
- **로컬 개발 환경 연결 방식**: AWS Systems Manager(SSM)의 `PortForwardingSession` 사용
- **전제 조건**:
  - AWS CLI 설치 및 설정 완료
  - EC2 인스턴스에 SSM Agent 설치 및 IAM Role 연결
  - EC2와 Redis가 동일 VPC/Subnet 내에 존재
  - ElastiCache Redis의 보안 그룹에 EC2 인스턴스 허용 설정

---

## 2. 설정 절차

### 2.1 AWS CLI 구성

1. 인증 키 생성 후 `.csv` 파일 다운로드 (예: `EcPortForwarding_accessKeys.csv`)
2. AWS CLI에 프로파일 등록:

```bash
aws configure --profile [your-profile-name]
```

- `Access Key ID`, `Secret Access Key`, `Region` 입력

> 예시:
> ```
> aws configure --profile dev-redis
> ```

---

### 2.2 EC2 포트 포워딩 세션 시작

1. EC2 인스턴스 ID 확인 (SSM 접속이 가능한 상태여야 함)
2. 아래 명령어로 Redis 포트(6379) 포워딩:

```bash
aws ssm start-session   --target i-0xxxxxxxxxxxxxxx   --document-name AWS-StartPortForwardingSession   --parameters '{"portNumber":["6379"],"localPortNumber":["6379"]}'   --profile dev-redis
```

> 🔁 위 명령어를 실행하면 **로컬의 `localhost:6379`** 로 접근 시, 해당 EC2 인스턴스 내부에서 Redis에 접속하는 것과 동일한 효과를 가집니다.

---

## 3. Spring Boot 설정 (`application.yml`)

로컬과 운영 환경 모두에서 동일하게 설정합니다:

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

> 운영에서는 EC2 내부에서 Redis에 직접 접근 가능  
> 로컬에서는 포트 포워딩 세션을 통해 동일하게 동작

---

## 4. 자주 발생하는 문제 및 해결법

| 증상                                     | 원인 및 해결 방법                                                                 |
|------------------------------------------|------------------------------------------------------------------------------------|
| `Timeout` 또는 연결 불가                  | SSM 세션이 끊어졌거나 Redis 보안 그룹이 EC2를 허용하지 않음                      |
| 포트 포워딩 명령어 실행 시 에러 발생     | EC2 인스턴스에 SSM Agent 미설치, IAM Role 누락, 혹은 CLI 인증 프로파일 오류      |
| Redis 연결은 되나 데이터가 이상하게 보임 | Redis 클러스터 모드가 활성화된 경우, Lettuce 설정을 클러스터 모드로 변경 필요     |

---

## 5. 유의 사항 및 권장 전략

- Bastion Host 불필요 → 비용 및 인프라 단순화
- `localhost:6379`을 고정하여 개발/운영 동일한 코드 사용 가능
- 보안 강화를 위해 EC2에 최소 권한 IAM Role 부여 및 Redis 보안 그룹 제한
- 필요시 HAProxy 도입으로 로컬 클러스터 라우팅 테스트도 가능

---

## 6. 참고 자료

- [AWS 공식 블로그: Session Manager Port Forwarding to Redis](https://aws.amazon.com/blogs/mt/aws-systems-manager-session-manager-port-forwarding-to-amazon-elasticache-redis-inside-private-subnet/)
- [PR #139](링크): 인증 구조 리팩토링 및 Redis 도입 관련 변경 내역