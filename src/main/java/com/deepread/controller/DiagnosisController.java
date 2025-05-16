package com.deepread.controller;

import com.deepread.dto.request.DiagnosisResultRequestDto;
import com.deepread.dto.response.DiagnosisResultResponseDto;
import com.deepread.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @Operation(summary = "진단 결과 저장", description = "사용자의 진단 결과를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "진단 결과 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 오류 (유효성 검증 실패 등)"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<DiagnosisResultResponseDto> submitDiagnosis(@RequestBody @Valid DiagnosisResultRequestDto dto) {
        DiagnosisResultResponseDto responseDto = diagnosisService.submitDiagnosisResult(dto);
        return ResponseEntity.ok(responseDto);
    }
}
