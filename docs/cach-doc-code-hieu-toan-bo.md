# Cách đọc code để hiểu toàn bộ (Tech Lab)

Monorepo này tách theo **lớp học** và **scenario**. Cách đọc hiệu quả là “vào từ biên → vào lõi”, không đọc file ngẫu nhiên.

---

## 1. Bắt đầu từ ranh giới hệ thống

1. **`README.md` (gốc)** — Cây thư mục, quy tắc vàng, phiên bản stack.
2. **`backend/scenarios/<ten-scenario>/README.md`** — Problem, architecture, cách chạy, khi nào dùng.
3. **`pom.xml` của scenario** — Phụ thuộc: Spring, Kafka client, Redis, H2, test…

Bạn sẽ biết **ứng dụng là gì** và **nó nói chuyện với thế giới bên ngoài** như thế nào trước khi đọc class.

---

## 2. Điểm vào chương trình (entrypoints)

Với Spring Boot, tìm theo thứ tự:

| Thứ tự | Nơi thường gặp | Câu hỏi khi đọc |
|--------|----------------|-----------------|
| 1 | `*Application.java` (`main`) | Module nào được bật? Profile mặc định? |
| 2 | `application.yml`, `application-*.yml` | Port, datasource (H2 vs JDBC thật), Kafka/Redis URL |
| 3 | `controller/` hoặc `web/` | API surface — danh sách endpoint |
| 4 | `consumer/`, `listener/` | Topic, group id, xử lý message |

Đọc xong bốn lớp này là có **bản đồ luồng request/event**.

---

## 3. Đọc theo luồng dữ liệu (không đọc theo alphabet)

Chọn **một** luồng (ví dụ: `POST /orders` hoặc `consume topic X`):

```text
Controller / Listener
        ↓
   Service (domain logic)
        ↓
 Repository / Producer / RedisTemplate
        ↓
   DB / Broker / Cache
```

- Mở **một** endpoint hoặc **một** listener, trace xuống service rồi repository/client.
- Ghi chú nhanh: “ai gọi ai” — sau 30 phút bạn có sơ đồ mental model.

---

## 4. Cấu hình & profile (rất quan trọng với H2 + Docker)

- **`spring.profiles.active`**: `demo`, `docker`, `local` — thường quyết định H2 in-memory vs Postgres, host `localhost` vs tên service Docker.
- **`spring.datasource`**: driver H2, `jdbc:h2:mem:…` hoặc file — đọc để biết dữ liệu demo có **persist** giữa restart không.
- **Bean có `@ConditionalOnProperty`**: logic chỉ chạy khi bật feature — đừng bỏ qua khi “code có nhưng không chạy”.

---

## 5. Thư mục `fundamentals/`

- Mục đích: **không** gắn infra nặng; học Java core, multithreading, collections.
- Đọc **README** trong từng mục nhỏ trước; code (nếu có) thường là ví dụ ngắn, độc lập.

---

## 6. Thư mục `backend/shared-libs/`

- Đọc **README** của từng lib (`kafka-lib`, `redis-lib`, `common-lib`) để biết **API public** và trách nhiệm.
- Trong scenario: tìm import `com.techlab...` hoặc groupId trong `pom.xml` — từ đó nhảy sang lib.

---

## 7. `devops/` và `frontend/`

- **`devops/docker/`**: Compose stack — đọc để biết **thứ tự khởi động** và port map.
- **`frontend/`**: Chỉ cần khi demo có UI; đọc `package.json` / README và điểm gọi API backend.

---

## 8. Chiến lược “hiểu nhanh trong 1 buổi”

1. **30 phút**: README scenario + `application*.yml` + một luồng happy path.
2. **1 giờ**: Thêm nhánh lỗi (429, exception handler, DLQ).
3. **Buổi thứ hai**: Shared libs + test + Docker networking giữa services.

---

## 9. Dấu hiệu nên đọc sâu hơn

- Class có nhiều `@Transactional` hoặc gọi ra ngoài trong transaction — hiểu boundary.
- Retry + idempotency — đọc kỹ để không hiểu sai semantics.
- Multi-tenant — filter ở đâu (connection, schema, row-level).

---

## Liên kết

- `thu-tu-trien-khai-code.md` — Thứ tự *viết* code khớp với thứ tự *đọc* ngược từ Phase 7 về Phase 1 khi onboarding.
- `phan-tich-ky-thuat-tech-lab.md` — Bối cảnh kiến thức nên có khi đọc sâu.
