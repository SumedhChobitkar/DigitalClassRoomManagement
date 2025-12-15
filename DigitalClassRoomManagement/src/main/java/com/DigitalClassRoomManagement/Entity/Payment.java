package com.DigitalClassRoomManagement.Entity;


import com.DigitalClassRoomManagement.Enum.PaymentMode;
import com.DigitalClassRoomManagement.Enum.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

//    @NotNull

//    @ManyToOne(fetch = FetchType.LAZY)
//
//    @JoinColumn(name = "invoice_id", nullable = false)
//
//    private Invoice invoice;

    private Long invoice_id;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentRegId", nullable = false)
    @JsonIgnore
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Parent parent;


    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(nullable = false, unique = true)
    private String transactionId;
    private String gatewayReferenceId;

    @PastOrPresent
    @Column(nullable = false)
    private Instant paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime paymentTime;

}