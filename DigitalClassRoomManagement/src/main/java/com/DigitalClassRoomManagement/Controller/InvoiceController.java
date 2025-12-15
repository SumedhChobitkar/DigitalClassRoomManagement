package com.DigitalClassRoomManagement.Controller;

import com.DigitalClassRoomManagement.Dto.InvoiceDto;
import com.DigitalClassRoomManagement.Entity.Invoice;
import com.DigitalClassRoomManagement.Service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Invoice API", description = "Manage student invoices, payments & fee records")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Operation(summary = "Create new invoice")
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceDto dto) {
        log.info("API: Creating invoice for studentId={}", dto.getStudentId());
        Invoice invoice = invoiceService.createInvoice(dto);
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Update invoice by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable Long id,
            @RequestBody InvoiceDto dto) {

        log.info("API: Updating invoice id={}", id);
        Invoice updated = invoiceService.updateInvoice(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Get invoice by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        log.info("API: Fetching invoice id={}", id);
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Get all invoices")
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        log.info("API: Fetching all invoices");
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @Operation(summary = "Delete invoice by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id) {
        log.info("API: Deleting invoice id={}", id);
        String res = invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(res);
    }
}


// fee getfeestruturebystudentid
