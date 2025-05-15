package com.deepread.config;

import com.deepread.dto.response.DiagnosisResultResponseDto;
import com.deepread.entity.DiagnosisResult;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 혼동되는 userLevel 명시적으로 매핑 지정
        modelMapper.addMappings(new PropertyMap<DiagnosisResult, DiagnosisResultResponseDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getId()); // 연관관계 매핑
                map().setUserLevel(source.getUserLevel()); // 이 경로로만 설정
            }
        });

        return modelMapper;
    }
}
