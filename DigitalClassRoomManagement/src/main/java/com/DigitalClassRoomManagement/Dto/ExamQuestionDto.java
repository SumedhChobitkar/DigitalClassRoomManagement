package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Enum.QuestionType;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Setter
@Builder
public class ExamQuestionDto {


    private Long examId;
    private long questionId;
    private String questionText;
    private QuestionType questionType;
    private List<String> options; // for MCQ
    private String correctAnswer; // for MCQ
    private BigDecimal marks;


}
