package com.deepread.service;

import com.deepread.dto.request.QuizResultRequestDto;
import com.deepread.dto.response.QuizAnswerValidationResponseDto;
import com.deepread.dto.response.QuizQuestionResponseDto;
import com.deepread.dto.response.QuizResultResponseDto;
import com.deepread.entity.QuizLevel;
import com.deepread.entity.QuizQuestion;
import com.deepread.entity.QuizResult;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.QuizQuestionRepository;
import com.deepread.repository.QuizResultRepository;
import com.deepread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizResultRepository quizResultRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // CSV 업로드
    public void uploadQuestionsFromCsv(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirst = true;

            while ((line = reader.readLine()) != null) {
                if (isFirst) { isFirst = false; continue; }

                String[] tokens = line.split(",");

                if (tokens.length < 7) continue;

                QuizLevel level = QuizLevel.valueOf(tokens[6].replace("\"", "").trim());

                QuizQuestion question = QuizQuestion.builder()
                        .question(tokens[0].replace("\"", ""))
                        .option1(tokens[1].replace("\"", ""))
                        .option2(tokens[2].replace("\"", ""))
                        .option3(tokens[3].replace("\"", ""))
                        .option4(tokens[4].replace("\"", ""))
                        .answer(Integer.parseInt(tokens[5]))
                        .level(level)
                        .build();

                quizQuestionRepository.save(question);
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV 업로드 실패: " + e.getMessage());
        }
    }

    // 문제 정답 검증
    public QuizAnswerValidationResponseDto validateAnswer(Long questionId, int selectedOption) {
        QuizQuestion question = quizQuestionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 문제를 찾을 수 없습니다."));

        boolean isCorrect = question.getAnswer().equals(selectedOption);

        return QuizAnswerValidationResponseDto.builder()
                .isCorrect(isCorrect)
                .correctOption(question.getAnswer())
                .build();
    }

    // 레벨별 랜덤 5문제 조회
    public List<QuizQuestionResponseDto> getQuizQuestionsByLevel(QuizLevel level) {
        List<QuizQuestion> questions = quizQuestionRepository.findRandom5ByLevel(level.name());
        return questions.stream()
                .map(q -> modelMapper.map(q, QuizQuestionResponseDto.class))
                .collect(Collectors.toList());
    }

    // 사용자가 최종적으로 결과 저장
    public QuizResultResponseDto saveQuizResult(QuizResultRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        QuizResult result = QuizResult.builder()
                .user(user)
                .correctCount(dto.getCorrectCount())
                .totalQuestions(dto.getTotalQuestions())
                .accuracy(dto.getAccuracy())
                .build();

        QuizResult saved = quizResultRepository.save(result);
        return modelMapper.map(saved, QuizResultResponseDto.class);
    }


    // 단일 문제 조회
    public QuizQuestionResponseDto getQuestionById(Long id) {
        QuizQuestion question = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 문제가 존재하지 않습니다."));
        return modelMapper.map(question, QuizQuestionResponseDto.class);
    }

    // 전체 문제 페이징
    public Page<QuizQuestionResponseDto> getAllQuestions(Pageable pageable) {
        return quizQuestionRepository.findAll(pageable)
                .map(q -> modelMapper.map(q, QuizQuestionResponseDto.class));
    }
}
