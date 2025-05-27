package com.deepread.service;

import com.deepread.dto.response.DiagnosisQuestionResponseDto;
import com.deepread.entity.DiagnosisQuestion;
import com.deepread.repository.DiagnosisQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiagnosisQuestionService {

    private final DiagnosisQuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    public List<DiagnosisQuestionResponseDto> getMixedQuestions() {
        List<DiagnosisQuestionResponseDto> result = new ArrayList<>();
        result.addAll(randomFromType("A", 1));
        result.addAll(randomFromType("B", 2));
        result.addAll(randomFromType("C", 2));
        return result;
    }

    private List<DiagnosisQuestionResponseDto> randomFromType(String type, int count) {
        List<DiagnosisQuestion> all = questionRepository.findByType(type);
        Collections.shuffle(all);
        return all.stream()
                .limit(count)
                .map(q -> modelMapper.map(q, DiagnosisQuestionResponseDto.class))
                .collect(Collectors.toList());
    }
}
