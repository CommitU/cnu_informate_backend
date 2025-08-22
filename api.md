# API 명세서

## 📅 Calendar API (Event)

### Base URL: `/api/events`

#### 1. 일정 생성
```http
POST /api/events
Content-Type: application/json

{
  "title": "테스트 일정",
  "startAt": "2025-08-25T09:00:00",
  "endAt": "2025-08-26T18:00:00",
  "userId": "1",
  "description": "선택적 설명",
  "location": "선택적 위치",
  "category": "선택적 카테고리",
  "allDay": false
}
```

**Response:**
```json
{
  "id": 13,
  "title": "테스트 일정",
  "startAt": "2025-08-25T09:00:00",
  "endAt": "2025-08-26T18:00:00",
  "allDay": false,
  "userId": "1",
  "description": null,
  "location": null,
  "category": null
}
```

#### 2. 일정 단건 조회
```http
GET /api/events/{id}
```

#### 3. 사용자별 일정 조회
```http
GET /api/events?userId=user1
```

#### 4. 기간별 일정 조회 (달력 뷰용)
```http
GET /api/events/range?userId=user1&start=2025-08-01T00:00:00&end=2025-08-31T23:59:59
```

#### 5. 제목으로 일정 검색
```http
GET /api/events/search?userId=user1&title=회의
```

#### 6. 카테고리별 일정 조회
```http
GET /api/events/category?userId=user1&category=업무
```

#### 7. 일정 수정
```http
PUT /api/events/{id}
Content-Type: application/json

{
  "title": "수정된 일정",
  "startAt": "2025-08-25T10:00:00",
  "endAt": "2025-08-26T19:00:00",
  "userId": "1"
}
```

#### 8. 일정 삭제
```http
DELETE /api/events/{id}
```

---

## 📢 Notice API

### Base URL: `/api/notices`

#### 1. 모든 공지사항 조회
```http
GET /api/notices
```

**Response:**
```json
[
  {
    "id": 1,
    "sourceId": 1,
    "externalId": null,
    "url": "https://plus.cnu.ac.kr/_prog/_board/?mode=V&no=2508345...",
    "title": "2025학년도 제2학기 예비수강신청 계획 안내",
    "content": "",
    "postedAt": null,
    "scrapedAt": "2025-08-11T19:27:30",
    "deadlineAt": null,
    "hash": "8fea480bafa48b300d908f6f1b3fd083833e901f56917f4218841802d47e2e4e"
  }
]
```

#### 2. 특정 공지사항 조회
```http
GET /api/notices/{id}
```

#### 3. 소스별 공지사항 조회
```http
GET /api/notices/source/{sourceId}
```

#### 4. 제목으로 공지사항 검색
```http
GET /api/notices/search?title=입학
```

#### 5. 활성 공지사항 조회 (마감일 지나지 않은 것)
```http
GET /api/notices/active
```

#### 6. 최근 공지사항 조회 (최근 10개)
```http
GET /api/notices/recent
```

#### 7. 기간별 공지사항 조회
```http
GET /api/notices/range?startDate=2025-08-01T00:00:00&endDate=2025-08-31T23:59:59
```

#### 8. 공지사항 생성
```http
POST /api/notices
Content-Type: application/json

{
  "sourceId": 1,
  "url": "https://example.com",
  "title": "새로운 공지사항",
  "content": "공지사항 내용",
  "postedAt": "2025-08-22T10:00:00",
  "deadlineAt": "2025-08-30T23:59:59"
}
```

#### 9. 공지사항 수정
```http
PUT /api/notices/{id}
Content-Type: application/json

{
  "title": "수정된 공지사항",
  "content": "수정된 내용"
}
```

#### 10. 공지사항 삭제
```http
DELETE /api/notices/{id}
```

#### 11. 🎯 개인화 추천 공지사항 조회
```http
GET /api/notices/recommend?interests=수강신청,장학금,취업&limit=5
```

**파라미터:**
- `interests` (선택): 사용자 관심사 키워드 (쉼표, 세미콜론, 공백으로 구분)
- `limit` (선택): 반환할 최대 개수 (기본값: 10)

**Response:**
```json
[
  {
    "id": 1,
    "title": "2025학년도 제2학기 예비수강신청 계획 안내",
    "sourceId": 1,
    "url": "https://plus.cnu.ac.kr/_prog/_board/?mode=V&no=2508345...",
    "content": "",
    "postedAt": null,
    "scrapedAt": "2025-08-11T19:27:30",
    "deadlineAt": null
  }
]
```

**추천 알고리즘:**
- **제목 매칭**: 관심사 키워드가 제목에 포함되면 높은 점수 (3.0점)
- **내용 매칭**: 관심사 키워드가 내용에 포함되면 중간 점수 (1.0점)
- **마감일 가중치**: 마감일이 남아있는 공지사항에 추가 점수 (0.5점)
- **최신성 가중치**: 
  - 7일 이내 게시: +1.0점
  - 30일 이내 게시: +0.5점
- 관심사가 없으면 최근 10개 공지사항 반환

---

## 📊 데이터 모델

### Event 엔티티
```json
{
  "id": "Long (자동 생성)",
  "title": "String (필수, 최대 100자)",
  "description": "String (선택, 최대 500자)",
  "startAt": "LocalDateTime (필수)",
  "endAt": "LocalDateTime (필수)",
  "allDay": "boolean (기본값: false)",
  "location": "String (선택, 최대 255자)",
  "category": "String (선택, 최대 50자)",
  "userId": "String (필수, 최대 50자)"
}
```

### Notice 엔티티
```json
{
  "id": "Long (자동 생성)",
  "sourceId": "Long (필수)",
  "externalId": "String (선택, 최대 100자)",
  "url": "String (필수, 최대 600자, 유니크)",
  "title": "String (필수, 최대 500자)",
  "content": "String (선택, MEDIUMTEXT)",
  "postedAt": "LocalDateTime (선택)",
  "scrapedAt": "LocalDateTime (필수, 자동 설정)",
  "deadlineAt": "LocalDateTime (선택)",
  "hash": "String (선택, 64자)"
}
```

---

## 🧪 테스트 예시

### Event API 테스트
```bash
# 일정 생성
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "title": "테스트 일정",
    "startAt": "2025-08-25T09:00:00",
    "endAt": "2025-08-26T18:00:00",
    "userId": "1"
  }'

# 사용자별 일정 조회
curl "http://localhost:8080/api/events?userId=1"
```

### Notice API 테스트
```bash
# 최근 공지사항 조회
curl http://localhost:8080/api/notices/recent

# 특정 공지사항 조회
curl http://localhost:8080/api/notices/1

# 제목 검색 (URL 인코딩 필요)
curl "http://localhost:8080/api/notices/search?title=%EC%9E%85%ED%95%99"

# 추천 공지사항 조회
curl "http://localhost:8080/api/notices/recommend?interests=수강신청,장학금,취업&limit=5"

# 관심사 없이 추천 조회 (최근 공지사항 반환)
curl "http://localhost:8080/api/notices/recommend"
```

---

## 🚀 서버 실행

```bash
# 애플리케이션 실행
./gradlew bootRun

# 서버 주소
http://localhost:8080
```

---

## 📝 참고사항

- 모든 날짜/시간은 ISO 8601 형식 사용: `YYYY-MM-DDTHH:mm:ss`
- 한글 검색어는 URL 인코딩 필요
- 모든 API는 JSON 형태로 응답
- 서버는 기본적으로 8080 포트에서 실행
- MySQL 데이터베이스 연결 필요 (dev 프로필: localhost:3306/cnu_info)