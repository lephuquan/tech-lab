# Demo yêu cầu cho `es-product-search`

Tài liệu này định nghĩa bộ yêu cầu tối thiểu để demo Elasticsearch rõ ràng, ngắn gọn, và sát nhu cầu thực tế.

---

## 1) Mục tiêu buổi demo

- Giải thích được Elasticsearch là gì và khi nào nên dùng.
- Trình bày được giá trị relevance trong full-text search.
- Trình bày được kết hợp keyword search + filter trong cùng query.
- Trình bày được quy trình index dữ liệu và suggest prefix.

---

## 2) Môi trường tối thiểu

- Java 17
- Maven 3.9+
- Docker Desktop
- Các port:
  - `8084` app
  - `9200` Elasticsearch
  - `5601` Kibana

---

## 3) Lệnh chạy trước demo

Trong `backend/scenarios/es-product-search`:

```bash
docker compose up -d
mvn spring-boot:run
```

Kiểm tra nhanh:

- `http://localhost:9200`
- `http://localhost:5601`
- `http://localhost:8084/actuator/health`

---

## 4) Script demo bắt buộc

### Step 1 - Seed dữ liệu

`POST /api/products/reindex-sample`

### Step 2 - Full-text search

`GET /api/products/search?keyword=iphone`

Kỳ vọng: trả kết quả smartphone liên quan và có score.

### Step 3 - Search + filter

`GET /api/products/search?keyword=laptop&minPrice=30000000&inStockOnly=true`

Kỳ vọng: kết hợp relevance và điều kiện nghiệp vụ.

### Step 4 - Suggest

`GET /api/products/suggest?prefix=son&limit=5`

Kỳ vọng: trả danh sách tên sản phẩm bắt đầu theo prefix.

---

## 5) Evidence cần show trên màn hình

- Response JSON của endpoint search có:
  - danh sách item
  - total
  - score cho từng item
- Kibana Dev Tools/Discover cho thấy index có dữ liệu.
- Log app cho thấy luồng recreate index và seed.

---

## 6) Checklist trước khi bắt đầu

- [ ] Elasticsearch healthy
- [ ] App kết nối được ES
- [ ] Đã seed lại index thành công
- [ ] Đã thử đủ 3 case: keyword, filter, suggest

---

## 7) Giới hạn phạm vi

Demo này chỉ tập trung:

- Full-text search
- Filtering
- Prefix suggestion

Không demo:

- typo tolerance nâng cao
- synonym dictionary
- vector search
- data sync từ DB production.
