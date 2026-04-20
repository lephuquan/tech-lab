# Thứ tự triển khai code (từ đầu đến hoàn thành)

Áp dụng cho **một scenario mới** hoặc **mở rộng** trong monorepo Tech Lab. Thứ tự giúp tránh refactor vòng: nền tảng → hợp đồng → luồng chính → nhánh lỗi → đóng gói demo.

---

## Phase 0 — Chuẩn bị repo & quy ước

1. Đọc `README.md` gốc: quy tắc vàng, cấu trúc thư mục, phiên bản Java/Spring/Maven.
2. Xác định **tên scenario** theo bài toán (không dùng `demo1`, `test-x`).
3. Nếu cần boilerplate: copy từ `backend/templates/spring-boot-template/` (khi template đã có mã nguồn đầy đủ).

---

## Phase 1 — Định nghĩa bài toán & ranh giới

1. Viết **Problem** và **When to use / When not** trong README scenario (có thể chỉnh dần).
2. Liệt kệ dependency ngoài: **chỉ những gì thật sự cần** (Redis, Kafka, …).
3. Quyết định **H2** cho persistence demo (profile `demo` / `h2`) vs DB thật cho môi trường dev đầy đủ.

---

## Phase 2 — Hợp đồng API & mô hình dữ liệu

1. Định nghĩa REST API (hoặc message schema) — request/response hoặc event payload.
2. Entity/repository (nếu có JPA) — schema đơn giản, migration hoặc `ddl-auto` phù hợp profile H2.
3. Validation + mã lỗi HTTP/message rõ ràng (để demo “lỗi có ý nghĩa”).

---

## Phase 3 — Luồng nghiệp vụ chính (happy path)

1. Service layer: một luồng đọc được từ controller/consumer → service → repository/producer.
2. Cấu hình Spring tối thiểu (`@Configuration`, beans cần thiết).
3. Test đơn vị hoặc integration test cho **một** luồng chính (theo checklist README gốc).

---

## Phase 4 — Hạ tầng kèm theo (Docker trước, tách profile)

1. Thêm `docker-compose.yml` (hoặc dùng stack trong `devops/docker/…`) — cổng, volume, healthcheck.
2. `application.yml` + `application-docker.yml` / `application-demo.yml`: URL Redis/Kafka trỏ tới container; H2 cho profile demo.
3. Kiểm tra: `docker compose up -d` rồi `./mvnw spring-boot:run` với profile đúng.

---

## Phase 5 — Nhánh lỗi, giới hạn, an toàn

1. Rate limit / retry / DLQ / tenant filter — **chỉ** phần gắn với bài toán đã chọn.
2. Timeout, idempotency (nếu có message/event).
3. Log + (tuỳ chọn) metrics tối thiểu để demo giải thích được.

---

## Phase 6 — Tách tái sử dụng (khi lặp lại ≥ 2 scenario)

1. Di chuyển config/helper chung lên `backend/shared-libs/` (`kafka-lib`, `redis-lib`, `common-lib`).
2. Cập nhật `pom.xml` scenario phụ thuộc artifact nội bộ; giữ version thống nhất.

---

## Phase 7 — Hoàn thiện “demo-ready”

1. README scenario: đủ 4 phần bắt buộc + **lệnh chạy với Docker + H2** nếu áp dụng.
2. Đồng bộ `docs/huong-dan-demo-cac-kich-ban.md` với thứ tự bước thật trên máy.
3. (Tuỳ chọn) Frontend trong `frontend/` nếu cần UI quan sát.

---

## Phase 8 — CI & chất lượng (sau khi scenario ổn định)

1. Pipeline riêng scenario (trong `devops/cicd/…`) nếu team cần.
2. `mvn verify` trên CI với profile không cần Docker (mock/testcontainers tuỳ chiến lược).

---

## Sơ đồ thứ tự tóm tắt

```text
Quy ước & tên scenario
        ↓
API / schema / model
        ↓
Happy path (service + test tối thiểu)
        ↓
Docker compose + profile (H2 cho DB demo)
        ↓
Lỗi & giới hạn (429, DLQ, …)
        ↓
shared-libs (khi cần)
        ↓
README + tài liệu demo + CI
```

---

## Liên kết

- `demo-yeu-cau.md` — checklist nội dung một buổi demo.
- `cach-doc-code-hieu-toan-bo.md` — cách đọc ngược lại từ code đã triển khai.
- `phan-tich-ky-thuat-tech-lab.md` — bối cảnh “vì sao thứ tự như vậy”.
