package com.DigitalClassRoomManagement.ServiceImpl;
import com.DigitalClassRoomManagement.Dto.ExamQuestionDto;
import com.DigitalClassRoomManagement.Entity.Exam;
import com.DigitalClassRoomManagement.Entity.ExamQuestion;
import com.DigitalClassRoomManagement.Repository.ExamQuestionRepository;
import com.DigitalClassRoomManagement.Repository.ExamRepository;
import com.DigitalClassRoomManagement.Service.ExamQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Autowired
    private ExamQuestionRepository questionRequestRepository;

    @Autowired
    private ExamRepository examRepository;

    @Override
    public void addQuestions(Long examId, List<ExamQuestion> questions, Long teacherId) {
        try {
            log.info("Adding {} questions for examId: {} by teacherId: {}", questions.size(), examId, teacherId);

            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new RuntimeException("Exam not found with id " + examId));

            for (ExamQuestion q : questions) {
                q.setExam(exam);
            }

            questionRequestRepository.saveAll(questions);
            log.info("Successfully saved {} questions for examId {}", questions.size(), examId);

        } catch (Exception e) {
            log.error("Error adding multiple questions for examId {}: {}", examId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ExamQuestionDto> getAllQuestions() {

        log.info("Fetching all questions");

        try {
            List<ExamQuestion> questions = questionRequestRepository.findAll();

            log.info("Total questions found: {}", questions.size());

            return questions.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error while fetching all questions", e);
            throw new RuntimeException("Failed to fetch questions", e);
        }
    }

    // Mapper
    private ExamQuestionDto mapToDto(ExamQuestion question) {
        return ExamQuestionDto.builder()
                .questionId(question.getId())
                .questionText(question.getQuestionText())
                .marks(question.getMarks())
                .examId(question.getExam() != null
                                ? question.getExam().getExamId()
                                : null
                )
                .build();
    }

}

