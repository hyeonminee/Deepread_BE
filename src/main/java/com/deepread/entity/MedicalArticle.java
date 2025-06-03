package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;  // 콘텐츠 제목

    @Column(columnDefinition = "TEXT")
    private String content;  // 원문 (XML 파싱 결과)

    private String sourceUrl;  // 출처 URL (선택사항)

    @Column(columnDefinition = "TEXT")
    private String aiSummary;  // AI 요약 결과
}
