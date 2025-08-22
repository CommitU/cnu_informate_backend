# 📢 CNU Informate Backend
> 대학 공지사항 개인화 추천 서비스

## 📋 프로젝트 소개
충남대학교 학생들을 위한 개인화된 공지사항 추천 및 일정 관리 시스템입니다. 사용자의 관심사를 기반으로 맞춤형 공지사항을 추천하고, 개인 일정 관리 기능을 제공합니다.

## 🎯 주요 기능

### 📢 공지사항 관리
- **전체 공지사항 조회**: 모든 공지사항 목록 확인
- **카테고리별 조회**: 특강, 취업/인턴십, IT/SW, 창업 등 12개 카테고리별 분류
- **개인화 추천**: 사용자 관심사 키워드 기반 맞춤형 공지사항 추천
- **검색 기능**: 제목 기반 공지사항 검색
- **기간별 조회**: 특정 기간 내 공지사항 필터링
- **최신순 조회**: 최근 게시된 공지사항 우선 노출

### 📅 개인 일정 관리
- **일정 생성**: 개인 일정 및 공지사항 연계 일정 생성
- **달력 뷰**: 월별/기간별 일정 조회
- **공지사항 연동**: 공지사항을 개인 일정으로 변환
- **일정 수정/삭제**: 생성된 일정 관리

### 🤖 AI 추천 알고리즘
- **제목 매칭**: 관심사 키워드가 제목에 포함 시 높은 점수 (3.0점)
- **내용 매칭**: 관심사 키워드가 내용에 포함 시 중간 점수 (1.0점)
- **마감일 가중치**: 마감일이 남은 공지사항 추가 점수 (0.5점)
- **최신성 가중치**: 최근 게시물일수록 높은 점수

## 🏗️ 프로젝트 구조

```
src/
├── main/
│   ├── java/com/commitU/informate/
│   │   ├── InformateApplication.java          # 메인 애플리케이션 클래스
│   │   ├── calendar/                          # 일정 관리 모듈
│   │   │   ├── controller/EventController.java    # 일정 API 컨트롤러
│   │   │   ├── service/EventService.java          # 일정 비즈니스 로직
│   │   │   ├── entity/Event.java                  # 일정 엔티티
│   │   │   ├── repository/EventRepository.java    # 일정 데이터 접근
│   │   │   └── dto/EventCreateRequest.java        # 일정 생성 DTO
│   │   ├── notice/                           # 공지사항 모듈
│   │   │   ├── controller/NoticeController.java   # 공지사항 API 컨트롤러
│   │   │   ├── service/NoticeService.java         # 공지사항 비즈니스 로직
│   │   │   ├── entity/Notice.java                 # 공지사항 엔티티
│   │   │   └── repository/NoticeRepository.java   # 공지사항 데이터 접근
│   │   └── user/                            # 사용자 모듈
│   │       └── entity/User.java                   # 사용자 엔티티
│   └── resources/
│       ├── application.yml                   # 애플리케이션 설정
│       ├── static/                          # 정적 리소스
│       └── templates/                       # 템플릿 파일
└── test/                                    # 테스트 코드
    └── java/com/commitU/informate/
        └── InformateApplicationTests.java
```

## 📡 API 명세서

### 📅 Calendar API (Event)
**Base URL**: `/api/events`

#### 주요 엔드포인트
- `POST /api/events` - 일정 생성
- `GET /api/events/{id}` - 일정 단건 조회
- `GET /api/events?userId={userId}` - 사용자별 일정 조회
- `GET /api/events/range` - 기간별 일정 조회 (달력 뷰용)
- `POST /api/events/from-notice` - 공지사항으로부터 일정 생성
- `PUT /api/events/{id}` - 일정 수정
- `DELETE /api/events/{id}` - 일정 삭제

### 📢 Notice API
**Base URL**: `/api/notices`

#### 주요 엔드포인트
- `GET /api/notices` - 모든 공지사항 조회
- `GET /api/notices/{id}` - 특정 공지사항 조회
- `GET /api/notices/recommend` - 🎯 **개인화 추천 공지사항 조회**
- `GET /api/notices/categories` - 📂 공지사항 카테고리 조회
- `GET /api/notices/category/{categoryId}` - 카테고리별 공지사항 조회
- `GET /api/notices/search?title={keyword}` - 제목 검색
- `GET /api/notices/recent` - 최근 공지사항 조회
- `GET /api/notices/active` - 활성 공지사항 조회

#### 🎯 추천 API 사용 예시
```bash
# 관심사 기반 추천
curl "http://localhost:8080/api/notices/recommend?interests=수강신청,장학금,취업&limit=5"

# 카테고리별 조회
curl "http://localhost:8080/api/notices/category/3"  # 취업/인턴십
curl "http://localhost:8080/api/notices/category/5"  # IT/SW
```

#### 📂 카테고리 목록
1. 특강 2. 기획/마케팅 3. 취업/인턴십 4. 봉사 활동
5. IT/SW 6. 스터디 7. 디자인 8. 창업
9. 영상/콘텐츠 10. 서포터즈/기자단 11. 학사 안내 12. 기타

**상세 API 문서**: [api.md](./api.md) 참고

## 💾 데이터베이스 구조

### 주요 엔티티
- **Event**: 사용자 일정 정보
- **Notice**: 공지사항 정보 (카테고리, URL, 마감일 포함)
- **User**: 사용자 정보

## 🚀 실행 방법

### 1. 환경 설정
- **Java**: 21 이상
- **MySQL**: 8.0 이상
- **Spring Boot**: 3.5.4

### 2. 데이터베이스 설정
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cnu_info
    username: your_username
    password: your_password
```

### 3. 애플리케이션 실행
```bash
# Gradle을 이용한 실행
./gradlew bootRun

# 또는 JAR 파일 빌드 후 실행
./gradlew build
java -jar build/libs/informate-0.0.1-SNAPSHOT.jar
```

### 4. 서버 확인
- **애플리케이션**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests InformateApplicationTests
```

## 🛠️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.4
- **Language**: Java 21
- **Database**: MySQL 8.0, H2 (테스트용)
- **ORM**: Spring Data JPA
- **Validation**: Spring Boot Validation
- **Build Tool**: Gradle

### Dependencies
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- Spring Boot Starter WebSocket
- Spring Boot Starter Actuator
- MySQL Connector
- Lombok

## 📄 라이선스
이 프로젝트는 CNU Generative AI Challenge의 일환으로 개발되었습니다.
