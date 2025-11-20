package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Teacher;
import com.DigitalClassRoomManagement.Enum.TeacherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Fetch all teachers with their assigned classes (FIX for LazyInitializationException)
    @Query("SELECT DISTINCT t FROM Teacher t LEFT JOIN FETCH t.assignedClass")
    List<Teacher> findAllWithClasses();

    // Fetch single teacher with classes
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.assignedClass WHERE t.id = :id")
    Optional<Teacher> findByIdWithClasses(Long id);


    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<Teacher> findByStatus(TeacherStatus status);
}
