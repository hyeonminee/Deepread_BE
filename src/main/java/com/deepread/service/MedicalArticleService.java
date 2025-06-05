package com.deepread.service;

import com.deepread.client.AiSummaryClient;
import com.deepread.dto.response.MedicalArticleResponseDto;
import com.deepread.dto.response.MedicalArticleUploadResponseDto;
import com.deepread.entity.MedicalArticle;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.MedicalArticleRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public List<MedicalArticleResponseDto> getArticlesByLevel(User.Level level) {
        return medicalArticleRepository.findByLevel(level).stream()
                .map(article -> modelMapper.map(article, MedicalArticleResponseDto.class))
                .collect(Collectors.toList());
    }

    // CSV 파일 업로드 처리 (aiSummary는 null로 저장됨)
    public MedicalArticleUploadResponseDto uploadCsv(MultipartFile file) throws Exception {
        int success = 0;
        int failure = 0;
        List<MedicalArticle> articles = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                try {
                    String theme = rows.get(i)[1].trim(); // 'theme' 필드
                    String content = rows.get(i)[2].trim(); // 'content' 필드
                    String levelText = rows.get(i)[3].trim(); // 'level' 필드
                    User.Level level = User.Level.valueOf(levelText); // "초급", "중급", "고급"과 정확히 일치해야 함

                    MedicalArticle article = MedicalArticle.builder()
                            .theme(theme)
                            .content(content)
                            .aiSummary(null) // 요약은 나중에 별도 호출
                            .level(level)
                            .build();

                    articles.add(article);
                    success++;
                } catch (Exception e) {
                    failure++;
                }
            }

            medicalArticleRepository.saveAll(articles);
        }

        return MedicalArticleUploadResponseDto.builder()
                .successCount(success)
                .failureCount(failure)
                .build();
    }

    // AI 서버로 요약 요청 후 DB에 aiSummary 저장
    @Transactional
    public MedicalArticleResponseDto summarizeAndUpdate(Long id) {
        MedicalArticle article = medicalArticleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalArticle not found"));

        try {
            log.info("MedicalArticle ID={} 요약 요청 시작", id);

            String content = article.getContent();
            String aiSummary = aiSummaryClient.requestSummary(content);
            article.setAiSummary(aiSummary);
            medicalArticleRepository.save(article);

            log.info("MedicalArticle ID={} 요약 저장 완료", id);
        } catch (Exception e) {
            log.error("MedicalArticle ID={} 요약 실패: {}", id, e.getMessage(), e);
            throw new RuntimeException("AI 요약 요청 실패: " + e.getMessage(), e);
        }

        return modelMapper.map(article, MedicalArticleResponseDto.class);
    }
}
