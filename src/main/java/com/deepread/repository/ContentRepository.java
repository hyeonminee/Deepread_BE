package com.deepread.repository;

import com.deepread.entity.Content;
import com.deepread.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByLevel(User.Level level);

}
