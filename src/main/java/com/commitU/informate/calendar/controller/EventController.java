package com.commitU.informate.calendar.controller;

import com.commitU.informate.calendar.entity.Event;
import com.commitU.informate.calendar.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // 새 일정 생성(Post /api/events)
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

    // Notice로부터 일정 생성 (POST /api/events/from-notice)
    @PostMapping("/from-notice")
    public ResponseEntity<Event> createEventFromNotice(
            @RequestParam Long userId,
            @RequestParam Long noticeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startAt,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endAt) {
        Event createdEvent = eventService.createEventFromNotice(userId, noticeId, startAt, endAt);
        return ResponseEntity.ok(createdEvent);
    }

    // 일정 단건 조회(Get /api/events/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return eventService.getEvent(id)
                .map(event -> ResponseEntity.ok(event))
                .orElse(ResponseEntity.notFound().build());
    }

    // 사용자의 모든 일정 조회(Get /api/events?userId=1)
    @GetMapping
    public List<Event> getUserEvents(@RequestParam Long userId) {
        return eventService.getUserEvents(userId);
    }

    /**
     * 기간별 일정 조회 (달력 뷰용)
     * GET /api/events/range?userId=1&start=2025-08-01T00:00:00&end=2025-08-31T23:59:59
     */
    @GetMapping("/range")
    public List<Event> getEventsByRange(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return eventService.getEventsByDateRange(userId, start, end);
    }

    /**
     * 제목으로 일정 검색
     * GET /api/events/search?userId=1&title=회의
     */
    @GetMapping("/search")
    public List<Event> searchEvents(
            @RequestParam Long userId,
            @RequestParam String title) {
        return eventService.searchEventsByTitle(userId, title);
    }

    /**
     * 카테고리별 일정 조회
     * GET /api/events/category?userId=1&category=업무
     */
    @GetMapping("/category")
    public List<Event> getEventsByCategory(
            @RequestParam Long userId,
            @RequestParam String category) {
        return eventService.getEventsByCategory(userId, category);
    }

    /**
     * Notice와 연결된 일정 조회
     * GET /api/events/with-notice?userId=1
     */
    @GetMapping("/with-notice")
    public List<Event> getEventsWithNotice(@RequestParam Long userId) {
        return eventService.getEventsWithNotice(userId);
    }

    /**
     * 일정 수정
     * PUT /api/events/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody Event event) {
        return eventService.updateEvent(id, event)
                .map(updatedEvent -> ResponseEntity.ok(updatedEvent))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 일정 삭제
     * DELETE /api/events/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventService.deleteEvent(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 간단한 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationError(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("오류: " + e.getMessage());
    }
}