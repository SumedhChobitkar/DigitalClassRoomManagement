package com.DigitalClassRoomManagement.commonUtil;

import java.util.regex.Pattern;

public class MultimediaValidationClass {

    // Multimedia Content Validations

    //Title should start with capital, can contain letters, numbers, space, underscore, hyphen
    public static final Pattern TITLE_PATTERN =
            Pattern.compile("^[A-Z][A-Za-z0-9 _-]{1,99}$");

    //Type must be one of these fixed values (case-insensitive in logic)
    public static final Pattern TYPE_PATTERN =
            Pattern.compile("^(?i)(VIDEO|AUDIO|ANIMATION|IMAGE|PDF|YOUTUBE)$");

    //YouTube URL validation
    public static final Pattern YOUTUBE_URL_PATTERN =
            Pattern.compile("^(https?://)?(www\\.)?(youtube\\.com|youtu\\.be)/.+$");

    //File name validation (must have extension)
    public static final Pattern FILE_NAME_PATTERN =
            Pattern.compile("^[\\w,\\s-]+\\.[A-Za-z]{3,5}$");

    //MIME type validation for common media types
    public static final Pattern CONTENT_TYPE_PATTERN =
            Pattern.compile("^(video/|audio/|image/|application/pdf).+$");

    //Optional file size limit check (you can compare bytes separately)
    public static final long MAX_FILE_SIZE = 512L * 1024 * 1024; // 512 MB

    // Utility Methods (Optional)
    public static boolean isValidTitle(String title) {
        return title != null && TITLE_PATTERN.matcher(title).matches();
    }

    public static boolean isValidType(String type) {
        return type != null && TYPE_PATTERN.matcher(type.trim()).matches();
    }

    public static boolean isValidYoutubeUrl(String url) {
        return url != null && YOUTUBE_URL_PATTERN.matcher(url.trim()).matches();
    }

    public static boolean isValidFileName(String fileName) {
        return fileName != null && FILE_NAME_PATTERN.matcher(fileName.trim()).matches();
    }

    public static boolean isValidContentType(String contentType) {
        return contentType != null && CONTENT_TYPE_PATTERN.matcher(contentType.trim()).matches();
    }

    public static boolean isFileSizeAllowed(long sizeInBytes) {
        return sizeInBytes <= MAX_FILE_SIZE;
    }
}
