# multi-tenant-db

## 1. Problem
Can phuc vu nhieu tenant tren cung he thong, dam bao tach biet du lieu va kiem soat truy cap theo tenant.

## 2. Architecture
- Tenant resolver (header/token/subdomain)
- Routing datasource theo tenant
- Tenant-aware repository/service layer
- Logging/tracing kem tenant id

## 3. How to run
1. Chuan bi DB cho 2 tenant mau
2. `./mvnw spring-boot:run`
3. Goi API voi tenant header khac nhau de kiem tra du lieu duoc tach biet

## 4. When to use
- SaaS B2B can phan tach du lieu
- Need on-board tenant moi nhanh
- Muon mo rong policy theo tenant (quota, config, feature flag)
