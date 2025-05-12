package com.deepread.controller;

import com.deepread.entity.DiagnosisResult;
import com.deepread.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController // Rest API 컨트롤러임을 명시
@RequestMapping("/api/diagnosis") // 진단 기능과 관련된 API 경로를 /api/diagnosis로 지정
@RequiredArgsConstructor
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @Operation(summary = "진단 결과 저장", description = "사용자의 진단 결과를 저장한다.")
    @ApiResponse(responseCode = "200", description = "진단 결과 저장 완료")
    @PostMapping // @PostMapping: 클라이언트가 진단 결과를 제출할 때 사용하는 HTTP 메서드
    // @RequestBody: 클라이언트가 전송한 JSON 데이터를 DiagnosisResult 객체로 변환
    public DiagnosisResult submitDiagnosis(@RequestBody DiagnosisResult result) {
        return diagnosisService.submitDiagnosisResult(result);
    }
}
