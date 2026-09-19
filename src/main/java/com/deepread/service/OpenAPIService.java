package com.deepread.service;

import com.deepread.dto.response.MeansResponseDto;
import com.deepread.dto.response.MeansResponseDto.MeaningDetail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAPIService {

    private static final Logger log = LoggerFactory.getLogger(OpenAPIService.class);

    private final String accessKey;
    private final String apiUrl;

    public OpenAPIService(
            @Value("${etri.api.key}") String accessKey,
            @Value("${etri.api.url}") String apiUrl
    ) {
        this.accessKey = accessKey;
        this.apiUrl = apiUrl;
    }

    /**
     * 주어진 단어에 대해 ETRI 어휘 API를 호출하여 뜻, 품사, 유의어, 반의어를 반환합니다.
     */
    @Transactional(readOnly = true)
    public MeansResponseDto getMeans(String word) throws IOException {
        // 요청 JSON 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(new EtriRequest(new EtriArgument(word)));

        // HTTP 연결 설정
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", accessKey);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        // 요청 전송
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

        // 응답 파싱
        JsonNode root = objectMapper.readTree(response.toString());

        // 결과 코드 확인
        int resultCode = root.path("result").asInt(-1);
        if (resultCode != 0) {
            throw new IOException("ETRI API 응답 실패 (result=" + resultCode + ")");
        }

        // return_object 추출
        JsonNode returnObject = root.path("return_object");
        if (returnObject.isMissingNode()) {
            throw new IOException("'return_object' 없음");
        }

        // 'WWN WordInfo' 배열에서 첫 항목 추출
        JsonNode wordInfoGroup = returnObject.path("WWN WordInfo");
        if (!wordInfoGroup.isArray() || wordInfoGroup.isEmpty()) {
            throw new IOException("'WWN WordInfo' 항목이 비어 있음");
        }

        JsonNode wordNode = wordInfoGroup.get(0);
        String wordText = wordNode.path("Word").asText(null);

        // 다의어 정의 정보 추출
        JsonNode wordInfoArray = wordNode.path("WordInfo");
        if (!wordInfoArray.isArray() || wordInfoArray.isEmpty()) {
            throw new IOException("WordInfo 배열 없음 또는 비어 있음");
        }

        List<MeaningDetail> meaningList = new ArrayList<>();
        for (JsonNode info : wordInfoArray) {
            String pos = info.path("POS").asText(null);
            String definition = info.path("Definition").asText(null);
            if (pos != null && !pos.isBlank() && definition != null && !definition.isBlank()) {
                meaningList.add(MeaningDetail.builder()
                        .pos(pos)
                        .definition(definition)
                        .build());
            }
        }

        // 유의어 추출
        List<String> synonymList = new ArrayList<>();
        JsonNode synonymArray = wordNode.path("Synonym");
        if (synonymArray != null && synonymArray.isArray()) {
            synonymArray.forEach(n -> synonymList.add(n.asText()));
        }

        // 반의어 추출
        List<String> antonymList = new ArrayList<>();
        JsonNode antonymArray = wordNode.path("Antonym");
        if (antonymArray != null && antonymArray.isArray()) {
            antonymArray.forEach(n -> antonymList.add(n.asText()));
        }

        // 최종 DTO 반환
        return MeansResponseDto.builder()
                .word(wordText)
                .meanings(meaningList)
                .synonym(synonymList)
                .antonym(antonymList)
                .build();
    }

    // 내부 클래스: 요청 본문 구조 정의
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
