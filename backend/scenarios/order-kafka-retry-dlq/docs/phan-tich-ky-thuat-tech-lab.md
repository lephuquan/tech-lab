# Phan tich ky thuat cho scenario `order-kafka-retry-dlq`

Tai lieu nay tom tat "vi sao" scenario duoc thiet ke nhu hien tai va nhung trade-off can nam.

---

## 1) Bai toan ky thuat

Khi consumer gap loi:

- Loi tam thoi (network timeout, downstream cham) -> nen retry.
- Loi co dinh (du lieu sai nghiep vu) -> khong nen retry vo han, can day vao DLT.

Neu khong tach 2 loai loi, he thong de bi:

- mat message
- lag consumer tang cao
- kho van hanh va kho debug

---

## 2) Cach thiet ke trong scenario

- Producer day event vao `order.created.v1`.
- Consumer chinh xu ly message.
- Retry topics dung de "tri hoan" xu ly lai.
- DLT la noi gom poison messages de xu ly van hanh sau.
- H2 dung de luu `OrderProcessingRecord` phuc vu quan sat nhanh.

---

## 3) Gia tri hoc duoc

- Hieu semantics retry va DLQ trong he thong event-driven.
- Hieu cach to chuc exception theo y nghia van hanh:
  - `TransientProcessingException`
  - `PermanentProcessingException`
- Hieu tam quan trong cua tracking status xu ly thay vi chi xem log.

---

## 4) Trade-off hien tai

- Uu diem:
  - Demo gon, de tai hien tren may sach.
  - Tap trung dung trong tam 1 bai toan.
- Han che:
  - Chua co idempotency key de chong process trung.
  - Chua co metric/tracing day du cho production.
  - H2 phu hop hoc/demo hon la benchmark production.

---

## 5) Huong mo rong tiep theo

1. Them idempotency o tang consumer/service.
2. Them metric retry count, dlt count va alert.
3. Them co che replay tu DLT co kiem soat.
4. Neu can sat production hon: doi profile persistence sang Postgres.

---

## 6) Khi nen dung pattern nay

- He thong co messaging bat dong bo.
- Co kha nang xuat hien loi tam thoi va poison message.
- Can dam bao "khong mat du lieu" trong xu ly event.

Khong can phuc tap pattern nay neu bai toan la CRUD don gian, dong bo, tai nho.
