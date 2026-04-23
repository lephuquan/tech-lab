# order-kafka-retry-dlq

Scenario nay mo phong bai toan rat hay gap khi dung Kafka trong production: consumer gap loi tam thoi, can retry co backoff; consumer gap loi nghiep vu co dinh, can dua message vao DLQ de van hanh xu ly rieng.

## 1) Muc tieu demo

- Hieu Kafka dung de lam gi: tach producer/consumer, xu ly bat dong bo, giam tai request HTTP.
- Hieu retry + DLQ de tranh mat du lieu va tranh "nghen" consumer chinh.
- Hieu cach trien khai thuc te trong Spring Boot:
  - Producer publish event
  - Consumer xu ly va phan loai loi
  - Retry topics + DLQ topic
  - Endpoint quan sat ket qua xu ly

## 2) Bai toan

He thong nhan event `OrderCreatedEvent`.

- Loi tam thoi (vi du timeout downstream): can retry 1 vai lan, co do tre tang dan.
- Loi co dinh (vi du payload sai nghiep vu): khong retry vo han; dua vao DLQ de theo doi/replay sau.

Neu khong co retry + DLQ:
- Hoac mat message
- Hoac consumer lap vo han, lam tang lag va kho van hanh

## 3) Architecture (gian luoc nhung dung production pattern)

- Main topic: `order.created.v1`
- Retry topics: `order.created.v1.retry-0`, `order.created.v1.retry-1`
- DLQ topic: `order.created.v1.dlt`
- API:
  - `POST /api/orders/events`: gui event vao Kafka
  - `GET /api/orders/records`: xem trang thai xu ly
  - `GET /api/orders/records/{orderId}`: xem chi tiet 1 event
- Persistence: H2 (in-memory) de de demo tren may sach
- Quan sat:
  - Log app
  - Kafka UI (`http://localhost:8080`)
  - Actuator health (`/actuator/health`)

## 4) Cac case duoc demo

Event co field `failureMode` de chu dong tao tinh huong:

- `NONE`: xu ly thanh cong ngay.
- `TRANSIENT_ONCE`: fail lan dau, sau do retry thanh cong.
- `PERMANENT`: loi co dinh, di thang vao DLQ.

Day la cach demo de "nhin thay hanh vi Kafka", khong can phu thuoc he thong ngoai.

## 5) How to run

### 5.1 Start Kafka stack

```bash
cd backend/scenarios/order-kafka-retry-dlq
docker compose up -d
```

Cho den khi `kafka` healthy.

### 5.2 Run app

```bash
mvn spring-boot:run
```

App mac dinh chay port `8083`.

### 5.3 Trigger demo requests

#### Case A - Happy path

```bash
curl -X POST http://localhost:8083/api/orders/events \
  -H "Content-Type: application/json" \
  -d '{
    "orderId":"ORDER-1001",
    "customerEmail":"alice@example.com",
    "totalAmount":125.50,
    "failureMode":"NONE"
  }'
```

#### Case B - Retry roi thanh cong

```bash
curl -X POST http://localhost:8083/api/orders/events \
  -H "Content-Type: application/json" \
  -d '{
    "orderId":"ORDER-1002",
    "customerEmail":"bob@example.com",
    "totalAmount":89.00,
    "failureMode":"TRANSIENT_ONCE"
  }'
```

#### Case C - DLQ

```bash
curl -X POST http://localhost:8083/api/orders/events \
  -H "Content-Type: application/json" \
  -d '{
    "orderId":"ORDER-1003",
    "customerEmail":"carol@example.com",
    "totalAmount":49.90,
    "failureMode":"PERMANENT"
  }'
```

#### Kiem tra trang thai

```bash
curl http://localhost:8083/api/orders/records
```

## 6) Gia tri thuc te khi ap dung production

- Retry chi danh cho loi tam thoi (timeout, network, service tam unavailable).
- DLQ la "vung an toan" de khong mat du lieu khi gap poison message.
- Team van hanh co the monitor DLQ volume, alert, va replay theo quy trinh rieng.
- Mo rong tiep theo:
  - them schema registry / avro
  - idempotency key de chong process trung
  - metric/trace day du (Prometheus + Grafana + OpenTelemetry)

## 7) Scope duoc gioi han de giu scenario trong tam

Scenario nay chu tap trung 1 bai toan: **retry + DLQ**.

Khong lam trong pham vi nay:
- khong tach nhieu microservice
- khong them auth/gateway
- khong them workflow phuc tap

Muc tieu la code de doc, de demo, de hieu cach Kafka di vao he thong that.
