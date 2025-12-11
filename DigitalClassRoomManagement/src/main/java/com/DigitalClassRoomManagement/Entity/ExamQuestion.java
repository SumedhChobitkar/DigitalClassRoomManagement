package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
  //  @Column(name = "exam_id", nullable = false)   // FIXED
    private Exam exam;

    private String questionText;

    @Enumerated(EnumType.ORDINAL)
    private QuestionType questionType;

    @ElementCollection
    @CollectionTable(
            name = "exam_question_options",
            joinColumns = @JoinColumn(name = "exam_question_id")
    )
    @Column(name = "option_value")
    private List<String> options;

    private String correctAnswer;

    private BigDecimal marks;
}
