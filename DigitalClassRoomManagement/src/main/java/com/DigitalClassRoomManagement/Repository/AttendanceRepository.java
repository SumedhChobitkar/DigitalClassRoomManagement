package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Attendance;
import com.DigitalClassRoomManagement.Enum.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findBySessionIdAndEmail(Long sessionId, String email);
    List<Attendance> findAllBySessionId(Long sessionId);
    long countByEmailAndStatus(String email, AttendanceStatus status);

    @Query("""
        SELECT COUNT(DISTINCT a.classDate)
        FROM Attendance a
        WHERE a.email = :email
    """)
    long countDistinctClassDays(@Param("email") String email);

    //METHOD
    List<Attendance> findAllByEmailAndClassDate(String email, LocalDate classDate);
    List<Attendance> findAllByEmail(String email);
}
