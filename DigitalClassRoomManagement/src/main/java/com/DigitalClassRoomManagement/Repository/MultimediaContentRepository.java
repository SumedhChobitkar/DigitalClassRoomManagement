package com.DigitalClassRoomManagement.Repository;
import com.DigitalClassRoomManagement.Entity.MultimediaContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MultimediaContentRepository extends JpaRepository<MultimediaContent, Long> {
    List<MultimediaContent> findByTypeIgnoreCase(String type);
}
