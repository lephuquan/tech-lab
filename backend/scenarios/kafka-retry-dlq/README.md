# kafka-retry-dlq

## 1. Problem
Xu ly message loi trong he thong event-driven ma khong mat du lieu va khong lam nghen consumer chinh.

## 2. Architecture
- Topic chinh + retry topics theo cap do tre
- Dead-letter topic cho message khong the xu ly
- Consumer co backoff va error classification
- Dashboard/alert theo doi DLQ volume

## 3. How to run
1. `docker compose up -d` (Kafka)
2. `./mvnw spring-boot:run`
3. Phat sinh message loi de quan sat retry flow va DLQ

## 4. When to use
- He thong event co upstream khong on dinh
- Can dam bao at-least-once voi kha nang recover
- Muon tach operational flow cho su co message
