package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.SubjectDto;
import com.DigitalClassRoomManagement.Entity.Subject;
import com.DigitalClassRoomManagement.Exception.SubjectNotFoundException;
import com.DigitalClassRoomManagement.Repository.SubjectRepository;
import com.DigitalClassRoomManagement.Service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    private SubjectRepository srepo;
    @Override
    public String addSubject( SubjectDto sdto){
        log.info("Adding new subject with code: {}"+ sdto.getSubjectCode());
        Subject subject = new Subject();
        subject.setSubjectCode(sdto.getSubjectCode());
        subject.setSubjectName(sdto.getSubjectName());
        subject.setDescription(sdto.getDescription());
        subject.setMaxMarks(sdto.getMaxMarks());
        subject.setIsActive(sdto.getIsActive());
        subject.setCreatedAt(sdto.getCreatedAt());
        subject.setTeacher(sdto.getTeacher());
        subject.setSchoolClass(sdto.getSchoolClass());
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
                    .orElseThrow(()->{
                        log.error("No subject present  with id:"+subjectId);
                        return new SubjectNotFoundException("NO subject fuound with id :"+subjectId);
                    });
            existing.setSubjectCode(sdto.getSubjectCode());
            existing.setSubjectName(sdto.getSubjectName());
            existing.setDescription(sdto.getDescription());
            existing.setMaxMarks(sdto.getMaxMarks());
            existing.setIsActive(sdto.getIsActive());
            existing.setCreatedAt(sdto.getCreatedAt());
            existing.setTeacher(sdto.getTeacher());
            existing.setSchoolClass(sdto.getSchoolClass());
            existing.setUpdatedAt(sdto.getUpdatedAt());
            srepo.save(existing);
            log.info("Subject information is updated with id:{}", subjectId);
            return "Updation Successfull";
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


}