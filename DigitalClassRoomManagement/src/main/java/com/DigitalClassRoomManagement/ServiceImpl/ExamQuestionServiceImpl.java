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

@Service
@Slf4j
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Autowired
    private ExamQuestionRepository questionRequestRepository;

    @Autowired
    private ExamRepository examRepository;


    public ExamQuestionDto addQuestion(ExamQuestionDto dto) {

        try {
            log.info("Adding question for examId: {}", dto.getExamId());

            //<---------------- MCQ VALIDATION  ------------------->

            if (dto.getQuestionType() != null && dto.getQuestionType().name().equals("MCQ")) {

                log.debug("Validating MCQ options for examId: {}", dto.getExamId());

                if (dto.getOptions() == null || dto.getOptions().isEmpty()) {
                    log.error("MCQ question missing options for examId: {}", dto.getExamId());
                    throw new IllegalArgumentException("MCQ must have options");
                }

                if (!dto.getOptions().contains(dto.getCorrectAnswer())) {
                    log.error("Correct answer not in options for examId: {}", dto.getExamId());
                    throw new IllegalArgumentException("Correct answer must be one of the options");
                }
            }

           Exam exam= examRepository.findById(dto.getExamId()).orElseThrow(()-> new RuntimeException("Exam id not found"));

            // <------------------ MAP DTO ENTITY-------------------------->
            ExamQuestion question = ExamQuestion.builder()
                    .exam(exam)
                    .questionText(dto.getQuestionText())
                    .questionType(dto.getQuestionType())
                    .options(dto.getOptions())
                    .correctAnswer(dto.getCorrectAnswer())
                    .marks(dto.getMarks())
                    .build();

            log.debug("Saving question to database for examId: {}", dto.getExamId());
            ExamQuestion savedQuestion = questionRequestRepository.save(question);

            // <---------------- MAP ENTITY DTO ---------------------------->

            return ExamQuestionDto.builder()
                    .examId(savedQuestion.getExam().getExamId())
                    .questionText(savedQuestion.getQuestionText())
                    .questionType(savedQuestion.getQuestionType())
                    .options(savedQuestion.getOptions())
                    .correctAnswer(savedQuestion.getCorrectAnswer())
                    .marks(savedQuestion.getMarks())
                    .build();

        } catch (Exception e) {
            log.error("Error occurred while adding question for examId {}: {}",
                    dto.getExamId(), e.getMessage(), e);
            throw e;
        }
    }

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

}

