# es-product-search

Scenario nay mo phong bai toan rat hay gap khi dung Elasticsearch: xay tim kiem san pham co relevance, co loc theo thuoc tinh, va co goi y prefix de nang trai nghiem nguoi dung.

## 1) Muc tieu demo

- Hieu Elasticsearch la gi trong he thong backend:
  - Search engine toi uu cho full-text + relevance ranking.
  - Khac voi query SQL thuong o bai toan tim kiem text.
- Hieu app thuc te se trien khai nhu the nao:
  - Tao index + mapping.
  - Index du lieu.
  - Search theo keyword + bo loc category/price/stock.
  - Suggest theo prefix.
- Co du quy trinh de demo nhanh tren may sach bang Docker.

## 2) Bai toan

Backend thuong co nhu cau:

- Tim kiem catalog theo tu khoa ("iphone", "laptop developer", ...)
- Loc theo category, khoang gia, con hang
- Goi y nhanh khi nguoi dung dang go

Neu dung database thuong cho full-text quy mo vua/lon:

- Relevance ranking yeu
- Query text + filter de rat nhanh tro nen kho khan
- Trai nghiem search khong "mang chat tim kiem"

## 3) Architecture (gian luoc nhung sat thuc te)

- `Spring Boot` app (`8084`) cung cap API index/search/suggest.
- `Elasticsearch` (`9200`) la storage + search engine.
- `Kibana` (`5601`) de quan sat index/query trong demo.
- Luong chinh:
  - `POST /api/products/reindex-sample` -> tao lai index + nap du lieu mau
  - `GET /api/products/search` -> keyword + filters
  - `GET /api/products/suggest` -> goi y prefix

## 4) Cac case duoc demo

- Case A - Search keyword:
  - `keyword=iphone` -> ket qua smartphone lien quan dung relevance.
- Case B - Search + filter:
  - `keyword=laptop&minPrice=30000000&inStockOnly=true`
- Case C - Prefix suggest:
  - `prefix=son` -> goi y "Sony WH-1000XM5"
- Case D - Truy van thuoc tinh:
  - `category=accessory` de thay filter keyword field.

## 5) How to run

### 5.1 Start Elasticsearch stack

```bash
cd backend/scenarios/es-product-search
docker compose up -d
```

Khi stack healthy:

- Elasticsearch: `http://localhost:9200`
- Kibana: `http://localhost:5601`

> Neu Docker Desktop bao thieu memory cho ES, tang RAM cap cho Docker len >= 4GB.

### 5.2 Run app

```bash
mvn spring-boot:run
```

App chay mac dinh tai `http://localhost:8084`.

### 5.3 Seed du lieu

```bash
curl -X POST http://localhost:8084/api/products/reindex-sample
```

### 5.4 Demo search/suggest

```bash
curl "http://localhost:8084/api/products/search?keyword=iphone&page=0&size=5"
curl "http://localhost:8084/api/products/search?keyword=laptop&minPrice=30000000&inStockOnly=true"
curl "http://localhost:8084/api/products/search?category=accessory"
curl "http://localhost:8084/api/products/suggest?prefix=son&limit=5"
```

## 6) Gia tri thuc te khi ap dung production

- Tach search workload khoi database giao dich.
- Ranking theo muc do lien quan thay vi chi "like %text%".
- Dung filter + full-text ket hop trong cung truy van.
- Co the mo rong:
  - analyzer theo ngon ngu
  - typo tolerance/fuzzy
  - synonym
  - metrics query latency va relevance tuning.

## 7) Scope duoc gioi han de giu scenario trong tam

Scenario nay chi tap trung vao:

- Full-text search + filters + prefix suggest.
- Quy trinh index/create/recreate de demo.

Khong lam trong pham vi nay:

- auth/gateway
- phan quyen
- CDC tu DB sang Elasticsearch
- architecture microservice phuc tap.

## 8) Tai lieu docs bo sung

Sau khi doc README nay, xem tiep:

- `docs/cach-doc-code-hieu-toan-bo.md`
- `docs/demo-yeu-cau.md`
- `docs/phan-tich-ky-thuat-tech-lab.md`
- `docs/thu-tu-trien-khai-code.md`
