package com.commitU.informate.calendar.repository;

import com.commitU.informate.calendar.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // 특정 사용자의 모든 일정 조회(시간순 정렬)
    List<Event> findByUserIdOrderByStartAtAsc(String userId);

    // 특정 사용자의 기간별 일정 조회
    @Query("""
  SELECT e FROM Event e
  WHERE e.userId = :userId
    AND e.startAt <= :end
    AND e.endAt   >= :start
  ORDER BY e.startAt ASC
""")
    List<Event> findOverlapping(
            @Param("userId") String userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // 제목으로 검색(대소문자 구분 X)
    List<Event> findByUserIdAndTitleContainingIgnoreCaseOrderByStartAtAsc(
            String userId, String title
    );

    // 카테고리로 필터링
    List<Event> findByUserIdAndCategoryOrderByStartAtAsc(String userId, String category);
}
