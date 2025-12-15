package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Entity.ExamSubmission;

import java.util.List;
import java.util.Map;

public interface ExamSubmissionService {

    void submitExam(Long studentId, Long examId, Map<Long, String> answers);
   // ExamSubmission submitExam(Long examId, Long studentId, String answers);
    List<ExamSubmission> getSubmissionsByExam(Long examId);

}
