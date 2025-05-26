# Redis 로컬 개발 환경 접속 가이드 (with AWS ElastiCache)

> 본 문서는 **VPC 내 ElastiCache Redis**에 대해, 로컬 개발 환경에서도 운영 환경과 동일한 방식으로 접근할 수 있도록 포트 포워딩 기반 개발 흐름을 정리한 가이드입니다.
> Bastion Host를 별도로 구성하지 않고, **기존 EC2 인스턴스를 SSM 포워딩 노드로 활용**합니다.

---

## ✅ 개요

| 항목        | 내용                                                  |
|-------------|-------------------------------------------------------|
| 대상 Redis  | AWS ElastiCache for Redis (Private Subnet)           |
| 접근 방식   | AWS Systems Manager - `PortForwardingSession` 사용   |
| 중계 노드   | 동일 VPC 내 EC2 인스턴스 (SSM Agent 연결 상태 필요) |

---

## 1. 요구 사항

### 1.1 사전 조건

- AWS CLI 설치 및 `configure` 완료
- EC2 인스턴스에 **SSM Agent 설치 + IAM Role 연결**되어 있어야 함
- Redis와 EC2는 동일 VPC/Subnet 내 존재
- Redis 보안 그룹에 EC2 인스턴스 허용 설정

---

## 2. 설정 단계

### 2.1 AWS CLI 인증 구성

```bash
aws configure --profile dev-redis
```

- Access Key, Secret, Region 입력
- 사용 목적에 맞게 별도 프로파일 구성 권장

---

### 2.2 EC2 인스턴스를 통한 포트 포워딩

1. EC2 인스턴스 ID 확인 (`i-xxxxxxxxxxxxxxxxx`)
2. SSM 포트 포워딩 세션 실행:

```bash
aws ssm start-session \
  --target i-xxxxxxxxxxxxxxxxx \
  --document-name AWS-StartPortForwardingSession \
  --parameters '{"portNumber":["6379"],"localPortNumber":["6379"]}' \
  --profile dev-redis
```

> 이 세션이 유지되는 동안 `localhost:6379`는 EC2 내부 Redis 포트에 직접 연결된 것과 동일하게 동작합니다.

---

### 2.3 연결 확인

```bash
valkey-cli --tls -h localhost -p 6379 ping
```

정상적으로 `PONG` 응답이 오면 연결 성공입니다.

---

### 2.4 Spring Boot 환경 구성 예시

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      ssl:
        enabled: true
```

- 운영/로컬 환경 모두 동일 구성 사용
- 운영에서는 EC2 → Redis 직접 연결
- 로컬에서는 포트포워딩 세션을 통해 동일 흐름 유지

---

## 3. Redis 연결 트러블슈팅

### 3.1 systemd 기반 socat 포워딩 관리 (옵션)

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable socat-redis
sudo systemctl start socat-redis
sudo systemctl status socat-redis
```

- 서비스 로그 확인:

```bash
journalctl -u socat-redis
```

---

## 4. 자주 발생하는 이슈

| 증상                             | 원인 및 해결 방안                                                                 |
|----------------------------------|------------------------------------------------------------------------------------|
| `Timeout` 또는 연결 안됨         | - SSM 세션이 종료되었거나<br>- Redis 보안 그룹에서 EC2 인바운드 허용 누락         |
| 포워딩 명령어 실행 시 오류 발생 | - EC2에 SSM Agent 미설치<br>- IAM Role에 `ssm:StartSession` 권한 미설정<br>- AWS CLI 인증 오류 |
| 데이터가 깨져 보임              | - Redis 클러스터 모드 사용 중<br>- Lettuce 클라이언트 설정을 클러스터 대응으로 변경 필요 |

---

## 📎 참고 자료

- [AWS Blog - Port Forwarding with SSM to ElastiCache Redis](https://aws.amazon.com/blogs/mt/aws-systems-manager-session-manager-port-forwarding-to-amazon-elasticache-redis-inside-private-subnet/)
- [PR #139](): 인증 전략 개선 및 Redis 기반 세션 관리 적용 상세 내역

---