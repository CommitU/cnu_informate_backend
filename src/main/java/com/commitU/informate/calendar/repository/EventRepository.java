package com.commitU.informate.calendar.repository;

import com.commitU.informate.calendar.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    // 특정 사용자의 모든 일정 조회(시간순 정렬)
    List<CalendarEvent> findByUerIdOrderByStartAt(String userId);

    // 특정 사용자의 기간별 일정 조회
    List<CalendarEvent> findByUserIdDateRange(
            @Param("userId") String userId,
            @Param("start") LocalDateTime start,
            @Param("add") LocalDateTime end
            );

    // 제목으로 검색(대소문자 구분 X)
    List<CalendarEvent> findByUserIdAndTitleContainingIgnoreCaseOrderByStartAtAsc(
            String userId, String title
    );

    // 카테고리로 필터링
    List<CalendarEvent> findByUserIdAndCategoryOrderByStartAtAsc(String userId, String category);
}
