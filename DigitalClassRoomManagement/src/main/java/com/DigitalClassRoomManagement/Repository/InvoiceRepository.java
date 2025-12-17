package com.DigitalClassRoomManagement.Repository;
import com.DigitalClassRoomManagement.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findTopByStudent_StudentRegId_OrderByInvoiceIdDesc(Long studentRegId);
}
