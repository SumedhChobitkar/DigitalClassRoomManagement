package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Entity.Student;

import com.DigitalClassRoomManagement.Entity.User;

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

@CrossOrigin(origins = "*")
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
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z ]{2,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    // ✅ CREATE Parent
    @Override
    public Parent createParent(Parent parent) {
        try {
            validateParent(parent);
            logger.info("Creating parent with email: " + parent.getEmail());

            // ✅ Fetch actual User from DB
            User existingUser = userRepository.findById(parent.getUsers().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ✅ Fetch actual Student from DB
            Student existingStudent = studentRepository.findById(parent.getStudent().getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // ✅ Set the managed entities
            parent.setUsers(existingUser);
            parent.setStudent(existingStudent);

            // ✅ Save the parent
            Parent savedParent = parentRepository.save(parent);
            logger.info("Parent created successfully with ID: " + savedParent.getParentId());
            return savedParent;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating parent: " + e.getMessage(), e);
            throw new RuntimeException("Unable to create parent. " + e.getMessage());
        }
    }

    // ✅ GET Parent by ID
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

    // ✅ GET All Parents
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

    // ✅ UPDATE Parent
    @Override
    public Parent updateParent(Long id, Parent parentDetails) {
        try {
            validateParent(parentDetails);
            logger.info("Updating parent with ID: " + id);

            Parent existingParent = getParentById(id);

            // Fetch and set updated User and Student
            User existingUser = userRepository.findById(parentDetails.getUsers().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Student existingStudent = studentRepository.findById(parentDetails.getStudent().getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Update fields
            existingParent.setName(parentDetails.getName());
            existingParent.setEmail(parentDetails.getEmail());
            existingParent.setPhone(parentDetails.getPhone());
            existingParent.setAddress(parentDetails.getAddress());
            existingParent.setRelationship(parentDetails.getRelationship());
            existingParent.setUsers(existingUser);
            existingParent.setStudent(existingStudent);

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

    // ✅ DELETE Parent
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

    // ✅ Validation Method
    private void validateParent(Parent parent) {
        if (parent.getName() == null || !NAME_PATTERN.matcher(parent.getName()).matches()) {
            throw new IllegalArgumentException("Invalid name. Only letters and spaces allowed (2–50 chars).");
        }
        if (parent.getEmail() == null || !EMAIL_PATTERN.matcher(parent.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (parent.getPhone() != null && !PHONE_PATTERN.matcher(parent.getPhone()).matches()) {
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }
        if (parent.getRelationship() == null) {
            throw new IllegalArgumentException("Relationship type must not be null.");
        }
    }
}
