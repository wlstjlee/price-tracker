# 📦 Price Tracker

관심 있는 쇼핑몰 상품의 URL을 등록하면, 주기적으로 최저가를 추적해서 가격 변동 이력을 보여주는 서비스입니다.


![데모](./docs/pricetracker.gif)

## 📌 소개

쇼핑몰 여러 곳을 돌아다니며 가격을 비교하는 게 번거로워서 만든 개인 프로젝트입니다.
관심 있는 상품의 URL을 등록해두면, 서버가 주기적으로 최신 가격을 조회해서 가격 변동 이력을 쌓아줍니다.

## ✨ 주요 기능

- [x] 상품 URL 입력을 통한 관심상품 등록 (쿠팡 지원, 웹 스크래핑 기반 상품 정보 자동 수집)
- [x] 관심상품 등록 / 조회 / 삭제
- [x] 가격 변동 이력 저장 및 조회
- [ ] 주기적 최저가 자동 갱신 (스케줄러) - 진행 중
- [ ] 다른 쇼핑몰(11번가, G마켓 등) 지원 확장 (예정)
- [ ] 회원가입 / 로그인 (예정)
- [ ] 목표가격 도달 시 이메일 알림 (예정)

## ⚠️ 지원 대상

현재는 **쿠팡(coupang.com) 상품 URL만 지원**합니다.

웹 스크래핑 방식은 특정 쇼핑몰의 HTML 구조에 맞춰 CSS 선택자를 지정하는 방식이라,
다른 쇼핑몰(11번가, G마켓 등) URL을 입력하면 상품 정보를 찾지 못해 파싱에 실패하며,
`400 Bad Request` 응답(`ProductParseFailedException`)을 받습니다.

향후 URL 도메인에 따라 파싱 로직을 분기하는 방식으로 다른 쇼핑몰 지원을 확장할 계획입니다.

## 🛠 사용 기술

- Java 17
- Spring Boot 
- Spring Data JPA
- MySQL
- Jsoup

## 📁 프로젝트 구조

```
com.github.wlstjlee.pricetracker
├── config          # 전역 설정 (JPA Auditing, RestTemplate 등)
├── controller       # API 엔드포인트
├── dto              # 요청/응답 DTO
├── entity           # JPA 엔티티
├── exception        # 커스텀 예외, 전역 예외 처리
├── repository       # JPA Repository
└── service          # 비즈니스 로직 (관심상품 관리, 웹 스크래핑)
```

## 📡 API 명세

| 기능 | Method | URL | 요청 | 응답 |
|---|---|---|---|---|
| 관심상품 등록 | POST | `/api/interests` | 쿠팡 상품 URL | 스크래핑된 상품 정보 |
| 관심상품 목록 조회 | GET | `/api/interests` | 없음 | 관심상품 리스트 |
| 관심상품 삭제 | DELETE | `/api/interests/{id}` | 없음 | 없음 (204) |
| 가격 이력 조회 | GET | `/api/interests/{id}/histories` | 없음 | 가격 변동 이력 리스트 |

## 🗂 ERD

```
InterestProduct (1) ──────< (N) PriceHistory
     id                          id
     name                        price
     url                         interest_product_id (FK)
     imageUrl                    createdAt
     mallName
     currentLowestPrice
     createdAt / updatedAt
```

## 🚀 실행 방법

```bash
git clone https://github.com/wlstjlee/price-tracker.git
cd price-tracker
```

