package com.deepread.service;

import com.deepread.entity.Content;
import com.deepread.entity.User;
import com.deepread.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    // 사용자 수준에 따른 뉴스/문서 추천
    public List<Content> getRecommendedContents(User.Level level) {
        return contentRepository.findByLevel(level);
    }

    // ID로 문서 상세 조회
    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }
}
