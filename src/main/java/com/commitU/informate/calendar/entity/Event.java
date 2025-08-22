package com.commitU.informate.calendar.entity;

import com.commitU.informate.notice.entity.Notice;
import com.commitU.informate.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수입니다.")
    @Column(nullable = false, length = 100)
    private String title;

    @NotNull(message = "일정 날짜는 필수입니다")
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    // 기본 생성자
    public Event() {}

    // 편의 생성자
    public Event(String title, LocalDate date, User user) {
        this.title = title;
        this.date = date;
        this.user = user;
    }

    // Notice와 함께 생성하는 편의 생성자
    public Event(String title, LocalDate date, User user, Notice notice) {
        this.title = title;
        this.date = date;
        this.user = user;
        this.notice = notice;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", user=" + (user != null ? user.getId() : null) +
                ", notice=" + (notice != null ? notice.getId() : null) +
                '}';
    }
}
