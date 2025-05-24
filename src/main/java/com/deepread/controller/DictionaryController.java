package com.deepread.controller;

import com.deepread.service.OpenAPIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final OpenAPIService openAPIService;

    @Operation(summary = "단어 의미 조회", description = "국어사전 API를 통해 단어의 의미를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단어 의미 조회 성공"),
            @ApiResponse(responseCode = "400", description = "입력 단어 누락 또는 잘못된 단어"),
            @ApiResponse(responseCode = "500", description = "외부 API 또는 서버 오류")
    })
    @GetMapping("/means")
    public ResponseEntity<String> getMeans(@RequestParam("word") String word) throws IOException {
        if (word == null || word.isBlank()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }
        return ResponseEntity.ok(openAPIService.getMeans(word));
    }
}
