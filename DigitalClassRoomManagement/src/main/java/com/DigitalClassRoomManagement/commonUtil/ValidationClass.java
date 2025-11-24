package com.DigitalClassRoomManagement.commonUtil;

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

}

