package com.DigitalClassRoomManagement.Exception;

public class ExamNotFoundException extends RuntimeException {

    public ExamNotFoundException() {
        super();
    }

    public ExamNotFoundException(String message) {
        super(message);
    }

    public ExamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}