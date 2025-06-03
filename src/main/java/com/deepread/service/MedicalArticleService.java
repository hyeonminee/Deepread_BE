package com.deepread.service;

import com.deepread.client.AiSummaryClient;
import com.deepread.dto.response.MedicalArticleResponseDto;
import com.deepread.entity.MedicalArticle;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.MedicalArticleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalArticleService {

    private final MedicalArticleRepository medicalArticleRepository;
    private final ModelMapper modelMapper;
    private final AiSummaryClient aiSummaryClient;

    public List<MedicalArticleResponseDto> getAllArticles() {
        return medicalArticleRepository.findAll().stream()
                .map(article -> modelMapper.map(article, MedicalArticleResponseDto.class))
                .collect(Collectors.toList());
    }

    public Optional<MedicalArticleResponseDto> getArticleById(Long id) {
        return medicalArticleRepository.findById(id)
                .map(article -> modelMapper.map(article, MedicalArticleResponseDto.class));
    }

    /**
     * AI 서버로 요약 요청 후 DB에 aiSummary 저장
     */
    @Transactional
    public MedicalArticleResponseDto summarizeAndUpdate(Long id) {
        MedicalArticle article = medicalArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalArticle not found"));

        try {
            String content = article.getContent();
            String aiSummary = aiSummaryClient.requestSummary(content);
            article.setAiSummary(aiSummary);
            medicalArticleRepository.save(article);
        } catch (Exception e) {
            throw new RuntimeException("AI 요약 요청 실패: " + e.getMessage(), e);
        }

        return modelMapper.map(article, MedicalArticleResponseDto.class);
    }
}
