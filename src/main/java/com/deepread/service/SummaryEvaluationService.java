package com.deepread.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class SummaryEvaluationService {

    private final RestTemplate restTemplate; // Flask REST 호출 도구
    private final String flaskUrl; // Flask 서버 base URL

    public SummaryEvaluationService(@Value("${flask.api.url}") String flaskUrl) {
        this.flaskUrl = flaskUrl;
        this.restTemplate = new RestTemplate();
        this.restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(flaskUrl));
    }

    // Flask 서버에 요약 평가 요청
    public Map<String, Object> evaluateSummary(String originalText, String summaryText) {
        String url = "/evaluate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("text", originalText); // 원문
        payload.put("summary", summaryText); // 사용자의 요약문

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        // Flask 서버의 응답 JSON -> Map 변환
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return response.getBody();
    }
}