`src/main/resources/application.properties` 파일을 생성하고 아래 내용을 채워주세요.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pricetracker
spring.datasource.username=본인계정
spring.datasource.password=본인비밀번호
spring.jpa.hibernate.ddl-auto=update
```

```bash
./gradlew bootRun
```

## 🐛 트러블슈팅

### 1. `created_at` / `updated_at`이 계속 NULL로 저장됨
- **원인**: `@EntityListeners`, `@CreatedDate`는 정상이었으나, `@EnableJpaAuditing`을 붙인 설정 클래스 자체를 만들지 않음
- **해결**: `config` 패키지에 `@EnableJpaAuditing`을 붙인 `JpaAuditingConfig` 추가

### 2. FK로 연결된 부모 테이블 데이터 삭제 시 에러
- **원인**: `PriceHistory`가 `InterestProduct`를 FK로 참조하고 있어, 부모를 먼저 삭제하면 데이터 정합성이 깨질 수 있어 DB가 차단
- **해결**: 애플리케이션 코드에서 자식(`PriceHistory`)을 먼저 삭제한 뒤 부모(`InterestProduct`)를 삭제하도록 `Service` 로직 수정

### 3. 네이버 쇼핑 검색 API 서비스 종료 (2026-07-31)
- **원인**: 네이버가 사전 공지 후 검색 API 중 쇼핑·책·전문자료 API를 유예기간 없이 전면 종료
- **대응 과정**:
    1. 쿠팡 파트너스 API 검토 → 블로그/웹사이트 등록 및 스크린샷 승인 절차 필요, 시간당 호출 10건 제한으로 개발 단계에서도 부담
    2. 11번가 오픈API 검토 → 셀러(사업자) 계정 및 고정 IP 등록 필요, 유동 IP를 쓰는 개인 개발 환경에 부적합
    3. 다나와 오픈API 검토 → 2019년에 이미 서비스 종료
    4. **최종적으로 Jsoup 기반 웹 스크래핑 방식으로 전환**
- **변경 내용**:
    - `NaverShoppingService`, `NaverSearchResponse`, `ProductSearchDTO`, `ProductSearchController` 삭제
    - `ProductParsingService`, `ProductParseResult` 신규 추가
    - 관심상품 등록 방식을 "검색 후 결과 선택"에서 "상품 URL 직접 입력"으로 변경
    - `InterestProduct`의 `naverProductId` 필드 제거 (저장된 URL로 재스크래핑하는 방식으로 대체)
- **배운 점**: 외부 API 의존 시 서비스 중단 리스크를 고려해야 하며, Controller/Service 계층을 분리해둔 덕분에 핵심 로직(외부 데이터 수집 방식)만 교체하고 Entity/Repository/Controller 등 나머지 구조는 그대로 재사용할 수 있었음
- **현재 한계**: 웹 스크래핑 방식은 특정 쇼핑몰의 HTML 구조에 종속적이라, 현재는 쿠팡 URL만 지원함. 다른 쇼핑몰을 지원하려면 각 사이트별로 별도의 파싱 로직(CSS 선택자)이 필요함

### 6. 쿠팡 웹 스크래핑 시 403 Forbidden 발생
- **원인**: 쿠팡이 자동화된 요청(봇)을 감지하는 보안 시스템을 갖추고 있어, Jsoup의 단순 HTTP 요청은 봇으로 판별되어 차단됨
- **시도한 방법**: User-Agent, Referer, Accept-Language 등 HTTP 헤더를 실제 브라우저와 유사하게 강화 → 여전히 403으로 차단됨
- **원인 분석**: Jsoup은 HTML만 가져올 뿐 JavaScript 실행, 쿠키 교환 등 실제 브라우저의 동작을 재현하지 못해 대형 쇼핑몰의 봇 탐지에 쉽게 걸림
- **다음 계획**: (진행 중 - Selenium 등 브라우저 자동화 도구 검토 또는 봇 차단이 약한 대상으로 전환 예정)

## 💭 배운 점

- JPA 연관관계(`@ManyToOne`, `mappedBy`, 연관관계의 주인)와 FK 제약이 실제 데이터 삭제에 미치는 영향
- Entity와 DTO의 책임 분리, 계층 간 의존 방향 원칙 (Entity는 DTO를 몰라야 함)
- 영속성 컨텍스트와 dirty checking을 이용한 안전한 데이터 수정
- 외부 API에 의존하는 서비스 설계 시 고려해야 할 리스크와, 계층 분리를 통한 변경 비용 최소화
- Git을 이용한 변경 이력 관리와, 트러블슈팅을 기록하는 습관의 중요성
