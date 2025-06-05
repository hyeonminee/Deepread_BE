package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Summary에서 참조되는 ID

    @Column(nullable = false)
    private String category;  // LAW, MEDICAL, NEWS 등

    @Column(nullable = false)
    private Long externalId;  // 원문이 저장된 외부 테이블의 ID

    @Column(nullable = false)
    private String title;  // 원문 주제 또는 제목

    @Column
    private String summarySourceType;  // CSV, CRAWL, MANUAL 등
}