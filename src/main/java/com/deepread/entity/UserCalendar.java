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

    private LocalDate user_date;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
