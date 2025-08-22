package com.commitU.informate.calendar.service;

import com.commitU.informate.calendar.entity.Event;
import com.commitU.informate.calendar.repository.EventRepository;
import com.commitU.informate.notice.entity.Notice;
import com.commitU.informate.notice.repository.NoticeRepository;
import com.commitU.informate.user.entity.User;
import com.commitU.informate.calendar.dto.EventCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private NoticeRepository noticeRepository;
    

    // 새 일정 생성
    public Event createEvent(Event event) {
        // 비즈니스 검증
        validateEvent(event);
        return eventRepository.save(event);
    }
    
    // 요청 DTO를 통한 일정 생성
    public Event createEvent(EventCreateRequest request) {
        User user = new User();
        user.setId(request.getUserId());
        
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDate(request.getDate());
        event.setUser(user);
        
        // noticeId가 있으면 Notice 연결
        if (request.getNoticeId() != null) {
            Notice notice = noticeRepository.findById(request.getNoticeId())
                    .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다: " + request.getNoticeId()));
            event.setNotice(notice);
        }
        
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
    public List<Event> getUserEvents(Long userId) {
        return eventRepository.findByUser_IdOrderByDateAsc(userId);
    }
    
    /**
     * Notice로부터 일정 생성
     */
    public Event createEventFromNotice(Long userId, Long noticeId, LocalDate date) {
        User user = new User();
        user.setId(userId);
        
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다: " + noticeId));
        
        Event event = new Event();
        event.setTitle(notice.getTitle());
        event.setDate(date);
        event.setUser(user);
        event.setNotice(notice);
        
        return eventRepository.save(event);
    }

    /** 컨트롤러 호환용 래퍼 (최소 변경) */
    @Transactional(readOnly = true)
    public List<Event> getEventsByDateRange(Long userId, LocalDate start, LocalDate end) {
        if (userId == null) throw new IllegalArgumentException("userId는 필수입니다.");
        if (start == null || end == null) throw new IllegalArgumentException("start/end 는 필수입니다.");
        if (end.isBefore(start)) throw new IllegalArgumentException("end는 start 이후여야 합니다.");
        return eventRepository.findByDateRange(userId, start, end);
    }

    
    /**
     * Notice와 연결된 일정만 조회
     */
    public List<Event> getEventsWithNotice(Long userId) {
        return eventRepository.findByUser_IdAndNoticeIsNotNullOrderByDateAsc(userId);
    }

    /**
     * 일정 수정
     */
    public Optional<Event> updateEvent(Long id, Event updatedEvent) {
        return eventRepository.findById(id)
                .map(existingEvent -> {
                    // 기본 정보만 업데이트
                    existingEvent.setTitle(updatedEvent.getTitle());
                    existingEvent.setDate(updatedEvent.getDate());
                    if (updatedEvent.getNotice() != null) {
                        existingEvent.setNotice(updatedEvent.getNotice());
                    }

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
        if (event.getDate() == null)
            throw new IllegalArgumentException("date는 필수입니다");
        if (event.getTitle() == null || event.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("제목은 필수입니다");
        if (event.getUser() == null)
            throw new IllegalArgumentException("user는 필수입니다");
    }
}