package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // Entity 명시 어노테이션
@Table(name = "chatbot_logs") // 테이블명 설정
@Getter
@Setter
@NoArgsConstructor // 파라미터가 없는 디폴트 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
public class ChatbotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 전략(데이터베이스에 위임)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // not nullable 설정
    private User user;

    @Lob
    private String question;

    @Lob
    private String response;

    private LocalDateTime createdAt = LocalDateTime.now();
}
