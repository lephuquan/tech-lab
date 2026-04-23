# Cách đọc code để hiểu toàn bộ `es-product-search`

Tài liệu này hướng dẫn đọc code theo luồng dữ liệu và luồng truy vấn, không đọc theo alphabet.

---

## 1) Bắt đầu từ boundary

1. `README.md` của scenario: bài toán, scope, cách chạy.
2. `docker-compose.yml`: Elasticsearch + Kibana.
3. `application.yml`: port app, ES URI, tên index.

Bước này giúp bạn biết app dùng hệ thống bên ngoài nào và chạy ở đâu.

---

## 2) Entrypoint và config

Đọc theo thứ tự:

1. `EsProductSearchApplication`
2. `config/AppElasticsearchProperties`
3. `service/ProductIndexService`
4. `bootstrap/ProductIndexInitializer`

Sau bước này, bạn hiểu app tạo index như thế nào và tạo khi nào.

---

## 3) Đọc theo luồng nghiệp vụ chính

### Luồng A - Index một sản phẩm

`ProductController#indexOne` -> `ProductCatalogService#indexOne` -> Elasticsearch `save`.

### Luồng B - Search keyword + filters

`ProductController#search` -> `ProductCatalogService#search` -> `ProductSearchQueryFactory#buildSearchQuery` -> Elasticsearch `search`.

### Luồng C - Suggest prefix

`ProductController#suggest` -> `ProductCatalogService#suggestByPrefix` -> `match_phrase_prefix`.

### Luồng D - Demo reset nhanh

`POST /api/products/reindex-sample` -> recreate index -> seed default catalog.

---

## 4) Điểm cần đọc kỹ

- `ProductSearchQueryFactory`: nơi tập trung relevance + bộ lọc.
- `ProductIndexService`: mapping và lifecycle index.
- `ProductSeedDataFactory`: dữ liệu mẫu phục vụ demo repeatable.
- `GlobalExceptionHandler`: response lỗi để dễ debug.

---

## 5) Cách verify nhanh understanding

1. Chạy stack và app.
2. Gọi `POST /api/products/reindex-sample`.
3. Thử 3 query:
   - `keyword=iphone`
   - `keyword=laptop&minPrice=30000000`
   - `prefix=son`

Nếu bạn theo được 3 query này đến query DSL tương ứng, đã onboard xong scenario.
