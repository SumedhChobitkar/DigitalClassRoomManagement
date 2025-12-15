package com.DigitalClassRoomManagement.Repository;

import com.DigitalClassRoomManagement.Entity.Payment;
import com.DigitalClassRoomManagement.Enum.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByStatus(PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE " +
            "(p.student.studentRegId = :studentId OR :studentId IS NULL) AND " +
            "(p.parent.parentId = :parentId OR :parentId IS NULL) " +
            "ORDER BY paymentDate DESC LIMIT 1")
    Optional<Payment> findLatestPayment(Long studentId, Long parentId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Payment p WHERE p.gatewayReferenceId = :orderId")
    int deleteByOrderId(@Param("orderId") String orderId);

}
