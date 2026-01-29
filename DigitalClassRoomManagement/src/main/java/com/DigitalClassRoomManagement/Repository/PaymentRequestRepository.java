package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.PaymentRequest;
import com.DigitalClassRoomManagement.Enum.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRequestRepository extends JpaRepository<PaymentRequest,Long> {
    List<PaymentRequest> findByProcessedFalse();
    @Query("SELECT pr FROM PaymentRequest pr WHERE " +
            "(pr.student.studentRegId = :studentId OR :studentId IS NULL) AND " +
            "(pr.parent.parentId = :parentId OR :parentId IS NULL) " +
            "AND pr.processed = false " +
            "ORDER BY pr.startDate DESC LIMIT 1")
    Optional<PaymentRequest> findLatestRequest(Long studentId, Long parentId);

    List<PaymentRequest> findByStatus(PaymentStatus paymentStatus);

    @Query("""
SELECT DISTINCT s.parent.id, pr.endDate, pr.amount
FROM PaymentRequest pr
JOIN Student s ON s.schoolClass = pr.schoolClass
WHERE pr.endDate >= :start
  AND pr.endDate < :end
  AND pr.status = 'PENDING'
  AND s.parent IS NOT NULL
""")
    List<Object[]> findParentsForPaymentReminder(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );



}
