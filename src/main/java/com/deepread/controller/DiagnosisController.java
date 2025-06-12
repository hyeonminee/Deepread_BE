package com.deepread.controller;

import com.deepread.dto.request.DiagnosisEvaluationRequestDto;
import com.deepread.dto.request.DiagnosisResultRequestDto;
import com.deepread.dto.response.DiagnosisEvaluationResponseDto;
import com.deepread.dto.response.DiagnosisQuestionResponseDto;
import com.deepread.dto.response.DiagnosisResultResponseDto;
import com.deepread.entity.DiagnosisQuestion;
import com.deepread.entity.User;
import com.deepread.repository.DiagnosisQuestionRepository;
import com.deepread.service.DiagnosisQuestionService;
import com.deepread.service.DiagnosisService;
import com.opencsv.CSVReader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Diagnosis", description = "문해력 진단 관련 API")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final DiagnosisQuestionService diagnosisQuestionService;
    private final DiagnosisQuestionRepository questionRepository;
    private final ModelMapper modelMapper;

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

    @Operation(
            summary = "문해력 진단 문제 랜덤 출제",
            description = "A:1개, B:2개, C:2개 문제를 무작위로 반환한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "문제 출제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/questions")
    public ResponseEntity<List<DiagnosisQuestionResponseDto>> getRandomQuestions() {
        return ResponseEntity.ok(diagnosisQuestionService.getMixedQuestions());
    }

    @PostMapping("/evaluate")
    @Operation(
            summary = "문해력 진단 결과 평가",
            description = "사용자 응답을 기반으로 정답 여부를 판단하고 점수를 계산하며, 문해력 수준을 판정한다. (DB 저장은 하지 않음)" +
                    "0~40 점 : 초급, 41~75 점 : 중급, 76 ~ 100 점 : 고급"


    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "평가 완료 및 점수 및 수준 반환"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<DiagnosisEvaluationResponseDto> evaluateAnswers(@RequestBody DiagnosisEvaluationRequestDto dto) {
        int totalScore = 0;

        // 문제별 배점 (사용자가 항상 5개 문항을 동일한 순서로 제출한다고 가정)
        int[] weights = {10, 15, 15, 30, 30};

        List<DiagnosisEvaluationRequestDto.AnswerSubmission> answers = dto.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            DiagnosisEvaluationRequestDto.AnswerSubmission submission = answers.get(i);

            DiagnosisQuestion question = questionRepository.findById(submission.getId())
                    .orElseThrow(() -> new IllegalArgumentException("문항 ID " + submission.getId() + "를 찾을 수 없습니다."));

            boolean isCorrect = question.getAnswer().equals(submission.getAnswer());
            if (isCorrect) {
                totalScore += weights[i];
            }
        }

        DiagnosisEvaluationResponseDto res = new DiagnosisEvaluationResponseDto();
        res.setScore(totalScore);
        res.setUserLevel(determineLevel(totalScore));

        return ResponseEntity.ok(res);
    }

    private User.Level determineLevel(int score) {
        if (score <= 40) return User.Level.초급;
        else if (score <= 75) return User.Level.중급;
        else return User.Level.고급;
    }


    @Operation(
            summary = "문해력 진단 문제 일괄 등록",
            description = "CSV 파일의 각 행은 문제(type,id,passage,question,option1~4,answer)를 나타내며, 이를 DB에 저장한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일괄 등록 성공"),
            @ApiResponse(responseCode = "500", description = "CSV 처리 중 오류")
    })
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

    @Operation(summary = "문해력 진단 문제 전체 조회", description = "전체 문제를 관리용으로 조회한다. (관리자용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 문제 반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/questions/admin")
    public ResponseEntity<List<DiagnosisQuestionResponseDto>> getAllQuestionsAdmin() {
        return ResponseEntity.ok(
                questionRepository.findAll().stream()
                        .map(q -> modelMapper.map(q, DiagnosisQuestionResponseDto.class))
                        .collect(Collectors.toList())
        );
    }
}
