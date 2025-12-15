package com.DigitalClassRoomManagement.Entity;

import com.DigitalClassRoomManagement.Enum.InvoiceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "fee_id", nullable = false)
    private FeeStructure feeStructure;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal totalDue = BigDecimal.ZERO;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @PastOrPresent
    private Instant generatedAt;

    @PastOrPresent
    private Instant updatedAt;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal amountPaid = BigDecimal.ZERO;


    // payemnt array
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "invoice_payments",
//            joinColumns = @JoinColumn(name = "invoice_id"),
//            inverseJoinColumns = @JoinColumn(name = "payment_id")
  //  )
   // private List<Payment> payments = new ArrayList<>();
    private BigDecimal paidAmount;
    private String paymentMode;
    private String transactionId;
    private String paymentStatus;
    private Instant paymentDate;
private BigDecimal totalAmount;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.generatedAt = now;
        this.updatedAt = now;
        if (this.amountPaid == null) this.amountPaid = BigDecimal.ZERO;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void recomputeStatus() {
        if (amountPaid == null) amountPaid = BigDecimal.ZERO;
        if (totalDue == null) totalDue = BigDecimal.ZERO;

        int cmp = amountPaid.compareTo(totalDue);

        if (cmp >= 0) {
            this.status = InvoiceStatus.PAID;
            this.amountPaid = totalDue;
        } else if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
            this.status = InvoiceStatus.PARTIALLY_PAID;
        } else {
            if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
                this.status = InvoiceStatus.OVERDUE;
            } else {
                this.status = InvoiceStatus.UNPAID;
            }
        }
    }
}
