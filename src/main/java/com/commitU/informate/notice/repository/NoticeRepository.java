package com.commitU.informate.notice.repository;

import com.commitU.informate.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findBySourceIdOrderByPostedAtDesc(Long sourceId);

    List<Notice> findByTitleContainingIgnoreCaseOrderByPostedAtDesc(String title);

    @Query("SELECT n FROM Notice n WHERE n.deadlineAt >= :now ORDER BY n.deadlineAt ASC")
    List<Notice> findActiveNotices(@Param("now") LocalDateTime now);

    @Query("SELECT n FROM Notice n WHERE n.postedAt BETWEEN :startDate AND :endDate ORDER BY n.postedAt DESC")
    List<Notice> findByPostedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);

    List<Notice> findTop10ByOrderByPostedAtDesc();
}