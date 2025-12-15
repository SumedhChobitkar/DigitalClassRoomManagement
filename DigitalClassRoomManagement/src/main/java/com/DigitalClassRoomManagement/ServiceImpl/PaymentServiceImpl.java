package com.DigitalClassRoomManagement.ServiceImpl;

import com.DigitalClassRoomManagement.Dto.PaymentRequestDTO;
import com.DigitalClassRoomManagement.Entity.*;
import com.DigitalClassRoomManagement.Enum.PaymentMode;
import com.DigitalClassRoomManagement.Enum.PaymentStatus;
import com.DigitalClassRoomManagement.Repository.*;
import com.DigitalClassRoomManagement.Service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository repo;
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private ParentRepository parentRepo;

    @Autowired
    private SchoolClassRepository schoolClassRepo;

    @Autowired
    private PaymentRequestRepository paymentRequestRepo;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Override
    public String createPaymentRequest(PaymentRequestDTO dto) {

        SchoolClass schoolClass = schoolClassRepo.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        List<Student> students = studentRepo.findBySchoolClass_ClassId(dto.getClassId());

        if (students.isEmpty()) {
            throw new RuntimeException("No students found for this class");
        }

        for (Student student : students) {

            PaymentRequest request = new PaymentRequest();
            request.setStudent(student);
            request.setParent(parentRepo.findByStudentId(student.getStudentRegId()));
            request.setSchoolClass(schoolClass);   // *** FIXED ***
            request.setStartDate(dto.getStartDate());
            request.setEndDate(dto.getEndDate());
            request.setAmount(dto.getAmount());
            request.setStatus(PaymentStatus.PENDING);

            paymentRequestRepo.save(request);
        }

        return "Payment request created for class " + dto.getClassId();
    }

    @Override
    public Map<String, Object> createPaymentOrder(Long parentId, Long studentRegId) {

        try {
            PaymentRequest paymentRequest = paymentRequestRepo
                    .findLatestRequest(
                            studentRegId,
                            parentId
            )
                    .orElseThrow(() ->
                            new RuntimeException("No pending payment request found"));

            BigDecimal assignedAmount = paymentRequest.getAmount();

            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", assignedAmount.multiply(BigDecimal.valueOf(100)));
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);

            Order order = client.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", assignedAmount);
            response.put("currency", "INR");
            response.put("studentId", studentRegId);
            response.put("parentId", parentId);
            response.put("status", "CREATED");

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> verifyPayment(
            String orderId,
            String paymentId,
            String razorpaySignature,
            String status,
            Long parentId,
            Long studentRegId) {

        Map<String, Object> response = new HashMap<>();

        try {
            String payload = orderId + "|" + paymentId;
            System.out.println("Payload Used for Signature: " + payload);
            String expectedSignature = generateRazorpaySignature(payload, razorpayKeySecret);
            if (!expectedSignature.trim().equals(razorpaySignature.trim())) {
                System.out.println(" SIGNATURE NOT MATCHING — Payment Verification Failed");
                throw new IllegalArgumentException("Invalid payment signature.");
            }
            String cleanStatus = status.trim();
            System.out.println("Clean Payment Status        : '" + cleanStatus + "'");
            if (!"success".equalsIgnoreCase(cleanStatus)) {
                System.out.println(" Payment Not Successful — Status: " + cleanStatus);
                throw new IllegalArgumentException("Payment status is not successful: " + cleanStatus);
            }
            Student student = studentRepo.findById(studentRegId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Parent parent = parentRepo.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            com.razorpay.Payment razorpayPayment = client.payments.fetch(paymentId);

            int amountInPaise = razorpayPayment.get("amount");
            BigDecimal actualAmount = new BigDecimal(amountInPaise)
                    .divide(new BigDecimal(100));

            System.out.println("PAYMENT VERIFIED SUCCESSFULLY");
            Payment payment = new Payment();
            payment.setInvoice_id(0L);
            payment.setStudent(student);
            payment.setParent(parent);
            payment.setAmount(actualAmount);
            payment.setPaymentMode(PaymentMode.UPI);
            payment.setTransactionId(paymentId);
            payment.setGatewayReferenceId(orderId);
            payment.setPaymentDate(Instant.now());
            payment.setStatus(PaymentStatus.PAID);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setPaymentTime(LocalDateTime.now());

            Optional<PaymentRequest> paymentRequestOpt =
                    paymentRequestRepo.findLatestRequest(studentRegId, parentId);
            if (paymentRequestOpt.isPresent()) {
                PaymentRequest paymentRequest = paymentRequestOpt.get();
                payment.setStartDate(paymentRequest.getStartDate());
                payment.setEndDate(paymentRequest.getEndDate());
                paymentRequest.setStatus(PaymentStatus.PAID);
                paymentRequest.setProcessed(true);
                paymentRequestRepo.save(paymentRequest);

            } else {
                payment.setStartDate(null);
                payment.setEndDate(null);
            }
            Payment savedPayment = repo.save(payment);
            System.out.println("Success");
            response.put("message", "Payment Verified Successfully");
            response.put("orderId", orderId);
            response.put("paymentId", paymentId);
            response.put("parentId", parentId);
            response.put("studentRegId", studentRegId);
            response.put("paymentStatus", "SUCCESS");
            return response;

        } catch (Exception e) {
            System.out.println(" EXCEPTION in verifyPayment(): " + e.getMessage());
            throw new IllegalArgumentException("Payment verification failed: " + e.getMessage());
        }
    }


    public String generateRazorpaySignature(String payload, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(payload.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    @Override
    public List<Payment> getAllPayments() {
        return repo.findAll();
    }


    @Override
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {

        return repo.findByStatus(status);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateExpiredPaymentsToUnpaid() {

        LocalDate today = LocalDate.now();

        List<PaymentRequest> pendingRequests = paymentRequestRepo.findByStatus(PaymentStatus.PENDING);

        for (PaymentRequest req : pendingRequests) {
            if (req.getEndDate() != null && req.getEndDate().isBefore(today)) {
                req.setStatus(PaymentStatus.UNPAID);
            }
        }

        paymentRequestRepo.saveAll(pendingRequests);
        System.out.println("Expired payment requests updated at " + today);
    }

    @Override
    public Map<String, Object> fetchPaymentInfo(Long studentId, Long parentId) {

        Map<String, Object> result = new HashMap<>();
        LocalDate today = LocalDate.now();

        Optional<PaymentRequest> reqOpt = paymentRequestRepo.findLatestRequest(studentId, parentId);
        if (reqOpt.isPresent()) {
            PaymentRequest r = reqOpt.get();


            if (r.getEndDate() != null) {

                if (r.getEndDate().isBefore(today)) {
                    if (r.getStatus() == PaymentStatus.PENDING) {
                        r.setStatus(PaymentStatus.UNPAID);
                        paymentRequestRepo.save(r);
                    }
                }
                else {

                    if (r.getStatus() == PaymentStatus.UNPAID) {
                        r.setStatus(PaymentStatus.PENDING);
                        paymentRequestRepo.save(r);
                    }
                }
            }
            result.put("request", Map.of(
                    "requestId", r.getRequestId(),
                    "startDate", r.getStartDate(),
                    "endDate", r.getEndDate(),
                    "amount", r.getAmount(),
                    "status", r.getStatus().name()
            ));
        }


        Optional<Payment> paymentOpt = repo.findLatestPayment(studentId, parentId);
        if (paymentOpt.isPresent()) {
            Payment p = paymentOpt.get();
            result.put("payment", Map.of(
                    "paymentId", p.getPaymentId(),
                    "orderId", p.getGatewayReferenceId(),
                    "razorpayPaymentId", p.getTransactionId(),
                    "invoice_id", p.getInvoice_id(),
                    "amount", p.getAmount(),
                    "status", p.getStatus().name(),
                    "payment_date", p.getPaymentDate()
            ));
        }

        if (result.isEmpty()) {
            result.put("message", "No payment or payment request found");
        }

        return result;
    }


    @Override
    public String deletePaymentByOrderId(String orderId) {

        int deleted = repo.deleteByOrderId(orderId);

        if (deleted == 0) {
            return "No payment found with Order ID: " + orderId;
        }

        return "Payment deleted successfully for Order ID: " + orderId;
    }
}

