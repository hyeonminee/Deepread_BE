package com.deepread.repository;

import com.deepread.entity.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {
    List<UserCalendar> findByUserId(Long userId);
}
