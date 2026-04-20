# Yêu cầu một demo (trong Tech Lab)

Tài liệu này mô tả **một demo cần có những gì** để vừa trình bày được ý tưởng, vừa tái lập được cho người xem sau sự kiện.

---

## 1. Mục tiêu rõ ràng

- **Bài toán**: Giải quyết điều gì (ví dụ: rate limit, retry + DLQ, multi-tenant routing).
- **Đối tượng**: Người nghe là tech hay non-tech; điều chỉnh độ sâu giải thích.
- **Kết quả mong đợi**: Sau demo, người xem hiểu *luồng dữ liệu*, *điểm nghẽn*, và *khi nào nên áp dụng*.

---

## 2. Môi trường chạy được (ưu tiên Docker + H2)

| Thành phần | Mục đích | Gợi ý cho demo |
|------------|----------|----------------|
| **Docker** | Kafka, Redis, mail mock, multi-service — đồng nhất máy demo | `docker compose up -d` trong thư mục scenario / `devops/docker/` |
| **H2 (in-memory hoặc file)** | DB không cần cài PostgreSQL/MySQL trên máy demo | Spring Boot `spring-boot-starter-data-jpa` + profile `demo` hoặc `application-demo.yml` |
| **Một lệnh chạy app** | Giảm lỗi thao tác khi live | `./mvnw spring-boot:run` hoặc `java -jar` sau build |

**Nguyên tắc**: Demo ngắn thì càng ít dependency ngoài (cài tay) càng tốt; H2 thay DB nặng, Docker gom phần còn lại.

---

## 3. Tài liệu kèm theo (tối thiểu)

- **README scenario** (theo quy ước repo): Problem → Architecture → How to run → When to use.
- **Một trang checklist** (có thể dùng `docs/huong-dan-demo-cac-kich-ban.md`) với bước 1, 2, 3… và lệnh copy-paste.
- **Biến môi trường / profile** ghi rõ (ví dụ: `SPRING_PROFILES_ACTIVE=demo,h2`).

---

## 4. Luồng thể hiện được (happy path + 1–2 biến thể)

- **Happy path**: Request/API hoặc message đi xuyên suốt hệ thống thành công.
- **Biến thể có ý nghĩa**: Lỗi có kiểm soát (429 rate limit, message vào DLQ, tenant sai) — mỗi biến thể gắn **một** insight kỹ thuật.

---

## 5. Quan sát được (observability tối thiểu)

- Log có **correlation id** hoặc ít nhất log theo bước (producer → consumer → DB).
- Health endpoint (`/actuator/health` nếu bật) hoặc endpoint kiểm tra đơn giản.
- (Tuỳ chọn) UI demo trong `frontend/` — không bắt buộc nếu demo chỉ tập trung backend.

---

## 6. Giới hạn phạm vi (để demo không “vỡ”)

- Một scenario chỉ **1–2 bài toán** rõ (đúng quy tắc vàng trong README gốc).
- Không phụ thuộc chéo sang scenario khác; tái sử dụng qua `shared-libs/` nếu cần.

---

## 7. Checklist nhanh trước khi demo

- [ ] `docker compose` (nếu có) chạy lên không lỗi port.
- [ ] Profile H2/demo chạy được không cần DB ngoài (nếu scenario có persistence).
- [ ] Đã thử lại toàn bộ kịch bản trong `huong-dan-demo-cac-kich-ban.md` trên máy sạch hoặc sau `docker compose down -v`.
- [ ] Ghi chú phiên bản: Java 17, Spring Boot 3.2.x (theo repo).

---

## Liên kết tài liệu khác trong `docs/`

- Thứ tự làm code: `thu-tu-trien-khai-code.md`
- Cách đọc code: `cach-doc-code-hieu-toan-bo.md`
- Phân tích kỹ thuật / trade-off: `phan-tich-ky-thuat-tech-lab.md`
- Kịch bản demo chi tiết: `huong-dan-demo-cac-kich-ban.md`
