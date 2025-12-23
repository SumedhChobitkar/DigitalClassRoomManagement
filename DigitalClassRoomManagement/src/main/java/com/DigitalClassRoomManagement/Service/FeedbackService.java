package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.FeedbackDto;

import java.util.List;

public interface FeedbackService {

    FeedbackDto createFeedback(FeedbackDto feedbackDto);

    FeedbackDto getFeedbackById(Long id);

    List<FeedbackDto> findFeedbackByStudentIdWithName(Long studentId, String studentName);

    List<FeedbackDto> findFeedbackByParentIdWithName(Long parentId, String parentName);

    List<FeedbackDto> getAllFeedbacks();

    FeedbackDto markReviewed(Long id);

    void deleteFeedback(Long feedbackId);


}
