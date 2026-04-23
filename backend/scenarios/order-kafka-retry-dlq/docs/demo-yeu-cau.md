# Demo yeu cau cho `order-kafka-retry-dlq`

Tai lieu nay dinh nghia bo yeu cau toi thieu de demo scenario retry + dead-letter queue mot cach on dinh.

---

## 1) Muc tieu buoi demo

- Nguoi xem thay ro su khac nhau giua loi tam thoi va loi co dinh.
- Nguoi xem thay message di qua retry topics va DLT.
- Nguoi xem quan sat duoc ket qua xu ly qua API va Kafka UI.

---

## 2) Moi truong toi thieu

- Java 17
- Maven 3.9+
- Docker Desktop
- Port trong may:
  - `9092` cho Kafka broker
  - `8080` cho Kafka UI
  - `8083` cho Spring Boot app

---

## 3) Lenh chay truoc demo

Trong thu muc `backend/scenarios/order-kafka-retry-dlq`:

```bash
docker compose up -d
mvn spring-boot:run
```

Kiem tra nhanh:

- Kafka UI: `http://localhost:8080`
- Health: `http://localhost:8083/actuator/health`

---

## 4) Script demo bat buoc

### Case A - Happy path (`NONE`)

- Gui event voi `failureMode=NONE`
- Ky vong: xu ly thanh cong ngay, khong qua retry/dlt

### Case B - Retry roi thanh cong (`TRANSIENT_ONCE`)

- Gui event voi `failureMode=TRANSIENT_ONCE`
- Ky vong: fail lan dau, vao retry topic, xu ly thanh cong o lan sau

### Case C - Dead-letter (`PERMANENT`)

- Gui event voi `failureMode=PERMANENT`
- Ky vong: khong thanh cong sau cac lan retry, message vao DLT

---

## 5) Evidence can show tren man hinh

- Log app theo tung buoc consume/retry/dlt
- Kafka UI:
  - main topic `order.created.v1`
  - retry topics
  - dlt topic `order.created.v1.dlt`
- Endpoint:
  - `GET /api/orders/records`
  - `GET /api/orders/records/{orderId}`

---

## 6) Checklist truoc khi bat dau

- [ ] `docker compose` healthy
- [ ] App run duoc, khong loi bootstrap Kafka
- [ ] Da test lai 3 case NONE/TRANSIENT_ONCE/PERMANENT
- [ ] Da clear data cu neu can (`docker compose down -v`)

---

## 7) Gioi han pham vi

Buoi demo nay chi tap trung vao:

- Retry strategy
- Dead-letter queue
- Quan sat ket qua xu ly

Khong di sau vao auth, gateway, schema registry hay tracing phuc tap.
