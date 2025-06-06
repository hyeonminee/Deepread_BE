package com.deepread.client;

import com.deepread.dto.request.AiSummaryRequestDto;
import com.deepread.dto.response.AiSummaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AiSummaryClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${flask.api.url}/generate-summary")
    private String summaryApiUrl;  // ex) http://3.35.200.173:5000/generate-summary

    public String requestSummary(String content) throws Exception {
        AiSummaryRequestDto requestDto = AiSummaryRequestDto.builder().text(content).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiSummaryRequestDto> entity = new HttpEntity<>(requestDto, headers);

        try {
            ResponseEntity<AiSummaryResponseDto> response = restTemplate.exchange(
                    summaryApiUrl,
                    HttpMethod.POST,
                    entity,
                    AiSummaryResponseDto.class
            );
            return response.getBody().getAi_summary();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("AI 요약 요청 실패 (Client Error): " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("AI 요약 요청 실패 (Server Error): " + e.getMessage(), e);
        }
    }
}
