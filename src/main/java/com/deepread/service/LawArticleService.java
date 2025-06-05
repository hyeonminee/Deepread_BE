package com.deepread.service;

import com.deepread.client.AiSummaryClient;
import com.deepread.dto.response.LawArticleResponseDto;
import com.deepread.dto.response.LawArticleUploadResponseDto;
import com.deepread.entity.LawArticle;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.LawArticleRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class LawArticleService {

    private final LawArticleRepository lawArticleRepository;
    private final ModelMapper modelMapper;
    private final AiSummaryClient aiSummaryClient;

     // 전체 법률 콘텐츠 조회
    public List<LawArticleResponseDto> getAllArticles() {
        return lawArticleRepository.findAll().stream()
                .map(article -> modelMapper.map(article, LawArticleResponseDto.class))
                .collect(Collectors.toList());
    }

    // ID 기준 단일 콘텐츠 조회
    public Optional<LawArticleResponseDto> getArticleById(Long id) {
        return lawArticleRepository.findById(id)
                .map(article -> modelMapper.map(article, LawArticleResponseDto.class));
    }

    // CSV 파일 업로드 처리 (aiSummary는 null로 저장됨)
    public LawArticleUploadResponseDto uploadCsv(MultipartFile file) throws Exception {
        int success = 0;
        int failure = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            List<LawArticle> articles = new ArrayList<>();

            for (int i = 1; i < rows.size(); i++) {  // 헤더가 존재한다고 가정
                try {
                    String[] row = rows.get(i);

                    String theme = row[1].trim();     // 'theme' 필드
                    String content = row[2].trim();   // 'content' 필드

                    LawArticle article = LawArticle.builder()
                            .theme(theme)
                            .content(content)
                            .aiSummary(null)  // 요약은 나중에 별도 호출
                            .build();

                    articles.add(article);
                    success++;
                } catch (Exception e) {
                    failure++;
                    e.printStackTrace();  // 에러 추적
                }
            }

            lawArticleRepository.saveAll(articles);
        }

        return LawArticleUploadResponseDto.builder()
                .successCount(success)
                .failureCount(failure)
                .build();
    }

    // AI 서버로 요약 요청 후 DB에 aiSummary 저장
    @Transactional
    public LawArticleResponseDto summarizeAndUpdate(Long id) {
        LawArticle article = lawArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LawArticle not found"));

        try {
            log.info("LawArticle ID={} 요약 요청 시작", id);

            String content = article.getContent();
            String aiSummary = aiSummaryClient.requestSummary(content);
            article.setAiSummary(aiSummary);
            lawArticleRepository.save(article);

            log.info("LawArticle ID={} 요약 저장 완료", id);
        } catch (Exception e) {
            log.error("LawArticle ID={} 요약 실패: {}", id, e.getMessage(), e);
            throw new RuntimeException("AI 요약 요청 실패: " + e.getMessage(), e);
        }

        return modelMapper.map(article, LawArticleResponseDto.class);
    }
}
