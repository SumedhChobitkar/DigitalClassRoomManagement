package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.SubjectDto;
import com.DigitalClassRoomManagement.Entity.SchoolClass;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Entity.Subject;
import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Exception.SubjectNotFoundException;
import com.DigitalClassRoomManagement.Repository.SchoolClassRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Repository.SubjectRepository;
import com.DigitalClassRoomManagement.Repository.TeacherRepository;
import com.DigitalClassRoomManagement.Service.SubjectService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    private SubjectRepository srepo;

    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private SchoolClassRepository schoolClassRepo;
    @Autowired
    private TeacherRepository teacherRepo;
    @Override
    public String addSubject( SubjectDto sdto){
        log.info("Adding new subject with code: {}"+ sdto.getSubjectCode());

        SchoolClass schoolClass = schoolClassRepo
                .findById(sdto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));
        Teacher teacher = teacherRepo.findById(sdto.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));


        Subject subject = new Subject();
        subject.setSubjectCode(sdto.getSubjectCode());
        subject.setSubjectName(sdto.getSubjectName());
        subject.setDescription(sdto.getDescription());
        subject.setMaxMarks(sdto.getMaxMarks());
        subject.setIsActive(sdto.getIsActive());
        subject.setCreatedAt(sdto.getCreatedAt());
        subject.setTeacher(teacher);
        subject.setSchoolClass(schoolClass);
        subject.setUpdatedAt(sdto.getUpdatedAt());
        Subject savedSubject=srepo.save(subject);
        return "New subject added successfully with id:"+savedSubject.getSubjectId();
    }

    @Override
    public List<Subject> getAllSubject(){
        log.info("Fetching list of all subjects");
        List<Subject> subject = srepo.findAll();
        log.info("Total number of subjects found:{}", subject.size());
        return subject;
    }

    @Override
    public Subject getSubjectById( Long subjectId){
        log.info("Fetching information about subject with id:{}", subjectId);
        return  srepo.findById(subjectId)
                .orElseThrow(() ->{
                    log.error("No subject present  with id:"+subjectId);
                    return new SubjectNotFoundException("NO subject fuound with id :"+subjectId);
                } );

    }

    @Override
    public String updateSubject(SubjectDto sdto, Long subjectId){
        try{
            Subject existing = srepo.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));


            existing.setSubjectCode(sdto.getSubjectCode());
            existing.setSubjectName(sdto.getSubjectName());
            existing.setDescription(sdto.getDescription());
            existing.setMaxMarks(sdto.getMaxMarks());
            existing.setIsActive(sdto.getIsActive());


            if (sdto.getId() != null) {
                Teacher teacher = teacherRepo.findById(sdto.getId())
                        .orElseThrow(() -> new RuntimeException("Teacher not found"));
                existing.setTeacher(teacher);
            }


            if (sdto.getClassId() != null) {
                SchoolClass schoolClass = schoolClassRepo.findById(sdto.getClassId())
                        .orElseThrow(() -> new RuntimeException("Class not found"));
                existing.setSchoolClass(schoolClass);
            }

            Subject saved = srepo.save(existing);

            return "updated successfully;";
        }catch(SubjectNotFoundException se){
            log.warn("Attempted to update subjet with non existing id:{}", subjectId);
            throw  se;
        }catch(Exception e){
            log.warn("Error occured while upating subject with id:{}", subjectId);
            throw new RuntimeException("Updation failed"+e.getMessage());
        }
    }

    @Override
    public String deleteSubject(Long subjectId) {
        log.info("Attempting to delete subject with is:{}", subjectId);
        try{
            Subject existing = srepo.findById(subjectId)
                    .orElseThrow(()->{
                        log.error("No subject present  with id:"+subjectId);
                        return new SubjectNotFoundException("NO subject fuound with id :"+subjectId);
                    });
            srepo.delete(existing);
            log.info("Teacher deleted successfully with ID: {}", subjectId);
            return "Subject  information deleted successfully with ID: " + subjectId;
        } catch (SubjectNotFoundException tx) {
            log.warn("Attempted to delete non-existing subject with ID: {}", subjectId);
            throw tx;
        } catch (Exception e) {
            log.error("Error occurred while deleting teacher with ID: {} - {}", subjectId, e.getMessage(), e);
            throw new RuntimeException("Deletion failed: " + e.getMessage());
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<SubjectDto> getSubjectsForStudent(Long studentRegId) {

        log.info("Fetching subjects for studentRegId={}", studentRegId);

        Student student = studentRepo.findByStudentRegId(studentRegId)
                .orElseThrow(() -> {
                    log.error("Student not found with regId={}", studentRegId);
                    return new RuntimeException("Student not found");
                });

        SchoolClass schoolClass = student.getSchoolClass();

        if (schoolClass == null) {
            log.warn("Student {} has no class assigned", studentRegId);
            return List.of();
        }

        log.info(
                "Student {} belongs to classId={}, className={}",
                studentRegId,
                schoolClass.getClassId(),
                schoolClass.getClassName()
        );

        List<Subject> subjects =
                srepo.findBySchoolClassAndIsActiveTrue(schoolClass);

        log.info("Total subjects found={}", subjects.size());

        return subjects.stream()
                .map(subject -> SubjectDto.builder()
                        .subjectId(subject.getSubjectId())
                        .subjectName(subject.getSubjectName())
                        .subjectCode(subject.getSubjectCode())
                        .classId(subject.getSchoolClass().getClassId())
                        .className(subject.getSchoolClass().getClassName())
                        .id(subject.getTeacher().getId())
                        .maxMarks(subject.getMaxMarks())
                        .isActive(subject.getIsActive())
                        .build()
                )
                .toList();
    }

    @Override
    @Transactional
    public SubjectDto assignSubjectToClass(Long subjectId, Long classId) {

        Subject subject = srepo.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        SchoolClass schoolClass = schoolClassRepo.findById(classId)
                .orElseThrow(() -> new RuntimeException("School class not found"));


        subject.setSchoolClass(schoolClass);

        Subject updated = srepo.save(subject);

        return SubjectDto.builder()
                .subjectId(updated.getSubjectId())
                .subjectName(updated.getSubjectName())
                .subjectCode(updated.getSubjectCode())
                .classId(schoolClass.getClassId())
                .className(schoolClass.getClassName())
                .build();
    }



}