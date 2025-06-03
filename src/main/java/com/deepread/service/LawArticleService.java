package com.deepread.service;

import com.deepread.client.AiSummaryClient;
import com.deepread.dto.response.LawArticleResponseDto;
import com.deepread.dto.response.LawArticleUploadResponseDto;
import com.deepread.entity.LawArticle;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.LawArticleRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LawArticleService {

    private final LawArticleRepository lawArticleRepository;
    private final ModelMapper modelMapper;
    private final AiSummaryClient aiSummaryClient;

    public List<LawArticleResponseDto> getAllArticles() {
        return lawArticleRepository.findAll().stream()
                .map(article -> modelMapper.map(article, LawArticleResponseDto.class))
                .collect(Collectors.toList());
    }

    public Optional<LawArticleResponseDto> getArticleById(Long id) {
        return lawArticleRepository.findById(id)
                .map(article -> modelMapper.map(article, LawArticleResponseDto.class));
    }

    /**
     * CSV 파일 업로드 처리 (aiSummary는 null로 저장됨)
     */
    public LawArticleUploadResponseDto uploadCsv(MultipartFile file) throws Exception {
        int success = 0;
        int failure = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            List<LawArticle> articles = new ArrayList<>();

            for (int i = 1; i < rows.size(); i++) {
                try {
                    String[] row = rows.get(i);
                    String theme = row[1].trim();
                    String question = row[2].trim();
                    String answer = row[3].trim();
                    String content = question + "\n\n" + answer;

                    LawArticle article = LawArticle.builder()
                            .theme(theme)
                            .content(content)
                            .aiSummary(null)
                            .build();

                    articles.add(article);
                    success++;
                } catch (Exception e) {
                    failure++;
                }
            }

            lawArticleRepository.saveAll(articles);
        }

        return LawArticleUploadResponseDto.builder()
                .successCount(success)
                .failureCount(failure)
                .build();
    }

    /**
     * AI 서버로 요약 요청 후 DB에 aiSummary 저장
     */
    @Transactional
    public LawArticleResponseDto summarizeAndUpdate(Long id) {
        LawArticle article = lawArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LawArticle not found"));

        try {
            String content = article.getContent();
            String aiSummary = aiSummaryClient.requestSummary(content);
            article.setAiSummary(aiSummary);
            lawArticleRepository.save(article);
        } catch (Exception e) {
            throw new RuntimeException("AI 요약 요청 실패: " + e.getMessage(), e);
        }

        return modelMapper.map(article, LawArticleResponseDto.class);
    }

}
