package com.deepread.controller;

import com.deepread.dto.request.DiagnosisEvaluationRequestDto;
import com.deepread.dto.request.DiagnosisResultRequestDto;
import com.deepread.dto.response.DiagnosisEvaluationResponseDto;
import com.deepread.dto.response.DiagnosisResultResponseDto;
import com.deepread.dto.response.DiagnosisQuestionResponseDto;
import com.deepread.entity.DiagnosisQuestion;
import com.deepread.repository.DiagnosisQuestionRepository;
import com.deepread.service.DiagnosisQuestionService;
import com.deepread.service.DiagnosisService;
import com.opencsv.CSVReader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final DiagnosisQuestionService diagnosisQuestionService;
    private final DiagnosisQuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    // 진단 결과 저장
    @Operation(summary = "진단 결과 저장", description = "사용자의 진단 결과를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진단 결과 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<DiagnosisResultResponseDto> submitDiagnosis(@RequestBody @Valid DiagnosisResultRequestDto dto) {
        DiagnosisResultResponseDto responseDto = diagnosisService.submitDiagnosisResult(dto);
        return ResponseEntity.ok(responseDto);
    }

    // 랜덤 문제 출제 (A:1, B:2, C:2)
    @Operation(summary = "문해력 진단 문제 랜덤 출제", description = "A:1개, B:2개, C:2개 문제를 무작위로 반환")
    @GetMapping("/questions")
    public ResponseEntity<List<DiagnosisQuestionResponseDto>> getRandomQuestions() {
        return ResponseEntity.ok(diagnosisQuestionService.getMixedQuestions());
    }

    // 사용자 채점
    @Operation(summary = "문해력 진단 결과 평가", description = "사용자 응답을 기반으로 정답 여부를 판단하고 점수를 계산한다.")
    @PostMapping("/evaluate")
    public ResponseEntity<DiagnosisEvaluationResponseDto> evaluateAnswers(@RequestBody DiagnosisEvaluationRequestDto dto) {
        int correctCount = 0;
        List<Boolean> results = new ArrayList<>();

        for (DiagnosisEvaluationRequestDto.AnswerSubmission submission : dto.getAnswers()) {
            DiagnosisQuestion question = questionRepository.findById(submission.getId())
                    .orElseThrow(() -> new IllegalArgumentException("문항 ID " + submission.getId() + "를 찾을 수 없습니다."));
            boolean isCorrect = question.getAnswer().equals(submission.getAnswer());
            results.add(isCorrect);
            if (isCorrect) correctCount++;
        }

        DiagnosisEvaluationResponseDto res = new DiagnosisEvaluationResponseDto();
        res.setScore(correctCount * 20);
        res.setResults(results);
        return ResponseEntity.ok(res);
    }

    // CSV 일괄 등록
    @Operation(summary = "문해력 진단 문제 일괄 등록", description = "CSV 파일을 업로드하여 문제를 DB에 일괄 등록한다.")
    @PostMapping("/questions/batch")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            List<DiagnosisQuestion> questions = new ArrayList<>();

            for (int i = 1; i < rows.size(); i++) { // skip header
                String[] tokens = rows.get(i);
                DiagnosisQuestion q = new DiagnosisQuestion();
                q.setType(tokens[0].trim());
                q.setId(Long.parseLong(tokens[1].trim()));
                q.setPassage(tokens[2].trim());
                q.setQuestion(tokens[3].trim());
                q.setOption1(tokens[4].trim());
                q.setOption2(tokens[5].trim());
                q.setOption3(tokens[6].trim());
                q.setOption4(tokens[7].trim());
                q.setAnswer(Integer.parseInt(tokens[8].trim()));
                questions.add(q);
            }

            questionRepository.saveAll(questions);
            return ResponseEntity.ok("총 " + questions.size() + "개의 문제가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("CSV 처리 중 오류: " + e.getMessage());
        }
    }

    // 전체 문제 조회 (관리자용)
    @Operation(summary = "문해력 진단 문제 전체 조회 (관리자용)", description = "전체 문제를 관리용으로 조회한다.")
    @GetMapping("/questions/admin")
    public ResponseEntity<List<DiagnosisQuestionResponseDto>> getAllQuestionsAdmin() {
        return ResponseEntity.ok(
                questionRepository.findAll().stream()
                        .map(q -> modelMapper.map(q, DiagnosisQuestionResponseDto.class))
                        .collect(Collectors.toList())
        );
    }
}
