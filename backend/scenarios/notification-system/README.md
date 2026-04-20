# notification-system

## 1. Problem
Can gui thong bao da kenh (email, push, SMS) theo su kien, co uu tien, retry va theo doi trang thai gui.

## 2. Architecture
- API nhan request tao notification
- Kafka event bus cho async processing
- Redis cho cache/idempotency/rate control
- Channel workers (email/push/sms) + retry/DLQ

## 3. How to run
1. `docker compose up -d` (Kafka + Redis + mock services neu co)
2. `./mvnw spring-boot:run`
3. Tao notification qua API va kiem tra trang thai processing

## 4. When to use
- Can mo rong nhieu kenh thong bao
- Can luong gui lon va bat dong bo
- Can theo doi SLA va ty le that bai theo channel
