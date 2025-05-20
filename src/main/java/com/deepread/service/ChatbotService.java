package com.deepread.service;

import com.deepread.dto.request.ChatbotLogRequestDto;
import com.deepread.dto.response.ChatbotLogResponseDto;
import com.deepread.entity.ChatbotLog;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.ChatbotLogRepository;
import com.deepread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ChatbotLogRepository chatbotLogRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 사용자 질문/응답 로그 저장
     */
    public ChatbotLogResponseDto saveChatbotLog(ChatbotLogRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        ChatbotLog log = new ChatbotLog();
        log.setUser(user);
        log.setQuestion(dto.getQuestion());
        log.setResponse(dto.getResponse());

        ChatbotLog saved = chatbotLogRepository.save(log);
        ChatbotLogResponseDto responseDto = modelMapper.map(saved, ChatbotLogResponseDto.class);
        responseDto.setUserId(saved.getUser().getId());

        return responseDto;
    }
}
