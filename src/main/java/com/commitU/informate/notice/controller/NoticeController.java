package com.commitU.informate.notice.controller;

import com.commitU.informate.notice.entity.Notice;
import com.commitU.informate.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public List<Notice> getAllNotices() {
        return noticeService.getAllNotices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long id) {
        return noticeService.getNoticeById(id)
                .map(notice -> ResponseEntity.ok(notice))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/source/{sourceId}")
    public List<Notice> getNoticesBySource(@PathVariable Long sourceId) {
        return noticeService.getNoticesBySourceId(sourceId);
    }

    @GetMapping("/search")
    public List<Notice> searchNotices(@RequestParam String title) {
        return noticeService.searchNoticesByTitle(title);
    }

    @GetMapping("/active")
    public List<Notice> getActiveNotices() {
        return noticeService.getActiveNotices();
    }

    @GetMapping("/recent")
    public List<Notice> getRecentNotices() {
        return noticeService.getRecentNotices();
    }

    @GetMapping("/range")
    public List<Notice> getNoticesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return noticeService.getNoticesByDateRange(startDate, endDate);
    }

    @GetMapping("/recommend")
    public List<Notice> getRecommendedNotices(
            @RequestParam(required = false) String interests,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return noticeService.getRecommendedNotices(interests, limit);
    }

    @PostMapping
    public ResponseEntity<Notice> createNotice(@RequestBody Notice notice) {
        Notice createdNotice = noticeService.createNotice(notice);
        return ResponseEntity.ok(createdNotice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long id, @RequestBody Notice notice) {
        return noticeService.updateNotice(id, notice)
                .map(updatedNotice -> ResponseEntity.ok(updatedNotice))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        if (noticeService.deleteNotice(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationError(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("오류: " + e.getMessage());
    }
}