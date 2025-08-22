package com.commitU.informate.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EventCreateRequest {
    
    @NotNull(message = "userId는 필수입니다")
    private Long userId;
    
    private Long noticeId;
    
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    @NotNull(message = "날짜는 필수입니다")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    public EventCreateRequest() {}
    
    public EventCreateRequest(Long userId, Long noticeId, String title, LocalDate date) {
        this.userId = userId;
        this.noticeId = noticeId;
        this.title = title;
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}