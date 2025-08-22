package com.commitU.informate.notice.service;

import com.commitU.informate.notice.entity.Notice;
import com.commitU.informate.notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    public List<Notice> getNoticesBySourceId(Long sourceId) {
        return noticeRepository.findBySourceIdOrderByPostedAtDesc(sourceId);
    }

    public List<Notice> searchNoticesByTitle(String title) {
        return noticeRepository.findByTitleContainingIgnoreCaseOrderByPostedAtDesc(title);
    }

    public List<Notice> getActiveNotices() {
        return noticeRepository.findActiveNotices(LocalDateTime.now());
    }

    public List<Notice> getNoticesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return noticeRepository.findByPostedAtBetween(startDate, endDate);
    }

    public List<Notice> getRecentNotices() {
        return noticeRepository.findTop10ByOrderByPostedAtDesc();
    }

    public Notice createNotice(Notice notice) {
        if (notice.getScrapedAt() == null) {
            notice.setScrapedAt(LocalDateTime.now());
        }
        return noticeRepository.save(notice);
    }

    public Optional<Notice> updateNotice(Long id, Notice notice) {
        return noticeRepository.findById(id)
                .map(existingNotice -> {
                    existingNotice.setTitle(notice.getTitle());
                    existingNotice.setContent(notice.getContent());
                    existingNotice.setUrl(notice.getUrl());
                    existingNotice.setPostedAt(notice.getPostedAt());
                    existingNotice.setDeadlineAt(notice.getDeadlineAt());
                    existingNotice.setExternalId(notice.getExternalId());
                    existingNotice.setHash(notice.getHash());
                    return noticeRepository.save(existingNotice);
                });
    }

    public boolean deleteNotice(Long id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Notice> getRecommendedNotices(String userInterests, Integer limit) {
        List<Notice> allNotices = noticeRepository.findAll();
        
        if (userInterests == null || userInterests.trim().isEmpty()) {
            return getRecentNotices();
        }

        Set<String> interestKeywords = parseInterests(userInterests);
        
        List<Notice> recommendedNotices = allNotices.stream()
                .filter(notice -> calculateRelevanceScore(notice, interestKeywords) > 0)
                .sorted((n1, n2) -> {
                    double score1 = calculateRelevanceScore(n1, interestKeywords);
                    double score2 = calculateRelevanceScore(n2, interestKeywords);
                    
                    if (score1 != score2) {
                        return Double.compare(score2, score1);
                    }
                    
                    LocalDateTime date1 = n1.getPostedAt() != null ? n1.getPostedAt() : n1.getScrapedAt();
                    LocalDateTime date2 = n2.getPostedAt() != null ? n2.getPostedAt() : n2.getScrapedAt();
                    
                    if (date1 != null && date2 != null) {
                        return date2.compareTo(date1);
                    }
                    
                    return 0;
                })
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());

        if (recommendedNotices.isEmpty()) {
            return getRecentNotices();
        }

        return recommendedNotices;
    }

    private Set<String> parseInterests(String interests) {
        Set<String> keywords = new HashSet<>();
        
        String[] interestArray = interests.split("[,;\\s]+");
        for (String interest : interestArray) {
            String cleaned = interest.trim().toLowerCase();
            if (!cleaned.isEmpty()) {
                keywords.add(cleaned);
            }
        }
        
        return keywords;
    }

    private double calculateRelevanceScore(Notice notice, Set<String> interestKeywords) {
        double score = 0.0;
        String title = notice.getTitle() != null ? notice.getTitle().toLowerCase() : "";
        String content = notice.getContent() != null ? notice.getContent().toLowerCase() : "";
        
        for (String keyword : interestKeywords) {
            if (title.contains(keyword)) {
                score += 3.0;
            }
            if (content.contains(keyword)) {
                score += 1.0;
            }
        }

        if (notice.getDeadlineAt() != null && notice.getDeadlineAt().isAfter(LocalDateTime.now())) {
            score += 0.5;
        }

        LocalDateTime postedDate = notice.getPostedAt() != null ? notice.getPostedAt() : notice.getScrapedAt();
        if (postedDate != null) {
            long daysOld = java.time.Duration.between(postedDate, LocalDateTime.now()).toDays();
            if (daysOld <= 7) {
                score += 1.0;
            } else if (daysOld <= 30) {
                score += 0.5;
            }
        }

        return score;
    }

    public Map<String, Object> getNoticeCategories() {
        List<Long> sourceIds = noticeRepository.findDistinctSourceIds();
        Map<String, Object> categories = new LinkedHashMap<>();
        
        for (Long sourceId : sourceIds) {
            Map<String, Object> categoryInfo = new LinkedHashMap<>();
            categoryInfo.put("sourceId", sourceId);
            categoryInfo.put("name", getCategoryName(sourceId));
            categoryInfo.put("count", noticeRepository.findBySourceIdOrderByPostedAtDesc(sourceId).size());
            categories.put(sourceId.toString(), categoryInfo);
        }
        
        return categories;
    }

    public List<Notice> getNoticesByCategory(Long sourceId) {
        return noticeRepository.findBySourceIdOrderByPostedAtDesc(sourceId);
    }

    private String getCategoryName(Long sourceId) {
        switch (sourceId.intValue()) {
            case 1: return "특강";
            case 2: return "기획/마케팅";
            case 3: return "취업/인턴십";
            case 4: return "봉사 활동";
            case 5: return "IT/SW";
            case 6: return "스터디";
            case 7: return "디자인";
            case 8: return "창업";
            case 9: return "영상/콘텐츠";
            case 10: return "서포터즈/기자단";
            case 11: return "학사 안내";
            case 12: return "기타";
            default: return "기타 (ID: " + sourceId + ")";
        }
    }
}