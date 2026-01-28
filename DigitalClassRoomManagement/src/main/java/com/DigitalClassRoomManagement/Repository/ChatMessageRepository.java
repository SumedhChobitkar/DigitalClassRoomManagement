package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 🔹 Last 7 days (Student ↔ Teacher)
    List<ChatMessage> findByTeacher_IdAndSenderAndTimestampAfter(
            Long teacherId,
            String sender,
            LocalDateTime after
    );

    @Modifying
    @Query("""
        DELETE FROM ChatMessage c
        WHERE c.timestamp < :cutoff
    """)
    void deleteMessagesOlderThan(
            @Param("cutoff") LocalDateTime cutoff
    );





    List<ChatMessage> findByTeacher_IdAndStudent_StudentRegIdOrderByTimestampAsc(
            Long teacherId,
            Long studentRegId
    );


    List<ChatMessage> findByTeacher_IdAndParent_ParentIdOrderByTimestampAsc(
            Long teacherId,
            Long parentId
    );

    List<ChatMessage> findByTeacher_IdOrderByTimestampAsc(Long teacherId);
@Modifying
@Query("""
UPDATE ChatMessage c
SET c.isRead = true, c.readAt = :now
WHERE c.teacher.id = :teacherId
  AND c.sender <> 'TEACHER'
  AND c.isRead = false
""")
void markTeacherMessagesAsRead(
        @Param("teacherId") Long teacherId,
        @Param("now") LocalDateTime now
);
    @Modifying
    @Query("""
UPDATE ChatMessage c
SET c.isRead = true, c.readAt = :now
WHERE c.teacher.id = :teacherId
  AND c.student.studentRegId = :studentRegId
  AND c.sender = 'TEACHER'
  AND c.isRead = false
""")
    void markStudentMessagesAsRead(
            @Param("teacherId") Long teacherId,
            @Param("studentRegId") Long studentRegId,
            @Param("now") LocalDateTime now
    );
    @Modifying
    @Query("""
UPDATE ChatMessage c
SET c.isRead = true, c.readAt = :now
WHERE c.teacher.id = :teacherId
  AND c.parent.parentId = :parentId
  AND c.sender = 'TEACHER'
  AND c.isRead = false
""")
    void markParentMessagesAsRead(
            @Param("teacherId") Long teacherId,
            @Param("parentId") Long parentId,
            @Param("now") LocalDateTime now
    );




}