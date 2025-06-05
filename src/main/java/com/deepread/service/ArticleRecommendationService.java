package com.deepread.service;

import com.deepread.dto.response.*;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleRecommendationService {

    private final UserRepository userRepository;
    private final LawArticleRepository lawArticleRepository;
    private final MedicalArticleRepository medicalArticleRepository;
    private final NewsArticleRepository newsArticleRepository;
    private final ModelMapper modelMapper;

    public RecommendedArticlesDto getRecommendedArticles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));
        User.Level level = user.getLevel();

        List<LawArticleResponseDto> lawList = lawArticleRepository.findByLevel(level).stream()
                .map(article -> modelMapper.map(article, LawArticleResponseDto.class))
                .collect(Collectors.toList());

        List<MedicalArticleResponseDto> medList = medicalArticleRepository.findByLevel(level).stream()
                .map(article -> modelMapper.map(article, MedicalArticleResponseDto.class))
                .collect(Collectors.toList());

        List<NewsArticleResponseDto> newsList = newsArticleRepository.findAll().stream()
                .map(article -> modelMapper.map(article, NewsArticleResponseDto.class))
                .collect(Collectors.toList());

        return RecommendedArticlesDto.builder()
                .lawArticles(lawList)
                .medicalArticles(medList)
                .newsArticles(newsList)
                .build();
    }
}
