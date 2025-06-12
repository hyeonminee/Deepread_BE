package com.deepread.controller;

import com.deepread.service.ContentInitializerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class DevController {

    private final ContentInitializerService contentInitializerService;

    @PostMapping("/initialize-contents")
    public String initializeContents() {
        contentInitializerService.initializeContents();
        return "Content 테이블 초기화 완료";
    }
}
