# Thứ tự triển khai code cho `es-product-search`

Tài liệu này mô tả thứ tự khuyến nghị để xây hoặc mở rộng scenario Elasticsearch mà vẫn giữ code clear.

---

## Phase 1 - Chốt bài toán và contract API

1. Chốt use case search (keyword + filter + suggest).
2. Chốt endpoint API cho index/search/suggest.
3. Chốt field của product document.

Output mong đợi: có contract rõ ràng để code không bị lan man.

---

## Phase 2 - Dựng hạ tầng Elasticsearch

1. Tạo `docker-compose.yml` cho ES + Kibana.
2. Khai báo `application.yml` (uri, index-name, port).
3. Tạo `ProductIndexService` để quản lý lifecycle index.

Output mong đợi: app boot và tạo index ổn định.

---

## Phase 3 - Indexing flow

1. Tạo `ProductDocument`.
2. Tạo API index một product.
3. Tạo seed data factory và endpoint seed.

Output mong đợi: có dữ liệu để query repeatable.

---

## Phase 4 - Search flow

1. Tạo `ProductSearchCriteria`.
2. Implement `ProductSearchQueryFactory`.
3. Implement search endpoint có paging và filters.

Output mong đợi: query keyword + filter ra kết quả đúng.

---

## Phase 5 - Suggest flow

1. Implement query prefix suggest.
2. Tạo endpoint suggest và giới hạn số kết quả.

Output mong đợi: người demo thấy rõ giá trị autocomplete căn bản.

---

## Phase 6 - Error handling + hardening

1. `GlobalExceptionHandler` cho response lỗi rõ ràng.
2. Validate request payload và query params.
3. Thêm endpoint reindex để reset nhanh trước demo.

Output mong đợi: dễ debug và dễ thao tác khi live demo.

---

## Phase 7 - Test và docs

1. Thêm unit test cho thành phần có logic độc lập.
2. Chạy `mvn test` cho scenario.
3. Hoàn thiện bộ docs trong `docs/` và README.

Output mong đợi: scenario dễ handover, dễ review, dễ demo.
