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
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class OpenAPIService {
    private static final Logger log = LoggerFactory.getLogger(OpenAPIService.class);


    private final String accessKey = "REMOVED";
    private final String apiUrl = "http://aiopen.etri.re.kr:8000/WiseWWN/Word";

    @Transactional(readOnly = true)
    public MeansResponseDto getMeans(String word) throws IOException {
        // 요청 JSON 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(new EtriRequest(new EtriArgument(word)));

        // HTTP POST 요청 설정
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", accessKey);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        // 요청 본문 전송
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        // 응답 수신
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        log.info("ETRI 응답 원본: {}", response);

        JsonNode root = objectMapper.readTree(response.toString());

        // result code 확인
        int resultCode = root.path("result").asInt(-1);
        if (resultCode != 0) {
            throw new IOException("ETRI API 응답 실패 (result=" + resultCode + ")");
        }

        // return_object 확인
        JsonNode returnObject = root.path("return_object");
        if (returnObject.isMissingNode()) {
            throw new IOException("'return_object' 없음");
        }

        // Word와 WordInfo 리스트 추출
        String wordText = returnObject.path("WWN WordInfo").get(0).path("Word").asText(null);
        JsonNode wordInfoArray = returnObject.path("WWN WordInfo").get(0).path("WordInfo");

        if (wordText == null || wordInfoArray == null || !wordInfoArray.isArray() || wordInfoArray.isEmpty()) {
            throw new IOException("단어 정보 누락");
        }

        JsonNode info = wordInfoArray.get(0); // 첫 번째 의미 사용

        // 필수 정보 파싱 (null or blank 확인)
        String pos = info.path("POS").asText(null);
        String definition = info.path("Definition").asText(null);
        if (pos == null || pos.isBlank() || definition == null || definition.isBlank()) {
            throw new IOException("POS 또는 Definition 누락");
        }

        // 유의어/반의어 파싱
        List<String> synonymList = new ArrayList<>();
        JsonNode synArray = returnObject.path("WWN WordInfo").get(0).path("Synonym");
        if (synArray != null && synArray.isArray()) {
            synArray.forEach(n -> synonymList.add(n.asText()));
        }

        List<String> antonymList = new ArrayList<>();
        JsonNode antArray = returnObject.path("WWN WordInfo").get(0).path("Antonym");
        if (antArray != null && antArray.isArray()) {
            antArray.forEach(n -> antonymList.add(n.asText()));
        }

        // DTO 생성 및 반환
        return MeansResponseDto.builder()
                .word(wordText)
                .pos(pos)
                .definition(definition)
                .synonym(synonymList)
                .antonym(antonymList)
                .build();
    }

    // 내부 클래스: 요청 포맷
    static class EtriRequest {
        public EtriArgument argument;

        public EtriRequest(EtriArgument argument) {
            this.argument = argument;
        }
    }

    static class EtriArgument {
        public String word;

        public EtriArgument(String word) {
            this.word = word;
        }
    }
}
