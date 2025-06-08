# 🔒 로컬 HTTPS 개발 환경 구성 (`local.juulabel.com`)

Next.js 프론트엔드와 Spring Boot 백엔드를 위한 로컬 HTTPS 개발 환경 설정 가이드입니다. `https://local.juulabel.com` 도메인을 기준으로 양쪽 환경을 통합합니다.

---

## 🧩 1. 공통 호스트 설정

### ✅ `/etc/hosts` 파일 수정

- **macOS / Linux**:

  ```bash
  sudo nano /etc/hosts
  ```

- **Windows**:
  ```
  C:\Windows\System32\drivers\etc\hosts
  ```

#### 📌 내용 추가:

```
127.0.0.1   local.juulabel.com
```

---

## 🔐 2. HTTPS 인증서 생성

### ✅ `mkcert` 설치 후 인증서 생성:

```bash
mkcert local.juulabel.com
```

생성된 파일:

- `local.juulabel.com.pem` – 인증서
- `local.juulabel.com-key.pem` – 개인 키

---

## 🧭 프론트엔드 (Next.js)

### 📁 프로젝트 루트에 파일 배치:

```
juulabel-front/
├── local.juulabel.com.pem
├── local.juulabel.com-key.pem
├── server.cjs
```

### 🧾 `server.cjs`

```js
const { createServer } = require("https");
const { parse } = require("url");
const next = require("next");
const fs = require("fs");
const path = require("path");

const dev = process.env.NODE_ENV !== "production";
const app = next({ dev });
const handle = app.getRequestHandler();

const httpsOptions = {
  key: fs.readFileSync(path.join(__dirname, "local.juulabel.com-key.pem")),
  cert: fs.readFileSync(path.join(__dirname, "local.juulabel.com.pem")),
};

app.prepare().then(() => {
  createServer(httpsOptions, (req, res) => {
    const parsedUrl = parse(req.url, true);
    handle(req, res, parsedUrl);
  }).listen(3000, () => {
    console.log("✅ App running at https://local.juulabel.com:3000");
  });
});
```

### 📜 `package.json` 설정

```json
"scripts": {
  "dev:https": "node server.cjs"
}
```

실행:

```bash
pnpm run dev:https
```

---

## 🛡 백엔드 (Spring Boot)

### 📦 인증서 변환 (PKCS12)

```bash
openssl pkcs12 -export \
  -in local.juulabel.com.pem \
  -inkey local.juulabel.com-key.pem \
  -out local.juulabel.com.p12 \
  -name local-ssl
```

### 📁 keystore 파일 위치

`local.juulabel.com.p12` → `src/main/resources` 디렉토리에 복사

---

### ⚙️ `application.yml` 설정

```yaml
server:
  port: 8080
  ssl:
    enabled: true
    key-store: classpath:local.juulabel.com.p12
    key-store-password: your_password
    key-store-type: PKCS12
```

---

## ✅ 결과 요약

| 항목       | 주소                                      |
| ---------- | ----------------------------------------- |
| 프론트엔드 | `https://local.juulabel.com:3000`         |
| 백엔드     | `https://local.juulabel.com:8080`         |
| 쿠키       | Secure + SameSite=None + 동일 도메인 필요 |

---

## 📎 참고 사항

- 소셜 로그인 리디렉션 URI도 `https://local.juulabel.com` 기준으로 등록해야 합니다.
- 브라우저가 쿠키를 허용하려면:
  - `Secure: true`
  - `SameSite: None`
  - 도메인 일치 필요
