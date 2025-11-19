package com.DigitalClassRoomManagement.Exception;

public class SchoolClassNotFoundException extends RuntimeException {
    public SchoolClassNotFoundException(Long id) {
        super("SchoolClass not found with id: " + id);
    }
}
