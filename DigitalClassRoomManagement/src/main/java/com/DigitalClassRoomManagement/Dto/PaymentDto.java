package com.DigitalClassRoomManagement.Dto;

import com.DigitalClassRoomManagement.Entity.Parent;
import com.DigitalClassRoomManagement.Entity.Student;
import com.DigitalClassRoomManagement.Enum.PaymentMode;
import com.DigitalClassRoomManagement.Enum.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String paymentId;

//    @NotNull

    //    @ManyToOne(fetch = FetchType.LAZY)
//
//    @JoinColumn(name = "invoice_id", nullable = false)
//
//    private Invoice invoice;
    @NotNull
    private Long invoice_id;

    @NotNull
    private String orderId;
    private String signature;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentRegId", nullable = false)
    private Student student;

    private Long studentRegId;
    private  Long parentId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", nullable = false)
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime paymentTime;
}
