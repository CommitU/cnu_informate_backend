# CNU Informate Backend 프로젝트 기능 정리

## 📋 프로젝트 개요

**프로젝트명**: CNU Informate Backend  
**설명**: CNU Generative AI Challenge를 위한 백엔드 시스템  
**기술 스택**: Spring Boot 3.5.4, Java 21, MySQL, JPA/Hibernate  
**포트**: 8080

---

## 🏗️ 시스템 아키텍처

### 기술 스택

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 21
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Gradle
- **Validation**: Spring Validation
- **WebSocket**: Spring WebSocket (준비됨)
- **Monitoring**: Spring Actuator

### 프로젝트 구조

```
src/main/java/com/commitU/informate/
├── InformateApplication.java (메인 애플리케이션)
├── calendar/ (일정 관리 모듈)
│   ├── controller/EventController.java
│   ├── service/EventService.java
│   ├── repository/EventRepository.java
│   ├── entity/Event.java
│   └── dto/EventCreateRequest.java
├── notice/ (공지사항 모듈)
│   ├── controller/NoticeController.java
│   ├── service/NoticeService.java
│   ├── repository/NoticeRepository.java
│   └── entity/Notice.java
└── user/ (사용자 모듈)
    └── entity/User.java
```

---

## 🗓️ 일정 관리 (Calendar) 모듈

### 주요 기능

#### 1. 일정 생성

- **기본 일정 생성**: 제목, 날짜, 사용자 정보로 일정 생성
- **공지사항 연동 일정 생성**: 공지사항을 기반으로 일정 자동 생성
- **유효성 검증**: 필수 필드 검증 및 비즈니스 로직 검증

#### 2. 일정 조회

- **단건 조회**: ID로 특정 일정 조회
- **사용자별 전체 일정**: 특정 사용자의 모든 일정 조회
- **기간별 일정**: 시작일~종료일 범위의 일정 조회 (달력 뷰용)
- **공지사항 연동 일정**: Notice와 연결된 일정만 조회

#### 3. 일정 관리

- **일정 수정**: 제목, 날짜, 공지사항 연결 정보 수정
- **일정 삭제**: ID로 일정 삭제

### API 엔드포인트

| HTTP Method | URL                                                     | 설명                       |
| ----------- | ------------------------------------------------------- | -------------------------- |
| POST        | `/api/events`                                           | 새 일정 생성               |
| POST        | `/api/events/from-notice`                               | 공지사항으로부터 일정 생성 |
| GET         | `/api/events/{id}`                                      | 일정 단건 조회             |
| GET         | `/api/events?userId={id}`                               | 사용자별 전체 일정         |
| GET         | `/api/events/range?userId={id}&start={date}&end={date}` | 기간별 일정 조회           |
| GET         | `/api/events/with-notice?userId={id}`                   | 공지사항 연동 일정 조회    |
| PUT         | `/api/events/{id}`                                      | 일정 수정                  |
| DELETE      | `/api/events/{id}`                                      | 일정 삭제                  |

### 데이터 모델 (Event)

```java
- id: Long (PK)
- title: String (제목, 필수)
- date: LocalDate (날짜, 필수)
- user: User (사용자, 필수)
- notice: Notice (연결된 공지사항, 선택)
```

---

## 📢 공지사항 (Notice) 모듈

### 주요 기능

#### 1. 공지사항 조회

- **전체 공지사항**: 모든 공지사항 조회
- **단건 조회**: ID로 특정 공지사항 조회
- **소스별 조회**: 특정 소스의 공지사항 조회
- **제목 검색**: 제목 기반 키워드 검색
- **활성 공지사항**: 마감일이 지나지 않은 공지사항
- **최근 공지사항**: 최근 10개 공지사항
- **기간별 조회**: 특정 기간의 공지사항 조회

#### 2. 추천 시스템

- **관심사 기반 추천**: 사용자 관심사에 따른 공지사항 추천
- **관련성 점수 계산**: 제목, 내용, 마감일, 게시일 기준 점수화
- **카테고리별 분류**: 12개 카테고리로 공지사항 분류

#### 3. 공지사항 관리

- **공지사항 생성**: 새로운 공지사항 등록
- **공지사항 수정**: 기존 공지사항 정보 수정
- **공지사항 삭제**: 공지사항 삭제

### API 엔드포인트

| HTTP Method | URL                                                       | 설명                 |
| ----------- | --------------------------------------------------------- | -------------------- |
| GET         | `/api/notices`                                            | 전체 공지사항 조회   |
| GET         | `/api/notices/{id}`                                       | 공지사항 단건 조회   |
| GET         | `/api/notices/source/{sourceId}`                          | 소스별 공지사항 조회 |
| GET         | `/api/notices/search?title={keyword}`                     | 제목 검색            |
| GET         | `/api/notices/active`                                     | 활성 공지사항 조회   |
| GET         | `/api/notices/recent`                                     | 최근 공지사항 조회   |
| GET         | `/api/notices/range?startDate={date}&endDate={date}`      | 기간별 조회          |
| GET         | `/api/notices/recommend?interests={keywords}&limit={num}` | 추천 공지사항        |
| GET         | `/api/notices/categories`                                 | 카테고리 목록        |
| GET         | `/api/notices/category/{categoryId}`                      | 카테고리별 공지사항  |
| POST        | `/api/notices`                                            | 공지사항 생성        |
| PUT         | `/api/notices/{id}`                                       | 공지사항 수정        |
| DELETE      | `/api/notices/{id}`                                       | 공지사항 삭제        |

