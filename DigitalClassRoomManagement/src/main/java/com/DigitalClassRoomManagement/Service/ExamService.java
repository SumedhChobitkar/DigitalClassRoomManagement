package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface ExamService {
    ExamDto createExam(ExamDto examDto);

    ExamDto updateExam(Long examId, ExamDto examDto);

    ExamDto getExamById(Long examId);

    ExamDto getExamByQuestionId(Long questionId);

    List<ExamDto> getAllExamList();

    void deleteExam(Long examId);

    List<ExamDto> getExamsByTeacherId(Long teacherId);

    List<ExamDto> getExams(  Long examId, Long teacherId);

    ExamDto adminCreateExam(ExamDto examDto);

    List<ExamDto> adminGetAllExams();

    ExamDto getExamScheduleByExamId(Long examId);


    // Daily at 9 AM
    @Scheduled(cron = "0 * * * * ?")
    void sendUpcomingExamReminders();
}