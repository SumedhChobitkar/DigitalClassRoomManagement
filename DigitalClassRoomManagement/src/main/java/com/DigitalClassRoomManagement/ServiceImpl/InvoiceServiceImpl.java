package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.InvoiceDto;
import com.DigitalClassRoomManagement.Entity.FeeStructure;
import com.DigitalClassRoomManagement.Entity.Invoice;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Enum.InvoiceStatus;
import com.DigitalClassRoomManagement.Repository.FeeStructureRepository;
import com.DigitalClassRoomManagement.Repository.InvoiceRepository;
import com.DigitalClassRoomManagement.Repository.StudentRepository;
import com.DigitalClassRoomManagement.Service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
@Service
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final StudentRepository studentRepository;
    private final FeeStructureRepository feeStructureRepository;
    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            StudentRepository studentRepository,
            FeeStructureRepository feeStructureRepository) {
        this.invoiceRepository = invoiceRepository;
        this.studentRepository = studentRepository;
        this.feeStructureRepository = feeStructureRepository;
    }
    @Override
    public Invoice createInvoice(InvoiceDto dto) {

        try {

            log.info("Creating new invoice for studentId: {}", dto.getStudentId());
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            FeeStructure fee = feeStructureRepository.findById(dto.getFeeStructureId())
                    .orElseThrow(() -> new RuntimeException("Fee Structure not found"));
            Invoice invoice = new Invoice();
            //payment hardcoded
            invoice.setPaidAmount(new BigDecimal("1000"));
            invoice.setPaymentMode("CASH");
            invoice.setTransactionId("TEST_TXN_" + System.currentTimeMillis());
            invoice.setPaymentStatus("SUCCESS");
            invoice.setPaymentDate(Instant.now());
            invoice.setStatus (InvoiceStatus.PAID);



            invoice.setStudent(student);
            invoice.setFeeStructure(fee);
            invoice.setTotalDue(dto.getTotalDue());
            invoice.setDueDate(dto.getDueDate());
            invoice.setAmountPaid(dto.getAmountPaid());

            invoice.recomputeStatus();
            return invoiceRepository.save(invoice);

        } catch (Exception e) {
            log.error("Error creating invoice: {}", e.getMessage());
            throw new RuntimeException("Failed to create invoice");
        }
    }
    @Override
    public Invoice updateInvoice(Long id, InvoiceDto dto) {
        try {
            log.info("Updating invoice {}", id);
            Invoice invoice = invoiceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));
            if (dto.getTotalDue() != null) invoice.setTotalDue(dto.getTotalDue());
            if (dto.getAmountPaid() != null) invoice.setAmountPaid(dto.getAmountPaid());
            if (dto.getDueDate() != null) invoice.setDueDate(dto.getDueDate());
            invoice.recomputeStatus();
            return invoiceRepository.save(invoice);
        } catch (Exception e) {
            log.error("Error updating invoice: {}", e.getMessage());
            throw new RuntimeException("Failed to update invoice");
        }
    }
    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }
    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
    @Override
    public String deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
        return "Invoice deleted successfully";
    }
}
//getinvoiceby student