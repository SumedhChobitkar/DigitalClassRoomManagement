package com.DigitalClassRoomManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class DigitalClassRoomManagementApplication {

	public static void main(String[] args) {
        SpringApplication.run(DigitalClassRoomManagementApplication.class, args);
        System.out.println("Application runed ");
    }
}
