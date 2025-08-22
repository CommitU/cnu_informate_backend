package com.commitU.informate.calendar.repository;

import com.commitU.informate.calendar.entity.Event;
import com.commitU.informate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // 특정 사용자의 모든 일정 조회(시간순 정렬)
    List<Event> findByUserOrderByStartAtAsc(User user);
    
    // userId로 조회 (호환성)
    List<Event> findByUser_IdOrderByStartAtAsc(Long userId);

    // 특정 사용자의 기간별 일정 조회
    @Query("""
  SELECT e FROM Event e
  WHERE e.user.id = :userId
    AND e.startAt <= :end
    AND e.endAt   >= :start
  ORDER BY e.startAt ASC
""")
    List<Event> findOverlapping(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // 제목으로 검색(대소문자 구분 X)
    List<Event> findByUser_IdAndTitleContainingIgnoreCaseOrderByStartAtAsc(
            Long userId, String title
    );

    // 카테고리로 필터링
    List<Event> findByUser_IdAndCategoryOrderByStartAtAsc(Long userId, String category);
    
    // Notice와 연결된 일정 조회
    List<Event> findByNotice_IdOrderByStartAtAsc(Long noticeId);
    
    // 사용자별 Notice 연결된 일정 조회
    List<Event> findByUser_IdAndNoticeIsNotNullOrderByStartAtAsc(Long userId);
}
