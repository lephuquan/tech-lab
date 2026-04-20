# mail-kafka-batch

## 1. Problem
Can xu ly gui mail theo lo (batch) voi throughput cao, giam tai cho mail provider va dam bao co the retry khi loi tam thoi.

## 2. Architecture
- REST API nhan request tao campaign
- Kafka giu hang doi event gui mail
- Batch worker doc event theo dot
- Dead-letter/retry strategy cho message that bai

## 3. How to run
1. `docker compose up -d` (Kafka + phu tro)
2. `./mvnw spring-boot:run`
3. Goi API tao campaign va theo doi log batch worker

## 4. When to use
- Gui mail so luong lon
- Can tach producer/consumer ro rang
- Can quan tri retry va giam sat message processing
