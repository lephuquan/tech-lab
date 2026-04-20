# Phân tích kỹ thuật: Tech Lab là gì?

---

## 1. Định nghĩa ngắn

**Tech Lab** là monorepo học và thực hành **thiết kế hệ thống theo kịch bản (scenario-based)**:

- Học nền tảng tách biệt trong `fundamentals/`.
- Xây các hệ backend thu nhỏ trong `backend/scenarios/` — mỗi thư mục là một bài toán có ranh giới rõ.
- Chuẩn hóa vận hành qua `devops/` (Docker, CI/CD).
- UI demo tùy chọn trong `frontend/`.

Không phải một sản phẩm thương mại hoàn chỉnh, mà là **sân chơi có kỷ luật** để luyện architecture và kỹ năng triển khai.

---

## 2. Dùng để làm gì?

| Mục đích | Mô tả |
|----------|--------|
| Học có lộ trình | Từ Java core → scenario có Kafka/Redis/DB |
| Portfolio / phỏng vấn | Tên folder theo bài toán thực tế, dễ giải thích |
| Chuẩn hóa cách làm việc | Quy tắc: scenario độc lập, README 4 phần, không phụ thuộc chéo |
| Thử nghiệm an toàn | `playground/` cho POC; main giữ sạch |

---

## 3. Lợi ích

- **Tách concern**: Fundamentals không vướng infra; scenario tập trung 1–2 vấn đề.
- **Scale tổ chức code**: Thêm scenario mới ít đụng code cũ; CI có thể tách theo project.
- **Tái sử dụng có kiểm soát**: `shared-libs/` tránh copy-paste vô hạn.
- **Gần production**: Docker cho broker/cache; có thể mở rộng K8s sau.
- **Demo nhẹ với H2**: Không cần cài DB server trên máy trình chiếu nếu profile demo dùng H2.

---

## 4. Bất lợi / đánh đổi (trade-off)

| Trade-off | Giải thích |
|-----------|------------|
| Monorepo lớn dần | Clone nặng, build toàn repo có thể chậm nếu không tách CI theo module |
| Đồng bộ phiên bản | Java 17 + Spring Boot 3.2.x bắt buộc thống nhất — nâng cấp phải kế hoạch |
| Shared lib “đông cứng” | Lib dùng chung sai thiết kế sẽ lan lỗi sang nhiều scenario |
| H2 ≠ production DB | Hành vi transaction/locking khác Postgres/MySQL — chỉ nên cho **demo/học**, không kết luận hiệu năng thật |
| Docker trên máy yếu | Kafka stack nặng hơn Redis-only; demo cần chọn stack tối thiểu |

---

## 5. Khi nên dùng mô hình này

- Bạn muốn **nhiều bài toán nhỏ** thay vì một app khổng lồ không ranh giới.
- Bạn cần **chứng minh** hiểu message queue, cache, rate limit, tenant… qua repo có cấu trúc.
- Team / cá nhân muốn **quy ước** rõ: README, Docker, profile, không import chéo scenario.

---

## 6. Khi không nên (hoặc nên giản lược)

- **Một** dự án sản phẩm duy nhất cần release nhanh — không cần monorepo scenario.
- Học **một** chủ đề rất hẹp (ví dụ chỉ thuật toán) — `fundamentals/` đủ, không cần full stack.
- Môi trường không chạy được Docker — phải chuyển sang mock/Testcontainers hoặc cloud, làm phức tạp demo ngắn.

---

## 7. Kiến thức **bắt buộc** nắm (để làm việc hiệu quả trong repo)

- **Java 17** cơ bản: package, OOP, exception, generics.
- **Maven**: lifecycle `compile`, `test`, `package`; đọc `pom.xml` và parent/module.
- **Spring Boot**: dependency injection, `@RestController`, cấu hình `application.yml`, **Spring profiles**.
- **HTTP/API** hoặc **messaging** (tuỳ scenario): request/response vs producer/consumer.
- **Docker Compose**: `up`, `down`, port, tên service mạng nội bộ.
- **Git**: branch, PR nhỏ; quy tắc playground vs main.

---

## 8. Kiến thức **nên** nhớ (nâng chất lượng scenario)

- **JPA/H2**: `ddl-auto`, khác biệt dialect; khi nào dùng H2 cho demo.
- **Redis**: TTL, atomic increment — rate limiting.
- **Kafka**: topic, partition, consumer group, semantics at-least-once.
- **Resilience**: timeout, retry, DLQ, idempotency.
- **Observability**: structured logging, correlation id, health checks.

---

## 9. Kiến thức **không cần** nhớ thuộc lòng

- Toàn bộ API Spring từng annotation (tra tài liệu khi cần).
- Cấu hình chi tiết từng phiên bản Kafka broker (chỉ cần hiểu vai trò và luồng).
- Toàn bộ spec Kubernetes — trừ khi bạn đang học track `devops/k8s/`.
- Lệnh SQL vendor-specific nếu scenario demo đang dùng H2 đơn giản.

---

## 10. Gợi ý stack demo (ưu tiên như yêu cầu dự án)

1. **H2** cho persistence trong buổi demo nội bộ / máy sạch.
2. **Docker** chỉ cho thành phần **bắt buộc** của bài toán (Redis hoặc Kafka, không dư).
3. Khi cần “sát production” hơn: chuyển profile sang Postgres/MySQL + compose riêng.

---

## Liên kết

- `demo-yeu-cau.md` — checklist nội dung demo.
- `huong-dan-demo-cac-kich-ban.md` — kịch bản cụ thể theo scenario.
