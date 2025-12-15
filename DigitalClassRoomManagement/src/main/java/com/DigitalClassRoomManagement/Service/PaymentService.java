package com.DigitalClassRoomManagement.Service;


import com.DigitalClassRoomManagement.Dto.PaymentRequestDTO;
import com.DigitalClassRoomManagement.Entity.Payment;
import com.DigitalClassRoomManagement.Enum.PaymentStatus;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    String createPaymentRequest(PaymentRequestDTO dto);

    Map<String, Object> createPaymentOrder(Long parentId, Long studentRegId);

    Map<String, Object> verifyPayment(
            String orderId,
            String paymentId,
            String razorpaySignature,
            String status,
            Long parentId,
            Long studentRegId

    );
    List<Payment> getAllPayments();

    List<Payment> getPaymentsByStatus(PaymentStatus paymentStatus);

    Map<String,Object> fetchPaymentInfo(Long studentId, Long parentId);

    String deletePaymentByOrderId(String orderId);
}
