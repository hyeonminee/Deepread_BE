package com.deepread.service;

import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.entity.QuizResult;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.QuizResultRepository;
import com.deepread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizResultRepository quizResultRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public QuizResultResponseDto saveQuizResult(QuizResultRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        QuizResult entity = new QuizResult();
        entity.setUser(user);
        entity.setCorrectCount(dto.getCorrectCount());
        entity.setTotalQuestions(dto.getTotalQuestions());
        entity.setAccuracy(dto.getAccuracy());

        QuizResult saved = quizResultRepository.save(entity);

        QuizResultResponseDto responseDto = modelMapper.map(saved, QuizResultResponseDto.class);
        responseDto.setUserId(saved.getUser().getId());
        return responseDto;
    }

    public List<QuizResult> getQuizResultsByUserId(Long userId) {
        return quizResultRepository.findByUserId(userId);
    }
}
