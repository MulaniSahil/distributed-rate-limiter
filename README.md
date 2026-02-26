\# 🚀 Distributed \& Configurable API Rate Limiter



A pluggable and production-ready API Rate Limiting framework built using \*\*Spring Boot 3\*\*, \*\*Java 17\*\*, and \*\*Redis\*\*.



---



\## 👨‍💻 Author

\*\*Sahil Mulani\*\*



---



\## 📌 Project Overview



This project implements a configurable and extensible API Rate Limiter using:



\- Strategy Pattern for pluggable algorithms

\- Token Bucket \& Fixed Window algorithms

\- In-Memory and Redis-backed storage

\- HTTP 429 enforcement

\- Standard rate-limit headers

\- Configuration-driven switching via YAML



---



\## 🏗 Architecture



\- `RateLimitAlgorithm` → Strategy abstraction

\- `TokenBucketAlgorithm` → Token bucket implementation

\- `FixedWindowAlgorithm` → Fixed window implementation

\- `RateLimitStorage` → Storage abstraction

\- `InMemoryStorage` → Local storage

\- `RedisStorage` → Distributed storage

\- `RateLimitFilter` → HTTP filter enforcement layer



---



\## ⚙️ Features



✔ Pluggable rate limiting algorithms  

✔ Distributed Redis support  

✔ Configurable via `application.yml`  

✔ Standard HTTP Headers:

\- `X-RateLimit-Limit`

\- `X-RateLimit-Remaining`

\- `Retry-After`

✔ Returns `429 Too Many Requests`  



---



\## 🛠 Tech Stack



\- Java 17

\- Spring Boot 3

\- Maven

\- Redis

\- Lombok

\- Docker (optional for Redis)



---



\## 🚀 Running the Application



\### 1️⃣ Build



```bash

mvn clean package



2️⃣ Run

mvn spring-boot:run



Application runs on:



http://localhost:8080



🧪 Testing



Test endpoint:



GET /api/protected



Rapid requests will return:



HTTP 429 Too Many Requests

🔄 Switch Storage Type



In application.yml:



rate-limiter:

&nbsp; storage:

&nbsp;   type: memory   # or redis

📈 Future Improvements



Sliding Window Algorithm



JWT-based user rate limiting



Unit testing (JUnit + Mockito)



Distributed atomic token bucket using Lua scripts



Performance benchmarking



📜 License



This project is for educational and portfolio purposes.





---



Save → Close → Then run:



```powershell

git add README.md

git commit -m "Added professional README"

git push

✅ FINAL CHECKLIST



Make sure your repo contains:



src/

pom.xml

README.md

