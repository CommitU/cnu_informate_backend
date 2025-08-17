package com.commitU.informate.calendar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수입니다.")
    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @NotNull(message = "시작 시간은 필수입니다")
    @Column(nullable = false)
    private LocalDateTime startAt;

    @NotNull(message = "종료 시간은 필수입니다")
    @Column(nullable = false)
    private LocalDateTime endAt;

    // allDay: 종일 여부
    @Column(nullable = false)
    private boolean allDay = false;

    @Column(length = 255)
    private String location;

    @Column(length = 50)
    private String category;

    @Column(nullable = false, length = 50)
    private String userId;

    // 기본 생성자
    public Event() {}

    // 편의 생성자
    public Event(String title, LocalDateTime startAt, LocalDateTime endAt, String userId) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.userId = userId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", allDay=" + allDay +
                ", userId='" + userId + '\'' +
                '}';
    }
}
