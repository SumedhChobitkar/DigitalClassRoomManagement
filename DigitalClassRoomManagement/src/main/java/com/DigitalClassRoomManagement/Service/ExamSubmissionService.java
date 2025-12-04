package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Entity.ExamSubmission;

import java.util.List;

public interface ExamSubmissionService {

    ExamSubmission submitExam(Long examId, Long studentId, String answers);
    List<ExamSubmission> getSubmissionsByExam(Long examId);

}
