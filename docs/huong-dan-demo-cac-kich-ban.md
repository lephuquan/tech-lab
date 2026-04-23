# Hướng dẫn demo — các kịch bản cần thiết

Tài liệu này mô tả **các case demo đủ dùng** (không dàn trải), mỗi case có **mục đích**, **bước**, và **điều cần thấy được**. Ưu tiên: **Docker** cho broker/cache/mock, **H2** cho DB khi scenario có persistence (cấu hình qua profile `demo` / `h2` trong code thực tế — nếu chưa có, dùng các bước dưới đây làm checklist khi bổ sung).

**Tiền đề chung**

- Java 17, Maven wrapper nếu có: `./mvnw` (Linux/macOS) hoặc `mvnw.cmd` (Windows).
- Từ thư mục scenario: `backend/scenarios/<ten-scenario>/`.
- Nếu README scenario chỉ dẫn khác tài liệu này, **ưu tiên README scenario**.

---

## A. Kịch bản nền: “Repo & quy ước” (5 phút)

| Mục đích | Cho thấy monorepo có lớp học + scenario, quy tắc vàng. |
| Điều thấy được | Người xem hiểu vì sao tách thư mục, không nhét mọi thứ vào một app. |

**Bước**

1. Mở `README.md` gốc, chỉ cấu trúc cây thư mục và mục “Quy tắc vàng”.
2. Mở README một scenario (ví dụ `redis-rate-limit`), chỉ 4 phần: Problem / Architecture / How to run / When to use.

---

## B. `redis-rate-limit` — Giới hạn tần suất (Docker Redis)

| Mục đích | Demo **chống spam / bảo vệ endpoint** bằng Redis. |
| Điều thấy được | Sau N request, client nhận **429** (hoặc tương đương); counter reset theo cửa sổ thời gian. |

**Bước**

1. `docker compose up -d` (Redis — theo compose của scenario khi đã có file).
2. Chạy ứng dụng: `./mvnw spring-boot:run` (hoặc lệnh trong README scenario).
3. Gọi cùng một endpoint lặp lại nhanh (curl hoặc Postman).

**Case con**

- **B1 — Happy path**: Request dưới ngưỡng → luôn 200.
- **B2 — Vượt ngưỡng**: Vượt limit → 429 hoặc body lỗi rõ ràng.
- **Giải thích**: So sánh “không có rate limit” (hệ thống dễ kiệt) vs “có Redis tập trung” (nhiều instance API vẫn dùng chung counter).

---

## C. `mail-kafka-batch` — Hàng đợi + xử lý theo lô (Docker Kafka)

| Mục đích | Demo **tách tiếp nhận và xử lý**, giảm spike tải. |
| Điều thấy được | Producer đẩy message; consumer xử lý batch; hệ thống không phải đồng bộ nặng trong request HTTP. |

**Bước**

1. `docker compose up -d` (Kafka + phụ trợ theo scenario).
2. Khởi động app; gửi một loạt event/job (API hoặc script theo README).
3. Quan sát log consumer (batch commit, hoặc số bản ghi xử lý).

**Case con**

- **C1 — Luồng thành công**: Message đi từ API → topic → consumer → side effect (mail mock / log).
- **C2 — Tải**: Gửi burst message → hệ thống không “chết” tại HTTP thread (giải thích back-pressure ở mức khái niệm).

---

## D. `order-kafka-retry-dlq` — Retry và Dead Letter Queue

| Mục đích | Demo **lỗi tạm thời vs lỗi không hồi phục**; không mất message. |
| Điều thấy được | Retry với backoff; sau hết lần thử → message sang **DLQ** để xử lý tay. |

**Bước**

1. `docker compose up -d` (Kafka).
2. Trigger consumer xử lý message “lỗi có chủ đích” (theo hướng dẫn code: ví dụ flag test).
3. Xác minh topic DLQ hoặc log/table theo thiết kế.

**Case con**

- **D1 — Lỗi tạm thời**: Retry thành công.
- **D2 — Lỗi cố định**: Vào DLQ; giải thích vận hành (replay, cảnh báo).

