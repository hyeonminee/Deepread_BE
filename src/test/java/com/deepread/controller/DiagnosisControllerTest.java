package com.deepread.controller;

import com.deepread.entity.DiagnosisResult;
import com.deepread.entity.User;
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
    @WithMockUser // 인증된 사용자로 설정 (기본값: username="user")
    @DisplayName("진단 결과 저장 성공")
    void submitDiagnosis_success() throws Exception {
        DiagnosisResult result = new DiagnosisResult();
        User user = new User();
        user.setId(1L);
        result.setUser(user);

        Mockito.when(diagnosisService.submitDiagnosisResult(any(DiagnosisResult.class)))
                .thenReturn(result);

        mockMvc.perform(post("/api/diagnosis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(result)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유효하지 않은 진단 결과 예외 발생")
    void submitDiagnosis_invalidRequest() throws Exception {
        DiagnosisResult invalid = new DiagnosisResult(); // user == null

        mockMvc.perform(post("/api/diagnosis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("진단 결과 또는 사용자 정보가 유효하지 않습니다."));
    }
}
