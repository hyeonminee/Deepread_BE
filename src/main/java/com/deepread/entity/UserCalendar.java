package com.deepread.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_calendar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate userDate;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @PrePersist
    public void prePersist() {
        if (userDate == null) {
            userDate = LocalDate.now();
        }
    }
}