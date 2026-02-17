package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Dto.ReportCardDto;
import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Entity.ReportCard;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Repository.ExamSubmissionRepository;
import com.DigitalClassRoomManagement.Repository.ReportCardRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Repository.SubjectRepository;
import com.DigitalClassRoomManagement.Repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportCardService {

    @Autowired
    private  ReportCardRepository reportCardRepo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private  ExamSubmissionRepository examSubmissionRepos;
    @Autowired
    private SubjectRepository subjectRepo;
    @Autowired
    private ResultRepository resultRepo;

    public ReportCardService(ReportCardRepository reportCardRepo, StudentRepository studentRepo) {
        this.reportCardRepo = reportCardRepo;
        this.studentRepo = studentRepo;
    }

    // CREATE
    @Transactional
    public ReportCardDto createReportCard(ReportCard reportCard) {
        ReportCard saved = reportCardRepo.save(reportCard);
        return toDto(saved);
    }

    @Transactional
    public ReportCardDto createReportCard(ReportCard reportCard, Long submissionId) {
        ExamSubmission submission = examSubmissionRepos.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found with id: " + submissionId));

        reportCard.setSubmission(submission);
        ReportCard saved = reportCardRepo.save(reportCard);
        return toDto(saved);
    }
    

    // GET ALL
    @Transactional(readOnly = true)
    public List<ReportCardDto> getAllReportCards() {
        return toDtoList(reportCardRepo.findAll());
    }

    // GET BY ID
    @Transactional(readOnly = true)
    public ReportCardDto getReportCardById(Long id) {
        ReportCard rc = reportCardRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReportCard not found with id: " + id));
        return toDto(rc);
    }

    // GET BY STUDENT
    @Transactional(readOnly = true)
    public List<ReportCardDto> getReportCardsByStudent(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        return toDtoList(reportCardRepo.findByStudent(student));
    }

    // UPDATE
    @Transactional
    public ReportCardDto updateReportCard(Long id, ReportCard updated) {
        ReportCard old = reportCardRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ReportCard not found with id: " + id));

        // If incoming payload has nested objects with IDs, resolve managed entities; otherwise keep existing
        if (updated.getSubject() != null) {
            Long sid = updated.getSubject().getSubjectId();
            if (sid != null) {
                subjectRepo.findById(sid).ifPresentOrElse(
                        old::setSubject,
                        () -> { throw new IllegalArgumentException("Subject not found with id: " + sid); }
                );
            } else {
                old.setSubject(updated.getSubject());
            }
        }

        if (updated.getResult() != null) {
            Long rid = updated.getResult().getResultId();
            if (rid != null) {
                resultRepo.findById(rid).ifPresentOrElse(
                        old::setResult,
                        () -> { throw new IllegalArgumentException("Result not found with id: " + rid); }
                );
            } else {
                old.setResult(updated.getResult());
            }
        }

        if (updated.getSubmission() != null) {
            Long subId = updated.getSubmission().getSubmissionId();
            if (subId != null) {
                examSubmissionRepos.findById(subId).ifPresentOrElse(
                        old::setSubmission,
                        () -> { throw new IllegalArgumentException("Submission not found with id: " + subId); }
                );
            } else {
                old.setSubmission(updated.getSubmission());
            }
        }

        // scalar updates
        old.setTerm(updated.getTerm());
        old.setTotalMarks(updated.getTotalMarks());
        old.setObtainedMarks(updated.getObtainedMarks());
        old.setPercentage(updated.getPercentage());
        old.setGrade(updated.getGrade());
        old.setRemarks(updated.getRemarks());

        ReportCard saved = reportCardRepo.save(old);
        return toDto(saved);
    }

    // DELETE
    @Transactional
    public void deleteReportCard(Long id) {
        reportCardRepo.deleteById(id);
    }

    // Conversion helper - keep lightweight DTO to avoid proxy serialization
    public ReportCardDto toDto(ReportCard rc) {
        if (rc == null) return null;
        ReportCardDto dto = new ReportCardDto();
        dto.setReportCardId(rc.getReportCardId());
        dto.setStudentId(rc.getStudent() != null ? rc.getStudent().getStudentId() : null);
        dto.setSubjectId(rc.getSubject() != null ? rc.getSubject().getSubjectId() : null);
        dto.setResultId(rc.getResult() != null ? rc.getResult().getResultId() : null);
        dto.setSubmissionId(rc.getSubmission() != null ? rc.getSubmission().getSubmissionId() : null);
        dto.setTerm(rc.getTerm());
        dto.setTotalMarks(rc.getTotalMarks());
        dto.setObtainedMarks(rc.getObtainedMarks());
        dto.setPercentage(rc.getPercentage());
        dto.setGrade(rc.getGrade());
        dto.setRemarks(rc.getRemarks());
        dto.setGeneratedAt(rc.getGeneratedAt());
        return dto;
    }

    public List<ReportCardDto> toDtoList(List<ReportCard> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
}
