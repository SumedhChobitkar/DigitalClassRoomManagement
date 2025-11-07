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

}

