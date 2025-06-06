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
        String requestBody = objectMapper.writeValueAsString(new EtriRequest(new EtriArgument(word)));

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
            throw new IOException("Invalid API response: 'return_object' 없음");
        }

        // WordInfo는 배열이므로 첫 번째 요소만 사용
        JsonNode wordInfoNode = returnObject.path("WordInfo");
        if (!wordInfoNode.isArray() || wordInfoNode.isEmpty()) {
            throw new IOException("단어 정보가 존재하지 않습니다.");
        }
        JsonNode info = wordInfoNode.get(0);

        // 필드 파싱
        String wordText = returnObject.path("Word").asText();
        String pos = info.path("POS").asText();
        String definition = info.path("Definition").asText();
        String hanja = info.path("Origin").asText();
        String example = info.path("Example").asText();

        List<String> synonymList = new ArrayList<>();
        JsonNode synNode = returnObject.path("Synonym");
        if (synNode.isArray()) {
            synNode.forEach(n -> synonymList.add(n.asText()));
        }

        List<String> antonymList = new ArrayList<>();
        JsonNode antNode = returnObject.path("Antonym");
        if (antNode.isArray()) {
            antNode.forEach(n -> antonymList.add(n.asText()));
        }

        // DTO 반환
        return MeansResponseDto.builder()
                .word(wordText)
                .pos(pos)
                .definition(definition)
                .hanja(hanja)
                .example(example)
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