### 데이터 모델 (Notice)

```java
- id: Long (PK)
- sourceId: Long (소스 ID, 필수)
- externalId: String (외부 ID)
- url: String (URL, 필수, 유니크)
- title: String (제목, 필수)
- content: String (내용)
- postedAt: LocalDateTime (게시일)
- scrapedAt: LocalDateTime (수집일, 필수)
- deadlineAt: LocalDateTime (마감일)
- hash: String (해시값)
```

### 공지사항 카테고리

1. **특강** (sourceId: 1)
2. **기획/마케팅** (sourceId: 2)
3. **취업/인턴십** (sourceId: 3)
4. **봉사 활동** (sourceId: 4)
5. **IT/SW** (sourceId: 5)
6. **스터디** (sourceId: 6)
7. **디자인** (sourceId: 7)
8. **창업** (sourceId: 8)
9. **영상/콘텐츠** (sourceId: 9)
10. **서포터즈/기자단** (sourceId: 10)
11. **학사 안내** (sourceId: 11)
12. **기타** (sourceId: 12)

---

## 👤 사용자 (User) 모듈

### 주요 기능

- **사용자 정보 관리**: 이메일, 비밀번호, 이름 관리
- **계정 생성**: 새로운 사용자 계정 생성
- **유효성 검증**: 이메일 형식, 비밀번호 길이 검증

### 데이터 모델 (User)

```java
- id: Long (PK)
- email: String (이메일, 필수, 유니크)
- passwordHash: String (비밀번호 해시, 필수, 최소 8자)
- name: String (이름)
- createdAt: LocalDateTime (생성일, 필수)
```

---

## 🔧 시스템 설정

### 환경 설정

- **개발 환경 (dev)**: 로컬 MySQL 사용
- **운영 환경 (prod)**: 환경변수 기반 설정
- **데이터베이스**: MySQL 8.0+
- **타임존**: Asia/Seoul

### 로깅 설정

- **개발**: SQL 쿼리 디버그 로깅
- **운영**: 일반 정보 로깅

### 보안 설정

- **비밀번호**: 최소 8자 이상
- **이메일**: 유효한 이메일 형식 검증
- **데이터 검증**: Spring Validation 사용

---

## 🚀 배포 및 실행

### 로컬 실행

```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

### Docker 실행

```bash
# Docker 이미지 빌드
docker build -t cnu-informate-backend .

# Docker 컨테이너 실행
docker run -p 8080:8080 cnu-informate-backend
```

### 환경변수 (운영)

- `SPRING_PROFILES_ACTIVE`: prod
- `DB_HOST`: 데이터베이스 호스트
- `DB_PORT`: 데이터베이스 포트
- `DB_NAME`: 데이터베이스 이름
- `DB_USER`: 데이터베이스 사용자
- `DB_PASS`: 데이터베이스 비밀번호

---

## 📊 주요 특징

### 1. 모듈화된 구조

- **Calendar**: 일정 관리 전용 모듈
- **Notice**: 공지사항 관리 전용 모듈
- **User**: 사용자 관리 전용 모듈

### 2. RESTful API 설계

- 표준 HTTP 메서드 사용
- 일관된 URL 패턴
- 적절한 HTTP 상태 코드 반환

### 3. 데이터 무결성

- JPA 엔티티 관계 설정
- 데이터베이스 제약조건
- 비즈니스 로직 검증

### 4. 확장 가능한 구조

- WebSocket 지원 (준비됨)
- Spring Actuator 모니터링
- 환경별 설정 분리

### 5. 추천 시스템

- 관심사 기반 공지사항 추천
- 다중 기준 점수화 알고리즘
- 실시간 관련성 계산

---

## 🔮 향후 개발 계획

### 현재 준비된 기능

- [x] 기본 CRUD 기능
- [x] RESTful API
- [x] 데이터 검증
- [x] 추천 시스템
- [x] 카테고리 분류

### 추가 개발 예정

- [ ] 사용자 인증/인가 (Spring Security)
- [ ] WebSocket 실시간 알림
- [ ] 파일 업로드 기능
- [ ] 캐싱 시스템
- [ ] API 문서화 (Swagger)
- [ ] 단위 테스트 보강
- [ ] 성능 최적화

---

## 📝 API 문서

자세한 API 문서는 `api.md` 파일을 참조하시기 바랍니다.

---

_마지막 업데이트: 2024년_
