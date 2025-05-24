package com.deepread.controller;

import com.deepread.service.OpenAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DictionaryController.class)
@AutoConfigureMockMvc
class DictionaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpenAPIService openAPIService;

    @Test
    @DisplayName("정상적인 단어 검색 요청 시 의미를 반환한다")
    void getMeans_shouldReturnDefinition() throws Exception {
        // given
        String word = "사랑";
        String meaning = "어떤 대상에 대하여 끌리고 소중히 여기는 마음.";
        when(openAPIService.getMeans(word)).thenReturn(meaning);

        // when & then
        mockMvc.perform(get("/api/dictionary/means")
                        .param("word", word)
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string(meaning));
    }

    @Test
    @DisplayName("비어 있는 단어 검색 시 400 반환")
    void getMeans_withBlankWord_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/dictionary/means")
                        .param("word", " ")
                        .with(user("user").roles("USER")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("검색어를 입력해주세요."));
    }

    @Test
    @DisplayName("존재하지 않는 단어 검색 시 400 반환")
    void getMeans_withInvalidWord_shouldReturnNotFound() throws Exception {
        when(openAPIService.getMeans("없는단어"))
                .thenThrow(new IllegalArgumentException("존재하지 않는 단어입니다."));

        mockMvc.perform(get("/api/dictionary/means")
                        .param("word", "없는단어")
                        .with(user("user").roles("USER")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("존재하지 않는 단어입니다."));
    }

    @Test
    @DisplayName("내부 API 오류 시 500 반환")
    void getMeans_withIOException_shouldReturnInternalServerError() throws Exception {
        when(openAPIService.getMeans("사랑"))
                .thenThrow(new IOException("API 요청 중 오류 발생"));

        mockMvc.perform(get("/api/dictionary/means")
                        .param("word", "사랑")
                        .with(user("user").roles("USER")))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("API 요청 중 오류 발생"));
    }
}
