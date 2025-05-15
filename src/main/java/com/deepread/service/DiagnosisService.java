package com.deepread.service;

import com.deepread.dto.request.DiagnosisResultRequestDto;
import com.deepread.dto.response.DiagnosisResultResponseDto;
import com.deepread.entity.DiagnosisResult;
import com.deepread.entity.User;
import com.deepread.exception.ResourceNotFoundException;
import com.deepread.repository.DiagnosisResultRepository;
import com.deepread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final DiagnosisResultRepository diagnosisResultRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public DiagnosisResultResponseDto submitDiagnosisResult(DiagnosisResultRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자 정보를 찾을 수 없습니다."));

        DiagnosisResult result = modelMapper.map(dto, DiagnosisResult.class);
        result.setUser(user); // 연결 수동 지정

        DiagnosisResult saved = diagnosisResultRepository.save(result);

        DiagnosisResultResponseDto responseDto = modelMapper.map(saved, DiagnosisResultResponseDto.class);
        responseDto.setUserId(user.getId());
        return responseDto;
    }
}
