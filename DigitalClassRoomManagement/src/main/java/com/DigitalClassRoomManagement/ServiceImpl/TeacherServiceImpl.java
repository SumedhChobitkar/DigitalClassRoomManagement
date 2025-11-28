package com.DigitalClassRoomManagement.ServiceImpl;
import com.DigitalClassRoomManagement.Dto.AssignTeacherRequestDto;
import com.DigitalClassRoomManagement.Dto.SchoolClassResponseDto;
import com.DigitalClassRoomManagement.Dto.TeacherDto;
import com.DigitalClassRoomManagement.Dto.TeacherResponseDto;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Enum.Role;
import com.DigitalClassRoomManagement.Enum.Status;
import com.DigitalClassRoomManagement.Exception.InvalidImageFormatException;
import com.DigitalClassRoomManagement.Exception.SchoolClassNotFoundException;

import com.DigitalClassRoomManagement.Enum.TeacherStatus;

import com.DigitalClassRoomManagement.Exception.SectionNotFoundException;
import com.DigitalClassRoomManagement.Exception.TeacherNotFoundException;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.EmailSenderService;
import com.DigitalClassRoomManagement.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class TeacherServiceImpl implements TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    private TeacherRepository repo;
    @Autowired
    private UserRepository urepo;
    @Autowired
    private  SchoolClassRepository classRepo;

    @Autowired
    private SectionRepository sectionRepo;

    @Autowired
    private AssignTeacherRequestRepository assignRepo;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JavaMailSender mailSender;

    // CREATE
    @Override

    @Transactional

    public String addTeacher(TeacherDto dto) {

        try {
            if (repo.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("A teacher with this email already exists: " + dto.getEmail());
            }
            if (repo.existsByPhone(dto.getPhone())) {
                throw new IllegalArgumentException("A teacher with this phone number already exists: " + dto.getPhone());
            }
             Teacher teacher = new Teacher();
        teacher.setEmail(dto.getEmail());
        teacher.setGender(dto.getGender());
        teacher.setPhone(dto.getPhone());
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setQualification(dto.getQualification());
        teacher.setDateOfBirth(String.valueOf(LocalDate.parse(String.valueOf(dto.getDateOfBirth()))));
        teacher.setExperienceYears(dto.getExperienceYears());
            User user = urepo.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + dto.getEmail()));
        teacher.setUser(user);
        teacher.setStatus(TeacherStatus.PENDING);

            String toEmail =dto.getAdminMailId() ;
            String subject = "No Reply";

            String body = "Dear " + "Principal"+ "," + "\n\nI hope this message finds you well. " +
                    "\nYou have new request from " + dto.getFirstName() + "." +"Please check your Dashboard."+
                    "\nIf you have any related queries, feel free to reach out to us." + "\n\n"
                    + "Best Regards," + "\n" + "HR Team." + "\n\n\nThis is an auto-generated mail.";

        Teacher savedTeacher = repo.save(teacher);
            emailSenderService.sendEmail(toEmail,subject,body);
        log.info("Teacher added successfully with ID: {}", savedTeacher.getId());
        return "Teacher registration submitted. Pending for approval. " + savedTeacher.getId();

        } catch (Exception e) {
            log.error("Error while adding teacher: {}", e.getMessage(), e);
            throw new RuntimeException("Add Teacher Failed: " + e.getMessage());
        }
    }



    // READ (Entity)
    @Override
    @Transactional
    public List<Teacher> getAllTeacher() {
        try {
            log.info("Fetching all teachers...");
            return repo.findAll();
        } catch (Exception e) {
            log.error("Error fetching teachers: {}", e.getMessage(), e);
            throw new RuntimeException("Get All Teachers Failed: " + e.getMessage());
        }
    }

    // READ (DTO) – FIX FOR LazyInitializationException
    @Override
    @Transactional
    public List<TeacherResponseDto> getAllTeacherDtos() {
        try {
            log.info("Fetching all teachers (DTO mode)");

            return repo.findAll()
                    .stream()
                    .map(t -> new TeacherResponseDto(
                            t.getId(),
                            t.getFirstName(),
                            t.getLastName(),
                            t.getEmail(),
                            t.getPhone(),
                            t.getGender(),
                            t.getQualification(),
                            t.getExperienceYears(),
                            t.getDateOfBirth(),
                            t.getAssignedClass() == null ? Collections.emptyList()
                                    : t.getAssignedClass()
                                    .stream()
                                    .map(SchoolClass::getClassId)
                                    .collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching teacher DTOs: {}", e.getMessage(), e);
            throw new RuntimeException("Get All Teachers DTO Failed: " + e.getMessage());
        }
    }

    // GET BY ID
    @Override
    @Transactional
    public Teacher getTeacherById(Long id) {
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new TeacherNotFoundException(id));
        } catch (Exception e) {
            log.error("Error getting teacher by ID {} -> {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // UPDATE
    @Override
    @Transactional
    public String updateTeacherInfo(Long id, TeacherDto dto) {
        try {
            Teacher existing = repo.findById(id)
                    .orElseThrow(() -> new TeacherNotFoundException(id));

            existing.setEmail(dto.getEmail());
            existing.setGender(dto.getGender());
            existing.setPhone(dto.getPhone());
            existing.setFirstName(dto.getFirstName());
            existing.setLastName(dto.getLastName());
            existing.setQualification(dto.getQualification());
            existing.setDateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth().toString() : null);
            existing.setExperienceYears(dto.getExperienceYears());

            repo.save(existing);
            return "Updation successful";

        } catch (Exception e) {
            log.error("Error updating teacher {} -> {}", id, e.getMessage(), e);
            throw new RuntimeException("Update Failed: " + e.getMessage());
        }
    }

    // DELETE
    @Override
    @Transactional
    public String deleteTeacherById(Long id) {
        try {
            Teacher existing = repo.findById(id)
                    .orElseThrow(() -> new TeacherNotFoundException(id));

            repo.delete(existing);

            return "Teacher deleted successfully with ID: " + id;

        } catch (Exception e) {
            log.error("Error deleting teacher {} -> {}", id, e.getMessage(), e);
            throw new RuntimeException("Delete Failed: " + e.getMessage());
        }
    }

    // ASSIGN CLASS
    @Override
    @Transactional
    public String assignClassToTeacher(Long teacherId, Long classId) {
        try {
            Teacher teacher = repo.findById(teacherId)
                    .orElseThrow(() -> new TeacherNotFoundException(teacherId));

            SchoolClass schoolClass = classRepo.findById(classId)
                    .orElseThrow(() -> new SchoolClassNotFoundException(classId));

            if (teacher.getAssignedClass().stream().noneMatch(c -> Objects.equals(c.getClassId(), classId))) {
                teacher.getAssignedClass().add(schoolClass);
            }

            repo.save(teacher);
            return "Class assigned to teacher successfully";

        } catch (Exception e) {
            log.error("Error assigning class {} to teacher {} -> {}", classId, teacherId, e.getMessage(), e);
            throw new RuntimeException("Assign Failed: " + e.getMessage());
        }
    }

    // UNASSIGN CLASS
    @Override
    @Transactional
    public String unassignClassFromTeacher(Long teacherId, Long classId) {
        try {
            Teacher teacher = repo.findById(teacherId)
                    .orElseThrow(() -> new TeacherNotFoundException(teacherId));

            teacher.getAssignedClass().removeIf(c -> Objects.equals(c.getClassId(), classId));
            repo.save(teacher);

            return "Class unassigned from teacher successfully";

        } catch (Exception e) {
            log.error("Error unassigning class {} from teacher {} -> {}", classId, teacherId, e.getMessage(), e);
            throw new RuntimeException("Unassign Failed: " + e.getMessage());
        }
    }

    // GET CLASSES OF TEACHER
    @Override
    @Transactional
    public List<SchoolClassResponseDto> getClassesOfTeacher(Long teacherId) {
        try {
            Teacher teacher = repo.findById(teacherId)
                    .orElseThrow(() -> new TeacherNotFoundException(teacherId));

            return teacher.getAssignedClass().stream()
                    .map(c -> new SchoolClassResponseDto(
                            c.getClassId(),
                            c.getClassName(),
                            c.getDescription(),
                            c.getCreatedAt(),
                            c.getUpdatedAt(),
                            c.getTeachers() == null
                                    ? Collections.emptyList()
                                    : c.getTeachers().stream().map(Teacher::getId).collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching classes of teacher {} -> {}", teacherId, e.getMessage(), e);
            throw new RuntimeException("Fetch Classes Failed: " + e.getMessage());
        }
    }

    // GET TEACHERS OF CLASS (MODIFIED) -> NOW RETURNS DTOS AND USES FIND-BY-ID-WITH-TEACHERS
    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponseDto> getTeachersOfClass(Long classId) {
        try {
            log.info("Fetching teachers for class id: {}", classId);

            SchoolClass schoolClass = classRepo.findByIdWithTeachers(classId)
                    .orElseThrow(() -> new SchoolClassNotFoundException(classId));

            List<Teacher> teachers = schoolClass.getTeachers() == null ? Collections.emptyList() : schoolClass.getTeachers();

            return teachers.stream()
                    .map(t -> {
                        TeacherResponseDto dto = new TeacherResponseDto();
                        dto.setId(t.getId());
                        dto.setFirstName(t.getFirstName());
                        dto.setLastName(t.getLastName());
                        dto.setEmail(t.getEmail());
                        dto.setPhone(t.getPhone());
                        dto.setGender(t.getGender());
                        dto.setQualification(t.getQualification());
                        dto.setExperienceYears(t.getExperienceYears());
                        dto.setDateOfBirth(t.getDateOfBirth());
                        List<Long> assigned = t.getAssignedClass() == null ? Collections.emptyList()
                                : t.getAssignedClass().stream().map(SchoolClass::getClassId).collect(Collectors.toList());
                        dto.setAssignedClassIds(assigned);
                        return dto;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error fetching teachers of class {} -> {}", classId, e.getMessage(), e);
            throw new RuntimeException("Fetch Teachers Failed: " + e.getMessage());
        }
    }


    @Override
    public List<User> getUnapprovedStatusRequest( )
    {
        try
        {
            List<User> ad = urepo.findAll();
            List<User> unapprovedStudent = ad.stream()
                    .filter(a -> a.getStatus() == Status.UNAPPROVED  && a.getRole()== Role.STUDENT || a.getRole()==Role.PARENT )
                    .toList();

            return unapprovedStudent;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    @Override
    public List<User> getapprovedStatusRequest( )
    {
        try
        {
            List<User> ad = urepo.findAll();
            List<User> approvedStudent = ad.stream()
                    .filter(a -> a.getStatus() == Status.APPROVED  && a.getRole()== Role.STUDENT || a.getRole()==Role.PARENT)
                    .toList();
            return approvedStudent;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    @Override
    public User updateStatus(Long id, Status status)
    {
        try
        {
            Optional<User> u=urepo.findById(id);
            if(u.isPresent())
            {
                User u1=u.get();
                if(u1.getStatus()==status)
                {
                    throw new RuntimeException("Already Done");
                }else {
                    u1.setStatus(status);
                    return urepo.save(u1);
                }
            }
            throw new RuntimeException("UserNotFoud");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String assignTeacher(Long classId, Long sectionId, AssignTeacherRequestDto dto) {
        SchoolClass schoolClass = classRepo.findById(classId)
                .orElseThrow(() -> new SchoolClassNotFoundException(classId));

        Section section = sectionRepo.findById(sectionId)
                .orElseThrow(()-> new SectionNotFoundException("Section not found for Id:"+sectionId));
        Teacher teacher = repo.findById(dto.getTeacherId())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found for Id:"+dto.getTeacherId()));

        // Create assignment entry
        AssignTeacherRequest assignment = new AssignTeacherRequest();
        assignment.setSchoolClass(schoolClass);
        assignment.setSectionId(section);
        assignment.setTeacher(teacher);

        assignRepo.save(assignment);

        return "Teacher assigned successfully to classID: "+classId+" with sectionID: "+sectionId;
    }

    @Override
    @Transactional
    public List<TeacherDto> getTeacherByClassId(Long classId) {
        try {
            SchoolClass schoolClass = classRepo.findById(classId)
                    .orElseThrow(() -> new SchoolClassNotFoundException(classId));
            List<Teacher> teachers = assignRepo.findTeachersByClassId(classId);

            return teachers.stream()
                    .map(t -> new TeacherDto(
                            t.getId(),
                            t.getFirstName(),
                            t.getLastName(),
                            t.getEmail(),
                            t.getPhone(),
                            t.getAdminMailId(),
                            t.getQualification(),
                            t.getExperienceYears(),
                            t.getGender(),
                            t.getDateOfBirth(),
                            t.getUser(),
                            t.getStatus(),
                            t.getProfilePicture(),

                            t.getAssignedSections() != null
                                    ? t.getAssignedSections().stream().map(sec -> sec.getSectionId()).toList()
                                    : null,

                            t.getAssignedClass() != null
                                    ? t.getAssignedClass().stream().map(cls -> cls.getClassId()).toList()
                                    : null
                    ))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error getting teacher for class ID {} -> {}", classId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public List<TeacherDto> getTeacherBySectionId(Long sectionId) {
        try {
            Section section = sectionRepo.findById(sectionId)
                    .orElseThrow(() -> new SectionNotFoundException("Section not found with ID " + sectionId));

            List<Teacher> teachers = assignRepo.findTeachersBySectionId(sectionId);

            return teachers.stream()
                    .map(t -> new TeacherDto(
                            t.getId(),
                            t.getFirstName(),
                            t.getLastName(),
                            t.getEmail(),
                            t.getPhone(),
                            t.getAdminMailId(),
                            t.getQualification(),
                            t.getExperienceYears(),
                            t.getGender(),
                            t.getDateOfBirth(),
                            t.getUser(),
                            t.getStatus(),
                            t.getProfilePicture(),

                            t.getAssignedSections() != null
                                    ? t.getAssignedSections().stream().map(sec -> sec.getSectionId()).toList()
                                    : null,

                            t.getAssignedClass() != null
                                    ? t.getAssignedClass().stream().map(cls -> cls.getClassId()).toList()
                                    : null
                    ))
                    .collect(Collectors.toList());


        } catch (Exception e) {
            log.error("Error getting teacher for section ID {} -> {}", sectionId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void uploadProfilePicture(Long id, MultipartFile file) throws IOException {
        Teacher teacher = repo.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));

        teacher.setProfilePicture(file.getBytes());
        repo.save(teacher);
    }

    @Override
    public void updateProfilePicture(Long id, MultipartFile file) throws IOException {
        Teacher teacher = repo.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));

        teacher.setProfilePicture(file.getBytes());
        repo.save(teacher);
    }

    @Override
    public void deleteProfilePicture(Long id) {
        Teacher teacher = repo.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));

        teacher.setProfilePicture(null);
        repo.save(teacher);
    }

    @Override
    public byte[] getProfilePicture(Long id) {
        Teacher teacher = repo.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id: " + id));

        return teacher.getProfilePicture();
    }


}




