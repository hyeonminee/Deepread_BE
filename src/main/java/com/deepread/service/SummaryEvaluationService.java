package com.deepread.service;

import com.deepread.exception.FlaskApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class SummaryEvaluationService {

    private final RestTemplate restTemplate;
    private final String flaskUrl;

    public SummaryEvaluationService(@Value("${flask.api.url}") String flaskUrl) {
        this.flaskUrl = flaskUrl;
        this.restTemplate = new RestTemplate();
        this.restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(flaskUrl));
    }

    public Map<String, Object> evaluateSummary(String originalText, String aiSummary, String userSummary) {
        String url = "/evaluate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("text", originalText);
        payload.put("ai_summary", aiSummary);
        payload.put("user_summary", userSummary);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new FlaskApiException("Flask 서버 요청 중 오류 발생", e);
        }

    }
}