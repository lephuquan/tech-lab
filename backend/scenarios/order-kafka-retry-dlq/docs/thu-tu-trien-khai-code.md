# Thu tu trien khai code cho `order-kafka-retry-dlq`

Tai lieu nay mo ta thu tu khuyen nghi de phat trien hoac mo rong scenario ma khong bi vo flow.

---

## Phase 1 - Xac dinh contract va topic

1. Chot schema `OrderCreatedEvent`.
2. Chot ten topic chinh, retry topics, dlt topic.
3. Khai bao property trong `application.yml`.

Output mong doi: co contract ro rang de producer/consumer thong nhat.

---

## Phase 2 - Tao producer va endpoint trigger

1. Tao request DTO va validation.
2. Implement `OrderEventProducer`.
3. Expose API `POST /api/orders/events`.

Output mong doi: gui duoc message vao topic chinh.

---

## Phase 3 - Implement consumer happy path

1. Tao `OrderCreatedListener`.
2. Implement xu ly co ban trong `OrderProcessingService`.
3. Luu ket qua thanh cong vao persistence.

Output mong doi: case `failureMode=NONE` pass end-to-end.

---

## Phase 4 - Them retry va DLQ

1. Dinh nghia exception transient/permanent.
2. Wiring retry + DLT trong config Kafka.
3. Xu ly logic theo `FailureMode`.

Output mong doi:

- `TRANSIENT_ONCE` duoc retry roi thanh cong.
- `PERMANENT` vao DLT dung ky vong.

---

## Phase 5 - Quan sat va API doc ket qua

1. Tao entity/repository cho `OrderProcessingRecord`.
2. Them endpoint:
   - `GET /api/orders/records`
   - `GET /api/orders/records/{orderId}`
3. Chuan hoa response DTO de de doc khi demo.

Output mong doi: quan sat duoc toan bo trang thai xu ly.

---

## Phase 6 - Docker + demo hardening

1. Hoan thien `docker-compose.yml` cho Kafka + Kafka UI.
2. Test startup stack tren may sach.
3. Chuan hoa script demo 3 case NONE/TRANSIENT_ONCE/PERMANENT.

Output mong doi: co the demo on dinh, it thao tac.

---

## Phase 7 - Test va tai lieu hoa

1. Them/duy tri unit test cho logic phan loai loi.
2. Chay `mvn test` truoc moi lan demo.
3. Dong bo cac file:
   - `README.md`
   - `demo-yeu-cau.md`
   - `cach-doc-code-hieu-toan-bo.md`
   - `phan-tich-ky-thuat-tech-lab.md`

Output mong doi: scenario de handover, de review, de demo.
