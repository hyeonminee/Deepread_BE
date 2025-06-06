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
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(new EtriRequest(new EtriArgument(word)));

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", accessKey);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        log.info("ETRI 응답 원본: {}", response.toString());


        JsonNode root = objectMapper.readTree(response.toString());
        JsonNode returnObject = root.path("return_object");
        if (returnObject.isMissingNode()) {
            throw new IOException("Invalid API response: 'return_object' 없음");
        }

        JsonNode wordInfoNode = returnObject.path("WordInfo");
        if (!wordInfoNode.isArray() || wordInfoNode.isEmpty()) {
            throw new IOException("단어 정보가 존재하지 않습니다.");
        }
        JsonNode info = wordInfoNode.get(0);

        String wordText = returnObject.path("Word").asText();
        String pos = info.path("POS").asText();
        String definition = info.path("Definition").asText();
        String hanja = info.path("Origin").asText();
        String example = info.path("Example").asText();

        // Synonym, Antonym은 JSON 문자열로 들어오기 때문에 재파싱 필요
        List<String> synonymList = new ArrayList<>();
        List<String> antonymList = new ArrayList<>();

        try {
            String synRaw = info.path("Synonym").asText();
            JsonNode synArray = objectMapper.readTree(synRaw);
            if (synArray.isArray()) {
                synArray.forEach(n -> synonymList.add(n.asText()));
            }
        } catch (Exception e) {
            // syn 파싱 실패 시 무시
        }

        try {
            String antRaw = info.path("Antonym").asText();
            JsonNode antArray = objectMapper.readTree(antRaw);
            if (antArray.isArray()) {
                antArray.forEach(n -> antonymList.add(n.asText()));
            }
        } catch (Exception e) {
            // ant 파싱 실패 시 무시
        }

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
