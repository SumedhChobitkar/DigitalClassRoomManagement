package com.DigitalClassRoomManagement.Service;
import com.DigitalClassRoomManagement.Dto.InvoiceDto;
import com.DigitalClassRoomManagement.Entity.Invoice;
import java.util.List;
public interface InvoiceService {
    Invoice createInvoice(InvoiceDto dto);
    Invoice updateInvoice(Long id, InvoiceDto dto);
    Invoice getInvoiceById(Long id);
    List<Invoice> getAllInvoices();
    String deleteInvoice(Long id);
}
