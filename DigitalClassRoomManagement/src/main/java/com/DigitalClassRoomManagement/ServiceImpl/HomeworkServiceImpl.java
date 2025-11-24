package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.homework;
import com.DigitalClassRoomManagement.Repository.HomeworkRepository;
import com.DigitalClassRoomManagement.Service.HomeworkService;
import com.DigitalClassRoomManagement.commonUtil.ValidationClass;
import com.DigitalClassRoomManagement.Dto.homeworkDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    private static final Logger logger = LoggerFactory.getLogger(HomeworkServiceImpl.class);

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Override
    public homework createHomework(homework homework) {
        try {
            logger.info("Creating new homework: {}", homework);

            // Convert Entity → DTO for validation
            homeworkDto dto = new homeworkDto(
                    homework.getTitle(),
                    homework.getDescription(),
                    homework.getAssignedDate(),
                    homework.getDueDate()
            );

            // Static validation call
            ValidationClass.validateHomework(dto);

            return homeworkRepository.save(homework);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed while creating homework: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error while creating homework: {}", e.getMessage(), e);
            throw new RuntimeException("Error while creating homework: " + e.getMessage(), e);
        }
    }

    @Override
    public homework getHomeworkById(Long id) {
        try {
            logger.info("Fetching homework with id: {}", id);
            Optional<homework> optionalHomework = homeworkRepository.findById(id);
            if (optionalHomework.isEmpty()) {
                logger.warn("Homework not found with id: {}", id);
                throw new RuntimeException("Homework not found with id: " + id);
            }
            return optionalHomework.get();
        } catch (Exception e) {
            logger.error("Error while fetching homework with id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error while fetching homework with id: " + id + " - " + e.getMessage(), e);
        }
    }

    @Override
    public List<homework> getAllHomeworks() {
        try {
            logger.info("Fetching all homeworks");
            return homeworkRepository.findAll();
        } catch (Exception e) {
            logger.error("Error while fetching all homeworks: {}", e.getMessage(), e);
            throw new RuntimeException("Error while fetching all homeworks: " + e.getMessage(), e);
        }
    }

    @Override
    public homework updateHomework(Long id, homework homework) {
        try {
            logger.info("Updating homework with id: {}", id);
            homework existing = getHomeworkById(id);

            // Convert to DTO for validation
            homeworkDto dto = new homeworkDto(
                    homework.getTitle(),
                    homework.getDescription(),
                    homework.getAssignedDate(),
                    homework.getDueDate()
            );

            // Static validation call
            ValidationClass.validateHomework(dto);

            existing.setTitle(homework.getTitle());
            existing.setDescription(homework.getDescription());
            existing.setAssignedDate(homework.getAssignedDate());
            existing.setDueDate(homework.getDueDate());

            return homeworkRepository.save(existing);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed while updating homework id {}: {}", id, e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            logger.warn("Known exception while updating homework with id {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error while updating homework with id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error while updating homework with id: " + id + " - " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteHomework(Long id) {
        try {
            logger.info("Deleting homework with id: {}", id);
            if (!homeworkRepository.existsById(id)) {
                logger.warn("Cannot delete. Homework not found with id: {}", id);
                throw new RuntimeException("Cannot delete. Homework not found with id: " + id);
            }
            homeworkRepository.deleteById(id);
            logger.info("Deleted homework with id: {}", id);
        } catch (RuntimeException e) {
            logger.warn("Known exception while deleting homework with id {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error while deleting homework with id {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error while deleting homework with id: " + id + " - " + e.getMessage(), e);
        }
    }
}
