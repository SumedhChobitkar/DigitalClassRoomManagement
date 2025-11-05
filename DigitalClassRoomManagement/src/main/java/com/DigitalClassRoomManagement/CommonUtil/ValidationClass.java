package com.DigitalClassRoomManagement.CommonUtil;

import java.util.regex.Pattern;

public class ValidationClass {

    //USER Validations
    public static final Pattern NAME_PATTERN = Pattern.compile("^[A-Z][a-zA-Z .]{1,49}$");

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");

    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])([a-zA-Z\\d@#$%^&+=!]{6,20})$");

    public static final Pattern ROLE_PATTERN = Pattern.compile("^(ADMIN|PRINCIPAL|TEACHER|STUDENT|PARENT)$");

    public static final Pattern LANGUAGE_PATTERN = Pattern.compile("^[A-Za-z -]{2,20}$");
}
