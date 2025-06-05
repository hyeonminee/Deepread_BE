package com.deepread.controller;

import com.deepread.dto.request.QuizAnswerValidationRequestDto;
import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizAnswerValidationResponseDto;
import com.deepread.dto.response.QuizQuestionResponseDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.entity.QuizLevel;
import com.deepread.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Quiz", description = "퀴즈 관련 API")
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // CSV 파일 업로드
    @Operation(summary = "퀴즈 문제 CSV 업로드", description = "CSV 파일로 퀴즈 문제를 업로드한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "CSV 형식 오류 또는 파싱 실패"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/questions/upload")
    public ResponseEntity<String> uploadQuizQuestions(@RequestParam("file") MultipartFile file) {
        quizService.uploadQuestionsFromCsv(file);
        return ResponseEntity.ok("퀴즈 문제가 성공적으로 업로드되었습니다.");
    }

    // 레벨별 5문제 랜덤 반환
    @Operation(summary = "레벨별 퀴즈 문제 5개 조회", description = "요청한 레벨에 맞는 퀴즈 문제 5개를 반환한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 레벨 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/questions")
    public ResponseEntity<List<QuizQuestionResponseDto>> getQuizQuestionsByLevel(
            @Parameter(description = "퀴즈 레벨 (예: 초급, 중급, 고급)", example = "초급")
            @RequestParam("level") QuizLevel level) {
        List<QuizQuestionResponseDto> questions = quizService.getQuizQuestionsByLevel(level);
        return ResponseEntity.ok(questions);
    }

    // 문제 정답 검증 (개별 문제별)
    @Operation(summary = "퀴즈 정답 검증", description = "문제 ID와 사용자의 선택을 기반으로 정답 여부를 판단한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정답 검증 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 오류"),
            @ApiResponse(responseCode = "404", description = "문제 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/validate")
    public ResponseEntity<QuizAnswerValidationResponseDto> validateAnswer(
            @RequestBody @Valid QuizAnswerValidationRequestDto dto) {
        QuizAnswerValidationResponseDto result = quizService.validateAnswer(dto.getQuestionId(), dto.getSelectedOption());
        return ResponseEntity.ok(result);
    }

    // 전체 푼 결과 저장 (사용자가 정답 개수 계산 후 전달)
    @Operation(summary = "퀴즈 결과 저장", description = "사용자가 푼 퀴즈의 정답 수와 정확도를 저장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결과 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 오류"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/result")
    public ResponseEntity<QuizResultResponseDto> saveQuizResult(@RequestBody @Valid QuizResultRequestDto dto) {
        QuizResultResponseDto response = quizService.saveQuizResult(dto);
        return ResponseEntity.ok(response);
    }

    // 단일 문제 조회 (관리자용)
    @Operation(summary = "문제 단건 조회 (관리자)", description = "ID로 퀴즈 문제를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "문제 ID를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuizQuestionResponseDto> getQuestionById(@PathVariable Long id) {
        QuizQuestionResponseDto dto = quizService.getQuestionById(id);
        return ResponseEntity.ok(dto);
    }

    // 전체 문제 페이징 조회 (관리자용)
    @Operation(summary = "전체 퀴즈 문제 조회 (관리자)", description = "모든 퀴즈 문제를 페이징 처리하여 반환한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/questions/all")
    public ResponseEntity<Page<QuizQuestionResponseDto>> getAllQuestions(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<QuizQuestionResponseDto> result = quizService.getAllQuestions(pageable);
        return ResponseEntity.ok(result);
    }
}
