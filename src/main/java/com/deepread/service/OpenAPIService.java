package com.deepread.service;

import com.deepread.dto.response.MeansResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class OpenAPIService {

    // ETRI API 인증키
    private final String accessKey = "REMOVED";

    // ETRI 어휘 정보 API 호출 URL
    private final String apiUrl = "http://aiopen.etri.re.kr:8000/WiseWWN/Word";

    // 입력된 단어의 의미를 ETRI 어휘 정보 API를 통해 조회
    @Transactional(readOnly = true)
    public MeansResponseDto getMeans(String word) throws IOException {
        // JSON 요청 본문 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(
                new EtriRequest(new EtriArgument(word))
        );

        // HTTP POST 요청 생성
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", accessKey);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true); // 요청 본문 전송 가능 설정

        // 요청 본문 전송
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        // 응답 수신
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // 응답 JSON 파싱
        JsonNode root = objectMapper.readTree(response.toString());
        JsonNode returnObject = root.path("return_object");

        // 응답 오류 처리
        if (returnObject.isMissingNode()) {
            throw new IOException("Invalid API response");
        }

        // 단어 정보 필드 추출
        String wordText = returnObject.path("Word").asText();                       // 단어
        String pos = returnObject.path("WordInfo").path("POS").asText();           // 품사
        String definition = returnObject.path("WordInfo").path("Definition").asText(); // 뜻풀이
        String hanja = returnObject.path("WordInfo").path("Origin").asText();      // 한자
        String example = returnObject.path("WordInfo").path("Example").asText();   // 예문
        String synonym = returnObject.path("Synonym").toString();                  // 유의어
        String antonym = returnObject.path("Antonym").toString();                  // 반의어

        // DTO로 변환 후 반환
        return MeansResponseDto.builder()
                .word(wordText)
                .pos(pos)
                .definition(definition)
                .hanja(hanja)
                .example(example)
                .synonym(synonym)
                .antonym(antonym)
                .build();
    }

    // 내부 클래스: 요청 본문 포맷 정의
    static class EtriRequest {
        public EtriArgument argument;

        public EtriRequest(EtriArgument argument) {
            this.argument = argument;
        }
    }

    // 내부 클래스: 단어 인자를 포함한 구조
    static class EtriArgument {
        public String word;

        public EtriArgument(String word) {
            this.word = word;
        }
    }
}
