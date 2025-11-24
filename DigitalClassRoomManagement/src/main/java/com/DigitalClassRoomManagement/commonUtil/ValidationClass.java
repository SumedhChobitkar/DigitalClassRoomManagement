package com.DigitalClassRoomManagement.commonUtil;

import com.DigitalClassRoomManagement.Dto.homeworkDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class ValidationClass {

    //USER Validations
    public static final Pattern NAME_PATTERN = Pattern.compile("^[A-Z][a-zA-Z .]{1,49}$");

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");

    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])([a-zA-Z\\d@#$%^&+=!]{6,20})$");

    public static final Pattern ROLE_PATTERN = Pattern.compile("^(ADMIN|PRINCIPAL|TEACHER|STUDENT|PARENT)$");

    public static final Pattern LANGUAGE_PATTERN = Pattern.compile("^[A-Za-z -]{2,20}$");


    // DigitalBook
    public static final Pattern TITLE_PATTERN = Pattern.compile("^[A-Z][a-zA-Z0-9 ]*$");
    public static final Pattern GRADE_PATTERN = Pattern.compile("^[a-zA-Z0-9 ]+$");
    public static final Pattern SUBJECT_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
    public static final Pattern FILE_TYPE_PATTERN = Pattern.compile(".*\\.(pdf)$");
    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    // StudyMaterial//
    // public static final Pattern TITLE_PATTERN = Pattern.compile("^[A-Z][a-zA-Z0-9 ]*$");
    public static final String[] VALID_TYPES = {"Notes", "Worksheet", "LessonPlan"};
    public static final Pattern FILE_URL_PATTERN = Pattern.compile(".*\\.(pdf|docx|pptx)$");
    // public static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50 MB


    //SCHOOL CLASS VALIDATIONS

    /**
     * CLASS_NAME_PATTERN
     * Example valid names: "Class 1", "Grade 10", "Science A"
     * Rules: Must start with a letter, may include digits/spaces, 2–50 chars
     */
    public static final Pattern CLASS_NAME_PATTERN =
            Pattern.compile("^[A-Za-z][A-Za-z0-9 ]{1,49}$");

    /**
     * CLASS_DESCRIPTION_PATTERN
     * Example valid: "Primary Section for Science", "Senior batch 2025"
     * Rules: Alphabets, digits, spaces, punctuation .,- allowed; max 200 chars
     */
    public static final Pattern CLASS_DESCRIPTION_PATTERN =
            Pattern.compile("^[A-Za-z0-9 ,.-]{2,200}$");

    /**
     * SECTION_NAME_PATTERN
     * Optional (for future use if you re-enable Section entity)
     * Example valid: "A", "Section B", "Blue House"
     */
    //public static final Pattern SECTION_NAME_PATTERN =
     //       Pattern.compile("^[A-Za-z0-9 ]{1,20}$");

    /**
     * TEACHER_ASSIGN_ROLE_PATTERN
     * Ensures that only valid roles can be linked as class teachers.
     * Allowed: TEACHER, PRINCIPAL (optional future)
     */
    public static final Pattern TEACHER_ASSIGN_ROLE_PATTERN =
            Pattern.compile("^(TEACHER|PRINCIPAL)$");

    // ---------------- Homework Validation ----------------
    public static void validateHomework(homeworkDto dto) {
        if (dto == null)
            throw new IllegalArgumentException("Homework data cannot be null.");

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty.");
        if (!TITLE_PATTERN.matcher(dto.getTitle()).matches())
            throw new IllegalArgumentException("Invalid title format. Must start with a capital letter and contain only letters, numbers, or spaces.");

        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty())
            throw new IllegalArgumentException("Description cannot be empty.");
        if (dto.getDescription().length() < 5 || dto.getDescription().length() > 500)
            throw new IllegalArgumentException("Description must be between 5 and 500 characters.");

        if (dto.getAssignedDate() == null)
            throw new IllegalArgumentException("Assigned date cannot be null.");
        if (dto.getDueDate() == null)
            throw new IllegalArgumentException("Due date cannot be null.");
        if (dto.getAssignedDate().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Assigned date cannot be in the future.");
        if (dto.getDueDate().isBefore(dto.getAssignedDate()))
            throw new IllegalArgumentException("Due date cannot be earlier than assigned date.");
    }
    // ---------------- AuditLog Validation ----------------
    public static void validateAuditLog(Long userId, String username, String action, String module, LocalDateTime time) {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("User ID must be valid and greater than zero.");

        if (username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be empty.");
        if (!NAME_PATTERN.matcher(username).matches())
            throw new IllegalArgumentException("Invalid username format. Must start with a capital letter and contain only letters, spaces, or dots.");

        if (action == null || action.trim().isEmpty())
            throw new IllegalArgumentException("Action cannot be null or empty.");

        if (module == null || module.trim().isEmpty())
            throw new IllegalArgumentException("Module cannot be null or empty.");

        if (time == null)
            throw new IllegalArgumentException("Time cannot be null.");
        if (time.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("Time cannot be in the future.");
    }
    // ---------------- LibraryMember Validation ----------------
    public static void validateMember(Long userId, String membershipType, String status, Integer totalIssuedBooks) {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("User ID must be valid and greater than zero.");

        if (membershipType == null || membershipType.trim().isEmpty())
            throw new IllegalArgumentException("Membership type cannot be null or empty.");

        if (status == null || status.trim().isEmpty())
            throw new IllegalArgumentException("Member status cannot be null or empty.");

        if (totalIssuedBooks != null && totalIssuedBooks < 0)
            throw new IllegalArgumentException("Total issued books cannot be negative.");
    }
}

