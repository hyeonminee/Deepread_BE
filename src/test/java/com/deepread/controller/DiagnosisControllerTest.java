package com.deepread.controller;

import com.deepread.dto.request.DiagnosisResultRequestDto;
import com.deepread.dto.response.DiagnosisResultResponseDto;
import com.deepread.service.DiagnosisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiagnosisController.class)
@AutoConfigureMockMvc(addFilters = false)
class DiagnosisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiagnosisService diagnosisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("진단 결과 저장 성공")
    void submitDiagnosis_success() throws Exception {
        // given
        DiagnosisResultRequestDto requestDto = new DiagnosisResultRequestDto();
        requestDto.setUserId(1L);
        requestDto.setScore(75);
        requestDto.setUserLevel(com.deepread.entity.User.Level.중급);

        DiagnosisResultResponseDto responseDto = new DiagnosisResultResponseDto();
        responseDto.setUserId(1L);
        responseDto.setScore(75);
        responseDto.setUserLevel(com.deepread.entity.User.Level.중급);

        // when
        Mockito.when(diagnosisService.submitDiagnosisResult(any(DiagnosisResultRequestDto.class)))
                .thenReturn(responseDto);

        // then
        mockMvc.perform(post("/api/diagnosis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.score").value(75))
                .andExpect(jsonPath("$.userLevel").value("중급"));
    }

    @Test
    @WithMockUser
    @DisplayName("진단 결과 저장 실패 - 누락된 사용자 ID")
    void submitDiagnosis_invalidRequest() throws Exception {
        DiagnosisResultRequestDto invalidDto = new DiagnosisResultRequestDto();

        mockMvc.perform(post("/api/diagnosis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}