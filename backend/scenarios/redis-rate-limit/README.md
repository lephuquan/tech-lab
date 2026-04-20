# redis-rate-limit

## 1. Problem
Can gioi han tan suat request de bao ve API khoi bi spam/abuse theo user, API key hoac IP.

## 2. Architecture
- Spring Boot API layer
- Redis luu counter theo sliding window/token bucket
- Middleware/interceptor ap dung limit truoc khi vao business logic

## 3. How to run
1. `docker compose up -d` (Redis)
2. `./mvnw spring-boot:run`
3. Goi lap lai cung endpoint de xac minh han muc bi chan

## 4. When to use
- Public API can anti-abuse
- Login/OTP endpoint can han che brute-force
- Gateway/service can cap theo tenant/user
