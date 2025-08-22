# API 명세서

## 📅 Calendar API (Event)

### Base URL: `/api/events`

#### 1. 일정 생성
```http
POST /api/events
Content-Type: application/json

{
  "userId": 1,
  "noticeId": 100,
  "title": "테스트 일정",
  "date": "2025-08-25"
}
```

**Request Parameters:**
- `userId` (필수): 사용자 ID
- `noticeId` (선택): 연결할 공지사항 ID (없으면 일반 일정)
- `title` (필수): 일정 제목
- `date` (필수): 일정 날짜

**Response:**
```json
{
  "id": 13,
  "title": "테스트 일정",
  "date": "2025-08-25",
  "user": {
    "id": 1
  },
  "notice": {
    "id": 100,
    "title": "연결된 공지사항 제목"
  }
}
```

#### 2. 일정 단건 조회
```http
GET /api/events/{id}
```

#### 3. 사용자별 일정 조회
```http
GET /api/events?userId=1
```

#### 4. 기간별 일정 조회 (달력 뷰용)
```http
GET /api/events/range?userId=1&start=2025-08-01&end=2025-08-31
```

#### 5. 공지사항으로부터 일정 생성
```http
POST /api/events/from-notice?userId=1&noticeId=1&date=2025-08-25
```

**Parameters:**
- `userId` (필수): 사용자 ID
- `noticeId` (필수): 공지사항 ID
- `date` (필수): 일정 날짜

**Response:**
```json
{
  "id": 14,
  "title": "2025학년도 제2학기 예비수강신청 계획 안내",
  "date": "2025-08-25",
  "user": {
    "id": 1
  },
  "notice": {
    "id": 1,
    "title": "2025학년도 제2학기 예비수강신청 계획 안내",
    "url": "https://plus.cnu.ac.kr/..."
  }
}
```

#### 6. Notice 연결된 일정 조회
```http
GET /api/events/with-notice?userId=1
```

#### 7. 일정 수정
```http
PUT /api/events/{id}
Content-Type: application/json

{
  "title": "수정된 일정",
  "date": "2025-08-25"
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

#### 12. 📂 공지사항 카테고리 조회
```http
GET /api/notices/categories
```

**Response:**
```json
{
  "1": {
    "sourceId": 1,
    "name": "특강",
    "count": 15
  },
  "2": {
    "sourceId": 2,
    "name": "기획/마케팅", 
    "count": 8
  },
  "3": {
    "sourceId": 3,
    "name": "취업/인턴십",
    "count": 25
  },
  "4": {
    "sourceId": 4,
    "name": "봉사 활동",
    "count": 12
  },
  "5": {
    "sourceId": 5,
    "name": "IT/SW",
    "count": 18
  },
  "6": {
    "sourceId": 6,
    "name": "스터디",
    "count": 9
  },
  "7": {
    "sourceId": 7,
    "name": "디자인",
    "count": 6
  },
  "8": {
    "sourceId": 8,
    "name": "창업",
    "count": 11
  },
  "9": {
    "sourceId": 9,
    "name": "영상/콘텐츠",
    "count": 14
  },
  "10": {
    "sourceId": 10,
    "name": "서포터즈/기자단",
    "count": 7
  },
  "11": {
    "sourceId": 11,
    "name": "학사 안내",
    "count": 22
  },
  "12": {
    "sourceId": 12,
    "name": "기타",
    "count": 13
  }
}
```

#### 13. 📂 카테고리별 공지사항 조회
```http
GET /api/notices/category/{categoryId}
```

**파라미터:**
- `categoryId`: 카테고리 ID
  - 1: 특강
  - 2: 기획/마케팅  
  - 3: 취업/인턴십
  - 4: 봉사 활동
  - 5: IT/SW
  - 6: 스터디
  - 7: 디자인
  - 8: 창업
  - 9: 영상/콘텐츠
  - 10: 서포터즈/기자단
  - 11: 학사 안내
  - 12: 기타
  - 

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

---

## 📊 데이터 모델

### Event 엔티티
```json
{
  "id": "Long (자동 생성)",
  "title": "String (필수, 최대 100자)",
  "date": "LocalDate (필수)",
  "user": "User 엔티티 (필수)",
  "notice": "Notice 엔티티 (선택, 공지사항과 연결된 일정인 경우)"
}
```

### User 엔티티
```json
{
  "id": "Long (자동 생성)",
  "email": "String (필수, 최대 200자, 유니크)",
  "passwordHash": "String (필수, 최대 255자)",
  "name": "String (선택, 최대 100자)",
  "createdAt": "LocalDateTime (자동 설정)"
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
# 일정 생성 (일반 일정)
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "title": "테스트 일정",
    "date": "2025-08-25"
  }'

# 일정 생성 (공지사항 연결)
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "noticeId": 100,
    "title": "테스트 일정",
    "date": "2025-08-25"
  }'

# 공지사항으로부터 일정 생성
curl -X POST "http://localhost:8080/api/events/from-notice?userId=1&noticeId=1&date=2025-08-25"

# 사용자별 일정 조회
curl "http://localhost:8080/api/events?userId=1"

# 특정 일정 조회
curl "http://localhost:8080/api/events/1"

# 일정 삭제
curl -X DELETE "http://localhost:8080/api/events/1"

# Notice 연결된 일정만 조회
curl "http://localhost:8080/api/events/with-notice?userId=1"
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

# 카테고리 목록 조회
curl http://localhost:8080/api/notices/categories

# 특강 카테고리 조회
curl http://localhost:8080/api/notices/category/1

# 취업/인턴십 카테고리 조회  
curl http://localhost:8080/api/notices/category/3

# IT/SW 카테고리 조회
curl http://localhost:8080/api/notices/category/5
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

- 모든 날짜는 ISO 8601 형식 사용: `YYYY-MM-DD`
- 한글 검색어는 URL 인코딩 필요
- 모든 API는 JSON 형태로 응답
- 서버는 기본적으로 8080 포트에서 실행
- MySQL 데이터베이스 연결 필요 (dev 프로필: localhost:3306/cnu_info)