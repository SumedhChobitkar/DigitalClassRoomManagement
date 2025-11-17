package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

    Optional<SchoolClass> findByClassNameIgnoreCase(String name);
    boolean existsByClassNameIgnoreCase(String name);

    // --- fetch join to initialize teachers collection within a single query/session ---
    @Query("select sc from SchoolClass sc left join fetch sc.teachers where sc.classId = :id")
    Optional<SchoolClass> findByIdWithTeachers(@Param("id") Long id);

    @Query("select distinct sc from SchoolClass sc left join fetch sc.teachers")
    List<SchoolClass> findAllWithTeachers();
}
