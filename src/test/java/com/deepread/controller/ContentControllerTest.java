package com.deepread.controller;

import com.deepread.dto.response.LawArticleResponseDto;
import com.deepread.dto.response.MedicalArticleResponseDto;
import com.deepread.dto.response.NewsArticleResponseDto;
import com.deepread.dto.response.RecommendedArticlesDto;
import com.deepread.entity.Content;
import com.deepread.entity.User;
import com.deepread.service.ContentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContentController.class)
class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("레벨에 따라 콘텐츠 추천 성공")
    void getRecommendedContents_success() throws Exception {
        RecommendedArticlesDto dto = RecommendedArticlesDto.builder()
                .lawArticles(List.of(new LawArticleResponseDto(1L, "법률 제목", "...")))
                .medicalArticles(List.of(new MedicalArticleResponseDto(2L, "의료 제목", "...")))
                .newsArticles(List.of(new NewsArticleResponseDto(3L, "뉴스 제목", "...")))
                .build();

        User mockUser = new User();
        mockUser.setId(1L);

        when(recommendationService.getRecommendedArticles(any(Long.class))).thenReturn(dto);

        mockMvc.perform(get("/api/contents/recommend")
                        .principal(() -> "user")) // OAuth mock 대신 Principal 제공
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lawArticles[0].title").value("법률 제목"))
                .andExpect(jsonPath("$.medicalArticles[0].title").value("의료 제목"))
                .andExpect(jsonPath("$.newsArticles[0].title").value("뉴스 제목"));
    }


    @Test
    @WithMockUser
    @DisplayName("콘텐츠 ID로 조회 성공")
    void getContentById_success() throws Exception {
        Content content = new Content();
        content.setId(2L);
        content.setTitle("중급 콘텐츠");

        when(contentService.getContentById(2L)).thenReturn(Optional.of(content));

        mockMvc.perform(get("/api/contents/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("중급 콘텐츠"));
    }

    @Test
    @WithMockUser
    @DisplayName("존재하지 않는 콘텐츠 조회 시 예외 발생")
    void getContentById_notFound() throws Exception {
        when(contentService.getContentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/contents/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("콘텐츠를 찾을 수 없습니다."));
    }
}