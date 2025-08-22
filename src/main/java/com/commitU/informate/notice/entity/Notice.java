package com.commitU.informate.notice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id", nullable = false)
    private Long sourceId;

    @Column(name = "external_id", length = 100)
    private String externalId;

    @Column(name = "url", nullable = false, length = 600, unique = true)
    private String url;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "posted_at")
    private LocalDateTime postedAt;

    @Column(name = "scraped_at", nullable = false)
    private LocalDateTime scrapedAt;

    @Column(name = "deadline_at")
    private LocalDateTime deadlineAt;

    @Column(name = "hash", length = 64)
    private String hash;

    public Notice() {}

    public Notice(Long sourceId, String url, String title) {
        this.sourceId = sourceId;
        this.url = url;
        this.title = title;
        this.scrapedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }

    public LocalDateTime getScrapedAt() {
        return scrapedAt;
    }

    public void setScrapedAt(LocalDateTime scrapedAt) {
        this.scrapedAt = scrapedAt;
    }

    public LocalDateTime getDeadlineAt() {
        return deadlineAt;
    }

    public void setDeadlineAt(LocalDateTime deadlineAt) {
        this.deadlineAt = deadlineAt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", postedAt=" + postedAt +
                ", deadlineAt=" + deadlineAt +
                '}';
    }
}