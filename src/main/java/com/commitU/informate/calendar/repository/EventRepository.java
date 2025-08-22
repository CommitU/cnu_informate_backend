package com.commitU.informate.calendar.repository;

import com.commitU.informate.calendar.entity.Event;
import com.commitU.informate.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // 특정 사용자의 모든 일정 조회(날짜순 정렬)
    List<Event> findByUserOrderByDateAsc(User user);
    
    // userId로 조회 (호환성)
    List<Event> findByUser_IdOrderByDateAsc(Long userId);

    // 특정 사용자의 기간별 일정 조회
    @Query("""
  SELECT e FROM Event e
  WHERE e.user.id = :userId
    AND e.date >= :start
    AND e.date <= :end
  ORDER BY e.date ASC
""")
    List<Event> findByDateRange(
            @Param("userId") Long userId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    
    
    // 사용자별 Notice 연결된 일정 조회
    List<Event> findByUser_IdAndNoticeIsNotNullOrderByDateAsc(Long userId);
}
