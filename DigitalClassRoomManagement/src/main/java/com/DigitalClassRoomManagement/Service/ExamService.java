package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.ExamDto;
import java.util.List;

public interface ExamService {
    ExamDto createExam(ExamDto examDto);

    ExamDto updateExam(Long examId, ExamDto examDto);

    ExamDto getExamById(Long examId);

    List<ExamDto> getAllExams();

    void deleteExam(Long examId);

    List<ExamDto> getExamsByTeacherId(Long teacherId);

    List<ExamDto> getExams(  Long examId, Long teacherId);

}