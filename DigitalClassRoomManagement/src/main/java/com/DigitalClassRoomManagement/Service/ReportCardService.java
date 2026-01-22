package com.DigitalClassRoomManagement.Service;

import com.DigitalClassRoomManagement.Entity.ExamSubmission;
import com.DigitalClassRoomManagement.Entity.ReportCard;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Repository.ExamSubmissionRepository;
import com.DigitalClassRoomManagement.Repository.ReportCardRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportCardService {

    @Autowired
    private  ReportCardRepository reportCardRepo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private  ExamSubmissionRepository examSubmissionRepos;

    public ReportCardService(ReportCardRepository reportCardRepo, StudentRepository studentRepo) {
        this.reportCardRepo = reportCardRepo;
        this.studentRepo = studentRepo;
    }

    // CREATE
    public ReportCard createReportCard(ReportCard reportCard) {
        return reportCardRepo.save(reportCard);
    }
    public ReportCard createReportCard(ReportCard reportCard, Long submissionId) {
        ExamSubmission submission = examSubmissionRepos.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        reportCard.setSubmission(submission);
        return reportCardRepo.save(reportCard);
    }
    

    // GET ALL
    public List<ReportCard> getAllReportCards() {
        return reportCardRepo.findAll();
    }

    // GET BY ID
    public ReportCard getReportCardById(Long id) {
        return reportCardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ReportCard not found"));
    }

    // GET BY STUDENT
    public List<ReportCard> getReportCardsByStudent(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return reportCardRepo.findByStudent(student);
    }

    // UPDATE
    public ReportCard updateReportCard(Long id, ReportCard updated) {
        ReportCard old = getReportCardById(id);

        old.setSubject(updated.getSubject());
        old.setResult(updated.getResult());
        old.setSubmission(updated.getSubmission());
        old.setTerm(updated.getTerm());
        old.setTotalMarks(updated.getTotalMarks());
        old.setObtainedMarks(updated.getObtainedMarks());
        old.setPercentage(updated.getPercentage());
        old.setGrade(updated.getGrade());
        old.setRemarks(updated.getRemarks());

        return reportCardRepo.save(old);
    }

    // DELETE
    public void deleteReportCard(Long id) {
        reportCardRepo.deleteById(id);
    }
}
