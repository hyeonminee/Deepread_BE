package com.deepread.service;

import com.deepread.dto.request.MeansReq;
import com.deepread.dto.response.MeansRes;
import com.deepread.dto.response.MeansResponseDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAPIService {

    private final String key = "66745AFF64C80B68E135A40C58CC1A40";

    @Transactional(readOnly = true)
    public MeansResponseDto getMeans(String word) throws IOException {
        String baseUrl = "https://stdict.korean.go.kr/api/search.do";
        String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8);

        MeansReq req = new MeansReq(key, encodedWord);
        StringBuilder result = new StringBuilder();

        URL url = new URL(baseUrl + req.getParameter());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
        String returnLine;
        while ((returnLine = bufferedReader.readLine()) != null) {
            result.append(returnLine);
        }

        JSONObject jsonObject = XML.toJSONObject(result.toString());

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        MeansRes meansRes = mapper.readValue(jsonObject.toString(), MeansRes.class);

        if (meansRes.getChannel().getItem() == null || meansRes.getChannel().getItem().isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 단어입니다.");
        }

        MeansRes.Item item = meansRes.getChannel().getItem().get(0);
        MeansRes.Sense sense = item.getSense().get(0);

        return MeansResponseDto.builder()
                .word(item.getWord())
                .pos(item.getPos())
                .definition(sense.getDefinition())
                .pattern(sense.getPattern())
                .example(sense.getExample())
                .proverb(sense.getProverb())
                .usage(sense.getUsage())
                .build();
    }
}
