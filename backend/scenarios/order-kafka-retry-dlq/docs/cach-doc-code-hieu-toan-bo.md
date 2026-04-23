# Cach doc code de hieu toan bo `order-kafka-retry-dlq`

Tai lieu nay giup onboarding nhanh scenario `order-kafka-retry-dlq` theo cach "doc theo luong", khong doc ngau nhien.

---

## 1) Bat dau tu boundary

1. `README.md` trong cung thu muc: muc tieu demo, bai toan, case NONE/TRANSIENT_ONCE/PERMANENT.
2. `pom.xml`: stack chinh (Spring Boot, Kafka, JPA, H2, test).
3. `docker-compose.yml`: broker Kafka va Kafka UI, port map.

Muc tieu cua buoc nay la hieu app nay noi chuyen voi thanh phan nao truoc khi doc class.

---

## 2) Entrypoint va config

Doc theo thu tu:

1. `src/main/java/.../KafkaRetryDlqApplication.java`
2. `src/main/resources/application.yml`
3. `src/main/java/.../config/AppKafkaProperties.java`
4. `src/main/java/.../config/KafkaConfig.java`

Sau buoc nay, ban se ro ten topic, retry topic, dlt topic va cach listener duoc wiring.

---

## 3) Doc theo luong nghiep vu

### Luong 1: publish event

`OrderEventController` -> `OrderEventProducer` -> Kafka topic `order.created.v1`

### Luong 2: consume + xu ly + retry/dlq

`OrderCreatedListener` -> `OrderProcessingService` -> throw exception theo `FailureMode` -> retry topic hoac DLT -> `OrderProcessingRecordService` luu ket qua.

### Luong 3: quan sat ket qua

`OrderRecordController` -> `OrderProcessingRecordRepository`

Neu muon hieu nhanh, trace 3 luong nay voi 3 request mau trong `README.md`.

---

## 4) Domain va persistence

- Domain:
  - `OrderCreatedEvent`
  - `FailureMode`
  - `ProcessingStatus`
- Persistence:
  - `OrderProcessingRecord`
  - `OrderProcessingRecordRepository`

Can doi chieu mapping status voi hanh vi retry/DLQ de khong nham y nghia.

---

## 5) Error handling can doc ky

- `TransientProcessingException`: loi tam thoi, duoc retry.
- `PermanentProcessingException`: loi co dinh, day ve DLT.
- `GlobalExceptionHandler`: format loi cho endpoint REST.

Doan nay la trong tam cua scenario, nen doc sau khi da hieu happy path.

---

## 6) Test de xac nhan understanding

File test hien tai:

- `src/test/java/.../FailureSimulationServiceTest.java`

Ban nen chay:

```bash
mvn -f backend/scenarios/order-kafka-retry-dlq/pom.xml test
```

Neu test pass va ban trace duoc 3 mode failure, coi nhu da onboard xong scenario.
