package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.ExamQuestionDto;
import com.DigitalClassRoomManagement.Entity.ExamQuestion;

import java.util.List;

public interface ExamQuestionService {

    void addQuestions(Long examId, List<ExamQuestion> questions, Long teacherId);

    List<ExamQuestionDto> getAllQuestions();

    List<ExamQuestionDto> getQuestionsByTeacherId(Long teacherId);


    //ExamQuestionDto addQuestion(ExamQuestionDto dto);
}
