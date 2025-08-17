package com.commitU.informate.calendar.service;

import com.commitU.informate.calendar.entity.Event;
import com.commitU.informate.calendar.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // 새 일정 생성
    public Event createEvent(Event event) {
        // 비즈니스 검증
        validateEvent(event);
        return eventRepository.save(event);
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

    /** 컨트롤러 호환용 래퍼 (최소 변경) */
    @Transactional(readOnly = true)
    public List<Event> getEventsByDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        return getEventsOverlapping(userId, start, end);
    }

    /** 실제 겹침 조회 로직 */
    @Transactional(readOnly = true)
    public List<Event> getEventsOverlapping(String userId, LocalDateTime start, LocalDateTime end) {
        if (userId == null || userId.isBlank()) throw new IllegalArgumentException("userId는 필수입니다.");
        if (start == null || end == null) throw new IllegalArgumentException("start/end 는 필수입니다.");
        if (end.isBefore(start)) throw new IllegalArgumentException("end는 start 이후여야 합니다.");
        return eventRepository.findOverlapping(userId, start, end);
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
    public Optional<Event> updateEvent(Long id, Event updatedEvent) {
        return eventRepository.findById(id)
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
                    return eventRepository.save(existingEvent);
                });
    }

    /**
     * 일정 삭제
     */
    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 일정 유효성 검증
     */
    private void validateEvent(Event event) {
        if (event.getStartAt() == null || event.getEndAt() == null)
            throw new IllegalArgumentException("startAt/endAt은 필수입니다");
        if (event.getStartAt().isAfter(event.getEndAt()))
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다");
        if (event.getTitle() == null || event.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("제목은 필수입니다");
        if (event.getUserId() == null || event.getUserId().isBlank())
            throw new IllegalArgumentException("userId는 필수입니다");
    }
}