---

## E. `multi-tenant-db` — Định tuyến dữ liệu theo tenant (H2 + Docker tùy thiết kế)

| Mục đích | Demo **cô lập dữ liệu** theo tenant (schema/DB/row-level — tuỳ implementation). |
| Điều thấy được | Request tenant A không đọc/ghi nhầm tenant B. |

**Bước (ưu tiên H2 cho demo)**

1. Bật profile **`demo`** hoặc **`h2`** (khi đã cấu hình): datasource H2 in-memory hoặc file.
2. Khởi động app **không** cần cài Postgres trên máy demo (nếu profile H2 đã có).
3. Gọi API với header/context tenant A và tenant B.

**Case con**

- **E1 — Đúng tenant**: Dữ liệu khớp tenant.
- **E2 — Tenant sai / thiếu**: 403/400 rõ ràng — thể hiện **ranh giới an toàn**.

---

## F. `notification-system` — Kết hợp nhiều thành phần (Docker Kafka + Redis)

| Mục đích | Demo **pipeline thông báo**: nhận sự kiện → xử lý → gửi kênh; có thể có dedupe/cache. |
| Điều thấy được | Nhiều service/broker phối hợp; điểm cần idempotency và timeout. |

**Bước**

1. `docker compose up -d` (Kafka + Redis — tối thiểu theo README scenario).
2. Phát một sự kiện kích hoạt thông báo.
3. Kiểm tra log hoặc mock provider (email/push).

**Case con**

- **F1 — End-to-end thành công**.
- **F2 — Redis down hoặc Kafka chậm** (tuỳ mức độ đã implement): graceful degradation hoặc lỗi có ý nghĩa.

---

## G. `fundamentals` (tùy buổi demo)

| Mục đích | Chứng minh nền tảng Java (async, collection, OOP) **tách** khỏi scenario infra. |
| Điều thấy được | Code nhỏ, chạy test/local đơn giản. |

**Bước**: Vào `fundamentals/java-core/multithreading/...`, đọc README, chạy ví dụ/test nếu có.

---

## H. `es-product-search` — Full-text search + filter + suggest

| Mục đích | Demo Elasticsearch cho bài toán tìm kiếm sản phẩm gần thực tế. |
| Điều thấy được | Relevance theo keyword, lọc theo thuộc tính, và gợi ý prefix. |

**Bước**

1. `docker compose up -d` trong `backend/scenarios/es-product-search` (Elasticsearch + Kibana).
2. Chạy app: `mvn spring-boot:run`.
3. Seed dữ liệu: `POST /api/products/reindex-sample`.
4. Chạy query:
   - `GET /api/products/search?keyword=iphone`
   - `GET /api/products/search?keyword=laptop&minPrice=30000000&inStockOnly=true`
   - `GET /api/products/suggest?prefix=son&limit=5`

**Case con**

- **I1 — Search keyword**: Thấy score và thứ tự relevance.
- **I2 — Search + filter**: Kết hợp full-text với điều kiện nghiệp vụ.
- **I3 — Suggest prefix**: Trải nghiệm autocomplete cơ bản.

---

## I. Dọn dẹch sau demo

```bash
docker compose down
# Nếu cần xóa volume (reset Kafka/Redis state):
docker compose down -v
```

---

## Bảng tóm tắt: Case vs Stack

| Scenario / nhóm | Docker | H2 (khi có DB) |
|-----------------|--------|----------------|
| redis-rate-limit | Redis | — |
| mail-kafka-batch, order-kafka-retry-dlq | Kafka | Tuỳ (thường không bắt buộc DB cho demo tối thiểu) |
| es-product-search | Elasticsearch (+ Kibana) | — |
| multi-tenant-db | Tuỳ | **Ưu tiên** cho demo máy sạch |
| notification-system | Kafka + Redis | Tuỳ |

---

## Liên kết

- `demo-yeu-cau.md` — checklist chung một buổi demo.
- `thu-tu-trien-khai-code.md` — thứ tự triển khai để README và compose khớp với các bước trên.
