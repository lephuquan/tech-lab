# Phân tích kỹ thuật cho scenario `es-product-search`

Tài liệu này tóm tắt lý do thiết kế và trade-off của scenario Elasticsearch product search.

---

## 1) Bài toán kỹ thuật

Product catalog cần:

- search full-text nhanh
- ranking kết quả theo độ liên quan
- filter theo category, giá, trạng thái còn hàng

Nếu dùng SQL query đơn thuần cho bài toán này:

- relevance ranking hạn chế
- query text + filter phức tạp và khó tối ưu
- scale search khó khăn hơn khi dữ liệu lớn dần

---

## 2) Cách thiết kế trong scenario

- Spring Boot giữ vai trò API layer.
- Elasticsearch là engine index + query.
- Kibana để quan sát và debug index.
- Logic được tách rõ:
  - index lifecycle: `ProductIndexService`
  - query building: `ProductSearchQueryFactory`
  - orchestration: `ProductCatalogService`

---

## 3) Giá trị học được

- Hiểu quy trình index và mapping trong dự án thực tế.
- Hiểu relevance và score trong kết quả search.
- Hiểu cách kết hợp full-text query với filter nghiệp vụ.
- Hiểu cách tạo demo repeatable qua reindex + seed.

---

## 4) Trade-off hiện tại

- Ưu điểm:
  - Dễ đọc, dễ mở rộng, dễ demo.
  - Scope tập trung 1 bài toán rõ ràng.
- Hạn chế:
  - Chưa tune analyzer theo tiếng Việt chuyên sâu.
  - Chưa có typo tolerance/fuzzy.
  - Chưa có pipeline đồng bộ từ DB giao dịch.

---

## 5) Hướng mở rộng tiếp theo

1. Thêm custom analyzer + synonym dictionary.
2. Thêm fuzzy query và từ khóa gõ sai.
3. Thêm observability cho search latency và cache hit.
4. Thêm indexing pipeline từ CDC hoặc event streaming.

---

## 6) Khi nên dùng pattern này

- App có nhu cầu tìm kiếm text trung tâm (catalog, knowledge base, logs, ...).
- Cần relevance ranking và response nhanh.
- Muốn tách search workload khỏi transactional DB.

Nếu bài toán chỉ là CRUD nhỏ, query exact-match đơn giản, chưa cần Elasticsearch.
