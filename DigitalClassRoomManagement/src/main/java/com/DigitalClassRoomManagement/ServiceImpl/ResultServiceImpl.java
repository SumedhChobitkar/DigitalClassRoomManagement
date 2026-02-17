package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.ResultDto;
import com.DigitalClassRoomManagement.Entity.Exam;
import com.DigitalClassRoomManagement.Entity.Result;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Repository.ExamRepository;
import com.DigitalClassRoomManagement.Repository.ResultRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Service.ResultService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamRepository examRepository;


    @Override
    public ResultDto createResult(ResultDto dto) {
        try {
            log.info("Creating result for studentId={} examId={}", dto.getStudentId(), dto.getExamId());

            Student student = getStudent(dto.getStudentId());
            Exam exam = getExam(dto.getExamId());

            Result result = Result.builder()
                    .student(student)
                    .name(dto.getName())
                    .exam(exam)
                    .obtainedMarks(dto.getObtainedMarks())
                    .percentage(dto.getPercentage())
                    .grade(dto.getGrade())
                    .status(dto.getStatus())
                    .publishedAt(dto.getPublishedAt())
                    .build();

            result = resultRepository.save(result);
            log.info("Result created successfully with ID={}", result.getResultId());

            return convertToDto(result);
        } catch (Exception e) {
            log.error("Error creating result for studentId={} examId={}", dto.getStudentId(), dto.getExamId(), e);
            throw new RuntimeException("Failed to create result", e);
        }
    }

    @Override
    public ResultDto getResultById(Long id) {
        try {
            log.info("Fetching result by ID={}", id);
            Result result = getResult(id);
            return convertToDto(result);
        } catch (Exception e) {
            log.error("Error fetching result with ID={}", id, e);
            throw new RuntimeException("Failed to fetch result", e);
        }
    }

    @Override
    public List<ResultDto> getAllResults() {
        try {
            log.info("Fetching all results");
            return resultRepository.findAll()
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all results", e);
            throw new RuntimeException("Failed to fetch all results", e);
        }
    }

    @Override
    public ResultDto updateResult(Long id, ResultDto dto) {
        try {
            log.info("Updating result ID={}", id);

            Result existing = getResult(id);
            Student student = getStudent(dto.getStudentId());
            Exam exam = getExam(dto.getExamId());
            existing.setName(dto.getName());
            existing.setStudent(student);
            existing.setExam(exam);
            existing.setObtainedMarks(dto.getObtainedMarks());
            existing.setPercentage(dto.getPercentage());
            existing.setGrade(dto.getGrade());
            existing.setStatus(dto.getStatus());
            existing.setPublishedAt(dto.getPublishedAt());

            Result updated = resultRepository.save(existing);
            log.info("Result ID={} updated successfully", id);

            return convertToDto(updated);
        } catch (Exception e) {
            log.error("Error updating result ID={}", id, e);
            throw new RuntimeException("Failed to update result", e);
        }
    }

    @Override
    public void deleteResult(Long id) {
        try {
            log.info("Deleting result ID={}", id);

            if (!resultRepository.existsById(id)) {
                throw new RuntimeException("Result not found");
            }

            resultRepository.deleteById(id);
            log.info("Result ID={} deleted successfully", id);
        } catch (Exception e) {
            log.error("Error deleting result ID={}", id, e);
            throw new RuntimeException("Failed to delete result", e);
        }
    }

    // ---------- Helper Methods with try-catch ----------

    private Result getResult(Long id) {
        try {
            return resultRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Result not found with ID=" + id));
        } catch (Exception e) {
            log.error("Error fetching result by ID={}", id, e);
            throw e;
        }
    }

    private Student getStudent(Long id) {
        try {
            return studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with ID=" + id));
        } catch (Exception e) {
            log.error("Error fetching student by ID={}", id, e);
            throw e;
        }
    }

    private Exam getExam(Long id) {
        try {
            return examRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Exam not found with ID=" + id));
        } catch (Exception e) {
            log.error("Error fetching exam by ID={}", id, e);
            throw e;
        }
    }

    private ResultDto convertToDto(Result result) {
        try {
            return ResultDto.builder()
                    .resultId(result.getResultId())
                    .studentId(result.getStudent().getStudentRegId())
                    .examId(result.getExam().getExamId())
                    .obtainedMarks(result.getObtainedMarks())
                    .percentage(result.getPercentage())
                    .grade(result.getGrade())
                    .status(result.getStatus())
                    .publishedAt(result.getPublishedAt())
                    .name(result.getName())
                    .build();
        } catch (Exception e) {
            log.error("Error converting Result entity to DTO for ID={}", result.getResultId(), e);
            throw new RuntimeException("Failed to convert Result to DTO", e);
        }
    }

    @Override
    public List<ResultDto> getResultsByStudent(Long studentId) {
        try {
            return resultRepository.findByStudentStudentRegId(studentId)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching results by student ID={}", studentId, e);
            throw new RuntimeException("Failed to fetch results by student", e);
        }
    }

    @Override
    public List<ResultDto> getResultsByExam(Long examId) {
        try {
            return resultRepository.findByExamExamId(examId)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching results by exam ID={}", examId, e);
            throw new RuntimeException("Failed to fetch results by exam", e);
        }
    }

    @Override
    public List<ResultDto> getResultsByStudentAndExam(Long studentId, Long examId) {
        try {
            return resultRepository.findByStudentStudentRegIdAndExamExamId(studentId, examId)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching results by student ID={} and exam ID={}", studentId, examId, e);
            throw new RuntimeException("Failed to fetch results by student and exam", e);
        }
    }

    @Override
    public List<ResultDto> getResultsByTopMarks() {
        // Fetch all results ordered by obtained marks descending
        List<Result> results = resultRepository.findAllByOrderByObtainedMarksDesc();

        // Map each Result entity to ResultDto
        return results.stream()
                .map(result -> ResultDto.builder()
                        .resultId(result.getResultId())
                        .studentId(result.getStudent().getStudentRegId())
                        .examId(result.getExam().getExamId())
                        .obtainedMarks(result.getObtainedMarks())
                        .percentage(result.getPercentage())
                        .grade(result.getGrade())
                        .name(result.getName())
                        .status(result.getStatus())
                        .publishedAt(result.getPublishedAt())
                        .build())
                .collect(Collectors.toList());
    }

}