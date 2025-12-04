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

    // Section Related
    public static final Pattern SECTION_NAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9 ]*$");
    public static final Pattern SCHOOL_CLASS_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
    public static final int MIN_CAPACITY = 1;
    public static final int MAX_CAPACITY = 200;
    public static final int SECTION_NAME_MAX_LENGTH = 50;

    // Timetable Related
    public static final Pattern TIMETABLE_CLASS_PATTERN = Pattern.compile("^[A-Za-z0-9 ]{1,20}$");
    public static final Pattern DAY_OF_WEEK_PATTERN = Pattern.compile("^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$");
    public static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    // Format: YYYY-MM-DD
    public static final Pattern TIME_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$");
    // Format: YYYY-MM-DDTHH:MM
    public static final int MIN_PERIOD_MINUTES = 20;      // Minimum lecture duration = 20 minutes
    public static final int MAX_PERIOD_MINUTES = 180;     // Maximum lecture duration = 3 hours
    public static final int MAX_TIMETABLE_ENTRIES_PER_DAY = 12;


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

    //  STUDENT Validations
    public static final Pattern ROLL_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9]{2,20}$");
    public static final Pattern ADMISSION_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9-]{2,20}$");
    public static final Pattern ACADEMIC_YEAR_PATTERN = Pattern.compile("^\\d{4}-\\d{4}$");
    public static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    public static final Pattern GENDER_PATTERN = Pattern.compile("^(Male|Female|Other)$", Pattern.CASE_INSENSITIVE);
    public static final Pattern PINCODE_PATTERN = Pattern.compile("^\\d{6}$");
    public static final Pattern CITY_STATE_COUNTRY_PATTERN = Pattern.compile("^[A-Za-z ]{2,50}$");


    //  PARENT Validations
    public static final Pattern PARENT_NAME_PATTERN = Pattern.compile("^[A-Z][a-zA-Z .]{1,49}$");
    public static final Pattern RELATION_PATTERN = Pattern.compile("^(Father|Mother|Guardian|Other)$", Pattern.CASE_INSENSITIVE);
    public static final Pattern PARENT_EMAIL_PATTERN = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
    public static final Pattern PARENT_MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    public static final Pattern ADDRESS_PATTERN = Pattern.compile("^[A-Za-z0-9 ,.\\-#/]{5,200}$");
    public static final Pattern OCCUPATION_PATTERN = Pattern.compile("^[A-Za-z ]{2,50}$");
    public static final Pattern INCOME_PATTERN = Pattern.compile("^\\d{1,10}(\\.\\d{1,2})?$");

    public static void validateContactUs(String schoolName, String email, String phone, String address) {

        // ---- School Name ----
        if (schoolName == null || schoolName.trim().isEmpty())
            throw new IllegalArgumentException("School name is required.");

        if (!NAME_PATTERN.matcher(schoolName).matches())
            throw new IllegalArgumentException("School name must start with a capital letter and contain only letters, spaces, or dots.");

        // ---- Email ----
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email is required.");

        if (!EMAIL_PATTERN.matcher(email).matches())
            throw new IllegalArgumentException("Invalid email format.");

        // ---- Phone ----
        if (phone == null || phone.trim().isEmpty())
            throw new IllegalArgumentException("Phone number is required.");

        if (!phone.matches("^[0-9]{10}$"))
            throw new IllegalArgumentException("Phone number must be exactly 10 digits.");

        // ---- Address ----
        if (address == null || address.trim().isEmpty())
            throw new IllegalArgumentException("Address cannot be empty.");

        if (address.length() < 10 || address.length() > 2000)
            throw new IllegalArgumentException("Address must be between 10 and 2000 characters.");
    }
}



