package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT s FROM Section s JOIN s.teachers t WHERE t.id = :teacherId")
    List<Section> findSectionsByTeacherId(@Param("teacherId") Long teacherId);
    //List<Section> findBySchoolClass(String schoolClass);
    List<Section> findBySchoolClass_ClassId(Long classId);
    List<Section> findBySchoolClass_ClassName(String className);

}
