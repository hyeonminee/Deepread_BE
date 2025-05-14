package com.deepread.service;

import com.deepread.entity.Summary;
import com.deepread.entity.SummaryFeedback;
import com.deepread.repository.SummaryFeedbackRepository;
import com.deepread.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final SummaryFeedbackRepository summaryFeedbackRepository;

    /**
     * 요약 저장 + AI 피드백 생성 및 저장까지 수행
     */
    public Summary submitSummary(Summary summary) {
        // 1. 요약 저장
        Summary saved = summaryRepository.save(summary);

        // 2. AI 피드백 생성
        SummaryFeedback feedback = generateFeedback(saved);

        // 3. 피드백 저장
        summaryFeedbackRepository.save(feedback);

        return saved;
    }

    /**
     * 요약 기반 AI 피드백 생성 로직 (임시로 정적 텍스트 사용)
     */
    private SummaryFeedback generateFeedback(Summary summary) {
        // TODO: 실제 GPT 연동 또는 평가 알고리즘 삽입
        SummaryFeedback feedback = new SummaryFeedback();
        feedback.setSummary(summary);
        feedback.setScore(92.5f);
        feedback.setFeedbackText("핵심이 잘 드러난 요약입니다. 불필요한 반복이 없고 구조가 명확합니다.");
        return feedback;
    }

    public Optional<Summary> getSummaryById(Long id) {
        return summaryRepository.findById(id);
    }

    public Optional<SummaryFeedback> getFeedbackBySummaryId(Long summaryId) {
        return summaryRepository.findById(summaryId)
                .flatMap(summaryFeedbackRepository::findBySummary);
    }
}


//package com.deepread.service;
//
//import com.deepread.entity.Summary;
//import com.deepread.entity.SummaryFeedback;
//import com.deepread.repository.SummaryFeedbackRepository;
//import com.deepread.repository.SummaryRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class SummaryService {
//
//    private final SummaryRepository summaryRepository;
//    private final SummaryFeedbackRepository summaryFeedbackRepository;
//
//    // 사용자의 요약 저장
//    public Summary submitSummary(Summary summary) {
//        return summaryRepository.save(summary);
//    }
//
//    // 요약에 대한 AI 피드백 저장
//    public SummaryFeedback getFeedback(SummaryFeedback feedback) {
//        return summaryFeedbackRepository.save(feedback);
//    }
//
//    // 요약 및 피드백 조회 (선택 기능)
//    public Optional<Summary> getSummaryById(Long id) {
//        return summaryRepository.findById(id);
//    }
//
//    // summaryId로 Summary를 조회한 뒤, 해당 요약에 대한 피드백을 반환
//    public Optional<SummaryFeedback> getFeedbackBySummaryId(Long summaryId) {
//        return summaryRepository.findById(summaryId)
//                .flatMap(summaryFeedbackRepository::findBySummary);
//    }
//
//}
