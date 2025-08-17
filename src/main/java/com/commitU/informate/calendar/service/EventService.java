package com.commitU.informate.calendar.service;

import com.commitU.informate.calendar.entity.CalendarEvent;
import com.commitU.informate.calendar.repository.CalendarEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CalendarEventService {

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    // 새 일정 생성
    public CalendarEvent createEvent(CalendarEvent calendarEvent) {
        // 비즈니스 검증
        validateEvent(calendarEvent);
        ret
    }

    /**
     * 일정 단건 조회
     */
    public Optional<Event> getEvent(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * 특정 사용자의 모든 일정 조회
     */
    public List<Event> getUserEvents(String userId) {
        return eventRepository.findByUserIdOrderByStartAtAsc(userId);
    }

    /**
     * 기간별 일정 조회
     */
    public List<Event> getEventsByDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        return eventRepository.findByUserIdAndDateRange(userId, start, end);
    }

    /**
     * 제목으로 검색
     */
    public List<Event> searchEventsByTitle(String userId, String title) {
        return eventRepository.findByUserIdAndTitleContainingIgnoreCaseOrderByStartAtAsc(userId, title);
    }

    /**
     * 카테고리별 조회
     */
    public List<Event> getEventsByCategory(String userId, String category) {
        return eventRepository.findByUserIdAndCategoryOrderByStartAtAsc(userId, category);
    }

    /**
     * 일정 수정
     */
    public Optional<CalendarEvent> updateEvent(Long id, CalendarEvent updatedEvent) {
        return CalendarEventRepository.findById(id)
                .map(existingEvent -> {
                    // 기존 일정의 정보를 새 정보로 업데이트
                    existingEvent.setTitle(updatedEvent.getTitle());
                    existingEvent.setDescription(updatedEvent.getDescription());
                    existingEvent.setStartAt(updatedEvent.getStartAt());
                    existingEvent.setEndAt(updatedEvent.getEndAt());
                    existingEvent.setAllDay(updatedEvent.isAllDay());
                    existingEvent.setLocation(updatedEvent.getLocation());
                    existingEvent.setCategory(updatedEvent.getCategory());

                    validateEvent(existingEvent);
                    return CalendarEventRepository.save(existingEvent);
                });
    }

    /**
     * 일정 삭제
     */
    public boolean deleteEvent(Long id) {
        if (calendarEventRepository.existsById(id)) {
            calendarEventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 일정 유효성 검증
     */
    private void validateEvent(CalendarEvent calendarEvent) {
        if (event.getStartAt().isAfter(event.getEndAt())) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다");
        }

        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
    }
}
