package com.DigitalClassRoomManagement.Repository;
import com.DigitalClassRoomManagement.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
