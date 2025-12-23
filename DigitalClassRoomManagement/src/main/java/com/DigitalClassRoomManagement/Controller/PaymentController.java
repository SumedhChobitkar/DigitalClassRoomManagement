package com.DigitalClassRoomManagement.Controller;
import com.DigitalClassRoomManagement.Dto.PaymentRequestDTO;
import com.DigitalClassRoomManagement.Entity.Payment;
import com.DigitalClassRoomManagement.Enum.PaymentStatus;
import com.DigitalClassRoomManagement.Repository.PaymentRequestRepository;
import com.DigitalClassRoomManagement.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;


    @PreAuthorize("hasRole('PRINCIPAL')")
    @PostMapping("/create-class-payment-request")
    public ResponseEntity<?> createClassPaymentRequest(@RequestBody PaymentRequestDTO dto) {
        String msg = paymentService.createPaymentRequest(dto);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(
            @RequestParam Long parentId,
            @RequestParam Long studentRegId
    ) {
        try {
            return ResponseEntity.ok(paymentService.createPaymentOrder( parentId, studentRegId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestParam String orderId,
            @RequestParam String paymentId,
            @RequestParam String razorpaySignature,
            @RequestParam String status,
            @RequestParam Long parentId,
            @RequestParam Long studentRegId
           ) {

        try {
            Map<String, Object> response = paymentService.verifyPayment(
                    orderId,
                    paymentId,
                    razorpaySignature,
                    status,
                    parentId,
                    studentRegId
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", "An unexpected error occurred: " + e.getMessage())
            );
        }
    }


    @GetMapping("/allPayments")
    public List<Payment> getAll() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/status/{status}")
    public List<Payment> getPaymentByStatus(@PathVariable PaymentStatus status) {
        return paymentService.getPaymentsByStatus(status);
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> fetchPaymentInfo(
            @RequestParam(required = false) Long studentRegId,
            @RequestParam(required = false) Long parentId) {

        if (studentRegId == null && parentId == null) {
            return ResponseEntity.badRequest().body("Provide studentId or parentId");
        }

        return ResponseEntity.ok(paymentService.fetchPaymentInfo(studentRegId, parentId));
    }

    @DeleteMapping("/delete-payment/{orderId}")
    public ResponseEntity<String> deletePayment(@PathVariable String orderId) {
        String response = paymentService.deletePaymentByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

}
