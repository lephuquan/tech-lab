# Tech Lab - Scenario-based System Design Playground

Kho monorepo hoc va thuc hanh backend theo huong "scenario-based":
- Hoc concept tach biet trong `fundamentals/`
- Xay "mini production systems" trong `backend/scenarios/`
- Tai su dung code chung qua `backend/shared-libs/`
- Van hanh moi truong/CI qua `devops/`
- Demo UI rieng trong `frontend/`
- Thu nghiem nhanh trong `playground/`

---

## 1) Cấu trúc repo chuẩn

```text
tech-lab/
├─ fundamentals/                        # kien thuc nen (doc lap, nho, khong infra)
│  └─ java-core/
│     └─ multithreading/
│        ├─ async/
│        ├─ oop/
│        └─ collections/
│
├─ backend/                             # backend system (trong tam)
│  ├─ scenarios/                        # moi scenario = 1 project doc lap
│  │  ├─ mail-kafka-batch/
│  │  ├─ redis-rate-limit/
│  │  ├─ multi-tenant-db/
│  │  ├─ order-kafka-retry-dlq/
│  │  └─ notification-system/
│  │
│  ├─ shared-libs/                      # thu vien tai su dung (phase sau mo rong)
│  │  ├─ kafka-lib/
│  │  ├─ redis-lib/
│  │  └─ common-lib/
│  │
│  └─ templates/                        # boilerplate tao nhanh project moi
│     └─ spring-boot-template/
│
├─ devops/                              # moi truong + deploy + CI/CD
│  ├─ docker/
│  │  ├─ kafka-stack/
│  │  ├─ redis-stack/
│  │  └─ multi-service/
│  ├─ cicd/
│  │  ├─ github-actions/
│  │  ├─ gitlab-ci/
│  │  └─ jenkins/
│  └─ k8s/                              # optional
│
├─ frontend/                            # UI demo doc lap
│  ├─ react-basic/
│  └─ react-notification-ui/
│
├─ playground/                          # khu thu nghiem linh tinh (duoc "ban")
└─ README.md                            # overview toan repo
```

---

## 2) Vì sao cấu trúc này bền vững

### Tach theo "muc dich hoc" (learning layers)
- `fundamentals`: hoc concept co ban, de test nhanh.
- `backend`: xay backend giong production thu nho.
- `devops`: dong goi moi truong, CI/CD, deploy.
- `frontend`: UI demo rieng, khong rang buoc backend.
- `playground`: cho phep thu nghiem nhanh ma khong lam ban cau truc chinh.

### Scenario-based la diem an tien
- Doi ten demo theo bai toan thuc te: `mail-kafka-batch`, `redis-rate-limit`, ...
- Recruiter/co-worker nhin folder ten la hieu van de dang giai.
- Moi scenario la mot "system nho" co API + storage + message flow rieng.

### Doc lap va scale tot
- Them scenario moi khong anh huong scenario cu.
- Co the run/CI tung scenario doc lap.
- De split thanh nhieu repo rieng khi can.

### Co duong tien hoa len he thong that
- Scenario `notification-system` co the ket hop Kafka + Redis + Mail.
- Tu "demo hoc tap" co the nang cap thanh architecture production.

---

## 3) Quy tac vang (bat buoc tuan thu)

1. **Moi scenario phai chay doc lap**
   - Vi du:
     - `cd backend/scenarios/mail-kafka-batch`
     - `docker compose up -d`
     - `./mvnw spring-boot:run`

2. **Moi scenario chi giai 1-2 bai toan ro rang**
   - Tot: `redis-rate-limit`, `order-kafka-retry-dlq`
   - Khong tot: `redis-all-in-one`, `kafka-everything`

3. **Dat ten co nghia theo bai toan**
   - Tranh: `demo1`, `test-redis`, `kafka-demo`
   - Nen: `mail-kafka-batch-processing`, `redis-rate-limiting`, `multi-tenant-routing`

4. **Scenario README bat buoc co 4 phan**
   - Problem (bai toan)
   - Architecture
   - How to run
   - When to use

5. **Khong phu thuoc cheo giua scenarios**
   - Scenario A khong import truc tiep code scenario B.
   - Neu can tai su dung => dua len `backend/shared-libs/`.

6. **Playground duoc phep "ban", main branch phai sach**
   - Playground de POC nhanh.
   - Code dua vao scenario/main phai duoc don dep.

7. **Version thong nhat toan repo**
   - Java: 17
   - Spring Boot: 3.2.x
   - Build tool: Maven

---

## 4) Tai sao can `shared-libs/`

Neu khong co `shared-libs/`, moi scenario se viet lai cung mot loat config va helper:
- duplicate code
- kho maintain
- 1 bug phai fix nhieu noi

Khi tach `shared-libs/`:
- viet 1 lan, dung nhieu noi
- giam sai lech implementation giua scenarios
- tang toc do tao scenario moi

### Goi y thanh phan trong shared libs
- `kafka-lib/`
  - KafkaConfig
  - BaseProducer
  - BaseConsumer
  - RetryHandler
- `redis-lib/`
  - RedisConfig
  - CacheService
  - DistributedLockService
  - RateLimitUtil
- `common-lib/`
  - BaseMessage
  - TenantAware helper
  - Constants
  - Utils

---

## 5) Cach dung `shared-libs/` trong scenario

Vi du trong `backend/scenarios/mail-kafka-batch/pom.xml`:

```xml
<dependency>
  <groupId>com.techlab</groupId>
  <artifactId>kafka-lib</artifactId>
  <version>1.0.0</version>
</dependency>
```

> Khuyen nghi: version hoa theo semver va su dung parent pom de dong bo.

---

## 6) Checklist tao scenario moi

1. Copy tu `backend/templates/spring-boot-template/`
2. Dat ten scenario theo bai toan cu the
3. Tao README theo template 4 phan bat buoc
4. Run local doc lap (app + dependency can thiet)
5. Them test toi thieu cho luong chinh
6. Bo sung pipeline CI rieng cho scenario neu can

---

## 7) Roadmap de xai repo nay hieu qua

- **Phase 1:** Lam vung fundamentals (`multithreading`, async, collections)
- **Phase 2:** Build 2-3 scenarios backend doc lap
- **Phase 3:** Rut code chung sang `shared-libs/`
- **Phase 4:** Chuan hoa Docker + CI/CD
- **Phase 5:** Them frontend demo + dashboard quan sat

---

## 8) Quy uoc tai lieu

- Root `README.md`: tong quan, quy tac, dinh huong architecture.
- Moi scenario: README mo ta bai toan va cach chay doc lap.
- Moi shared-lib: README mo ta API public, version, changelog ngan.

Neu ban follow dung bo quy tac tren, repo se de maintain, de scale va de trinh bay nhu mot "engineering portfolio" chuyen nghiep.
