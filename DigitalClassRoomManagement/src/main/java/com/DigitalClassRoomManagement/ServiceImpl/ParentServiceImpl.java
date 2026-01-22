package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Entity.Student;

import com.DigitalClassRoomManagement.Entity.User;

import com.DigitalClassRoomManagement.Enum.Relationship;
import com.DigitalClassRoomManagement.Exception.ParentNotFoundException;
import com.DigitalClassRoomManagement.Repository.ParentRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Repository.UserRepository;
import com.DigitalClassRoomManagement.Service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.DigitalClassRoomManagement.commonUtil.ValidationClass;



@Service
public class ParentServiceImpl implements ParentService {

    private static final Logger logger = Logger.getLogger(ParentServiceImpl.class.getName());

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Regex validation patterns
    /*private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z ]{2,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");*/

    //  CREATE Parent
    @Override
    public Parent createParent(Parent parent) {
        try {
            validateParent(parent);
            logger.info("Creating parent with email: " + parent.getEmail());

            //  Fetch actual User from DB
            User existingUser = userRepository.findById(parent.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            //  Fetch actual Student from DB
            Student existingStudent = studentRepository.findById(parent.getStudent().getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            //  Set the managed entities
            parent.setUser(existingUser);
            parent.setStudent(existingStudent);


            //  Save the parent
            Parent savedParent = parentRepository.save(parent);
            logger.info("Parent created successfully with ID: " + savedParent.getParentId());
            return savedParent;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating parent: " + e.getMessage(), e);
            throw new RuntimeException("Unable to create parent. " + e.getMessage());
        }
    }

    //  GET Parent by ID
    @Override
    public Parent getParentById(Long id) {
        try {
            logger.info("Fetching parent with ID: " + id);
            Optional<Parent> parentOpt = parentRepository.findById(id);
            if (parentOpt.isPresent()) {
                logger.info("Parent found with ID: " + id);
                return parentOpt.get();
            } else {
                logger.warning("Parent not found with ID: " + id);
                throw new ParentNotFoundException("Parent not found with ID: " + id);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching parent: " + e.getMessage(), e);
            throw new RuntimeException("Error retrieving parent with ID: " + id);
        }
    }

    //  GET All Parents
    @Override
    public List<Parent> getAllParents() {
        try {
            logger.info("Fetching all parents...");
            List<Parent> parents = parentRepository.findAll();
            logger.info("Total parents found: " + parents.size());
            return parents;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching parent list: " + e.getMessage(), e);
            throw new RuntimeException("Unable to fetch parent list.");
        }
    }

    //  UPDATE Parent
    @Override
    public Parent updateParent(Long id, Parent parentDetails) {
        try {
            validateParent(parentDetails);
            logger.info("Updating parent with ID: " + id);

            Parent existingParent = getParentById(id);

            // Fetch and set updated User and Student
            User existingUser = userRepository.findById(parentDetails.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Student existingStudent = studentRepository.findById(parentDetails.getStudent().getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Update fields
           // existingParent.setName(parentDetails.getName());
            existingParent.setFirstName(parentDetails.getFirstName());
            existingParent.setLastName(parentDetails.getLastName());
            existingParent.setEmail(parentDetails.getEmail());
            existingParent.setPhone(parentDetails.getPhone());
            existingParent.setAddress(parentDetails.getAddress());
            existingParent.setRelationship(parentDetails.getRelationship());
            existingParent.setUser(existingUser);
            existingParent.setStudent(existingStudent);

            // validateParent(parent);
            // Save updated parent
            Parent updatedParent = parentRepository.save(existingParent);
            logger.info("Parent updated successfully with ID: " + id);
            return updatedParent;

        } catch (ParentNotFoundException e) {
            logger.warning("Parent not found: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating parent: " + e.getMessage(), e);
            throw new RuntimeException("Unable to update parent with ID: " + id + ". " + e.getMessage());
        }
    }

    //  DELETE Parent
    @Override
    public void deleteParent(Long id) {
        try {
            logger.info("Deleting parent with ID: " + id);
            Parent parent = getParentById(id);
            parentRepository.delete(parent);
            logger.info("Parent deleted successfully with ID: " + id);
        } catch (ParentNotFoundException e) {
            logger.warning("Delete failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting parent: " + e.getMessage(), e);
            throw new RuntimeException("Unable to delete parent with ID: " + id);
        }
    }

    @Override
    public String linkParentToStudent(Long parentId, Long studentId, String relationship) {

        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + parentId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        Relationship rel;
        try {
            rel = Relationship.valueOf(relationship.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Invalid relationship type. Allowed: FATHER, MOTHER, GUARDIAN, OTHER."
            );
        }

        parent.setStudent(student);
        parent.setRelationship(rel);
        parentRepository.save(parent);

        return "Parent successfully linked with student.";
    }

    //  Validation logic
    private void validateParent(Parent parent) {

        // Validate Parent Name
        /*if (parent.getName() == null ||
                !ValidationClass.PARENT_NAME_PATTERN.matcher(parent.getName()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Parent Name. It must start with a capital letter and be 2–50 characters."
            );
        }*/
        // Validate Parent First Name
        if (parent.getFirstName() == null ||
                !ValidationClass.PARENT_NAME_PATTERN
                        .matcher(parent.getFirstName())
                        .matches()) {

            throw new IllegalArgumentException(
                    "Invalid Parent First Name. It must start with a capital letter and be 2–50 characters."
            );
        }

        // Validate Parent Last Name
        if (parent.getLastName() == null ||
                !ValidationClass.PARENT_NAME_PATTERN
                        .matcher(parent.getLastName())
                        .matches()) {

            throw new IllegalArgumentException(
                    "Invalid Parent Last Name. It must start with a capital letter and be 2–50 characters."
            );
        }


        // Validate Email
        if (parent.getEmail() == null ||
                !ValidationClass.PARENT_EMAIL_PATTERN.matcher(parent.getEmail()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Email Format."
            );
        }

        // Validate Mobile Number
        if (parent.getPhone() == null ||
                !ValidationClass.PARENT_MOBILE_PATTERN.matcher(parent.getPhone()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Mobile Number. Must be 10 digits starting with 6-9."
            );
        }

        // Validate Relationship
        if (parent.getRelationship() == null ||
                !ValidationClass.RELATION_PATTERN
                        .matcher(parent.getRelationship().name())
                        .matches()) {

            throw new IllegalArgumentException(
                    "Invalid Relationship. Allowed: Father, Mother, Guardian, Other."
            );
        }

        // Validate Address
        if (parent.getAddress() == null ||
                !ValidationClass.ADDRESS_PATTERN.matcher(parent.getAddress()).matches()) {

            throw new IllegalArgumentException(
                    "Invalid Address. Allowed 5–200 characters including letters, numbers, comma, dash, slash."
            );
        }




        // Validate Student
        if (parent.getStudent() == null) {
            throw new IllegalArgumentException(
                    "Student reference is required for Parent."
            );
        }

        // Validate User
        if (parent.getUser() == null) {
            throw new IllegalArgumentException(
                    "User reference is required for Parent."
            );
        }
    }





}
