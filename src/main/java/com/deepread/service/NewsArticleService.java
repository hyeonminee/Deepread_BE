package com.deepread.service;

import com.deepread.client.AiSummaryClient;
import com.deepread.dto.response.NewsArticleResponseDto;
import com.deepread.entity.Content;
import com.deepread.entity.NewsArticle;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.ContentRepository;
import com.deepread.repository.NewsArticleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsArticleService {

    private final NewsArticleRepository newsArticleRepository;
    private final ContentRepository contentRepository;
    private final ModelMapper modelMapper;
    private final AiSummaryClient aiSummaryClient;

    private NewsArticleResponseDto convertToDto(NewsArticle article) {
        return modelMapper.map(article, NewsArticleResponseDto.class);
    }

    public List<NewsArticleResponseDto> getAllArticles() {
        return newsArticleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<NewsArticleResponseDto> getArticleById(Long id) {
        return newsArticleRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<NewsArticleResponseDto> getArticlesByCategory(String category) {
        return newsArticleRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NewsArticleResponseDto> searchArticlesByTitle(String keyword) {
        return newsArticleRepository.findByTitleContaining(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NewsArticleResponseDto> getTodayArticles() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        return newsArticleRepository.findByCreatedAtBetween(start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<NewsArticleResponseDto> getSingleTodayArticleByCategory(String category) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 23:59:59.999999999

        return newsArticleRepository.findByCategoryAndCreatedAtBetween(category, startOfDay, endOfDay).stream()
                .findFirst()
                .map(article -> {
                    NewsArticleResponseDto dto = new NewsArticleResponseDto();
                    dto.setId(article.getId());
                    dto.setCategory(article.getCategory());
                    dto.setTitle(article.getTitle());
                    dto.setContent(article.getContent());
                    dto.setAiSummary(article.getAiSummary());
                    dto.setOriginalUrl(article.getOriginalUrl());
                    dto.setCreatedAt(article.getCreatedAt());

                    dto.setContentId(
                            contentRepository.findByExternalIdAndCategory(article.getId(), "NEWS")
                                    .map(Content::getId)
                                    .orElse(null)
                    );

                    return dto;
                });
    }

    @Transactional
    public NewsArticleResponseDto summarizeAndUpdate(Long id) throws Exception {
        NewsArticle article = newsArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("뉴스 기사를 찾을 수 없습니다."));

        String content = article.getContent();
        String aiSummary = aiSummaryClient.requestSummary(content);
        article.setAiSummary(aiSummary);
        newsArticleRepository.save(article);

        return convertToDto(article);
    }
}
