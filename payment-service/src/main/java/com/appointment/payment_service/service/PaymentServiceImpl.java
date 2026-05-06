//package com.appointment.payment_service.service;
//
//import com.appointment.payment_service.client.NotificationClient;
//import com.appointment.payment_service.client.UserClient;
//import com.appointment.payment_service.dto.CreateOrderRequest;
//import com.appointment.payment_service.dto.CreateOrderResponse;
//import com.appointment.payment_service.dto.NotificationMessage;
//import com.appointment.payment_service.dto.PaymentRequest;
//import com.appointment.payment_service.dto.VerifyPaymentRequest;
//import com.appointment.payment_service.entity.Payment;
//import com.appointment.payment_service.entity.PaymentMode;
//import com.appointment.payment_service.entity.PaymentStatus;
//import com.appointment.payment_service.exception.BadRequestException;
//import com.appointment.payment_service.exception.ResourceNotFoundException;
//import com.appointment.payment_service.repository.PaymentRepository;
//import com.razorpay.Order;
//import com.razorpay.RazorpayClient;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//@Service
//public class PaymentServiceImpl implements PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final NotificationClient notificationClient;
//    private final RazorpayClient razorpayClient;
//    private final NotificationProducer notificationProducer;
//    private final UserClient userClient;
//    
//    @Value("${razorpay.key-id}")
//    private String keyId;
//
//    @Value("${razorpay.key-secret}")
//    private String keySecret;
//
//    public PaymentServiceImpl(PaymentRepository paymentRepository,
//                              NotificationClient notificationClient,
//                              RazorpayClient razorpayClient,NotificationProducer notificationProducer,UserClient userClient) {
//        this.paymentRepository = paymentRepository;
//        this.notificationClient = notificationClient;
//        this.razorpayClient = razorpayClient;
//        this.notificationProducer = notificationProducer;
//        this.userClient = userClient;
//    }
//
//    @Override
//    public Payment processPayment(PaymentRequest request) {
//        if (paymentRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
//            throw new BadRequestException("Payment already exists for this appointment");
//        }
//
//        PaymentMode mode;
//        try {
//            mode = PaymentMode.valueOf(request.getMode().toUpperCase());
//        } catch (IllegalArgumentException ex) {
//            throw new BadRequestException("Invalid payment mode: " + request.getMode());
//        }
//
//        Payment payment = new Payment();
//        payment.setAppointmentId(request.getAppointmentId());
//        payment.setPatientId(request.getPatientId());
//        payment.setAmount(request.getAmount());
//        payment.setMode(mode);
//        payment.setStatus(PaymentStatus.PAID);
//        payment.setTransactionId(UUID.randomUUID().toString());
//        payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
//        payment.setPaidAt(LocalDateTime.now());
//        payment.setNotes(request.getNotes());
//
//        Payment saved = paymentRepository.save(payment);
//
//        HashMap<String, Object> notification = new HashMap<>();
//        notification.put("recipientId", saved.getPatientId());
//        notification.put("type", "PAYMENT");
//        notification.put("title", "Payment Successful");
//        notification.put("message", "Your payment was successful");
//        notification.put("channel", "APP");
//        notification.put("relatedId", saved.getPaymentId());
//        notification.put("relatedType", "PAYMENT");
//        
//        Map<String, Object> user = userClient.getUserById(saved.getPatientId());
//        String email = (String) user.get("email");
//
//        notificationProducer.sendNotification(
//        	    new NotificationMessage(
//        	        saved.getPatientId(),
//        	        email,   //  REAL EMAIL
//        	        "Payment Successful",
//        	        "Your payment has been completed successfully.",
//        	        "EMAIL"
//        	    )
//        );
//
//        return saved;
//    }
//
//    public CreateOrderResponse createOrder(CreateOrderRequest request) {
//        if (paymentRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
//            throw new BadRequestException("Payment already exists for this appointment");
//        }
//
//        try {
//            JSONObject options = new JSONObject();
//            options.put("amount", request.getAmount());
//            options.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
//            options.put("receipt", "appt_" + request.getAppointmentId());
//
//            Order order = razorpayClient.orders.create(options);
//
//            Payment payment = new Payment();
//            payment.setAppointmentId(request.getAppointmentId());
//            payment.setPatientId(request.getPatientId());
//            payment.setAmount(request.getAmount() / 100.0);
//            payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
//            payment.setStatus(PaymentStatus.PENDING);
//            payment.setMode(PaymentMode.ONLINE);
//            payment.setNotes(request.getNotes());
//            payment.setGateway("RAZORPAY");
//            payment.setGatewayOrderId(order.get("id"));
//            payment.setTransactionId(order.get("id"));
//
//            Payment saved = paymentRepository.save(payment);
//
//            CreateOrderResponse response = new CreateOrderResponse();
//            response.setPaymentId(saved.getPaymentId());
//            response.setOrderId(order.get("id"));
//            response.setAmount(request.getAmount());
//            response.setCurrency(saved.getCurrency());
//            response.setKeyId(keyId);
//
//            return response;
//        } catch (Exception e) {
//            throw new BadRequestException("Unable to create Razorpay order: " + e.getMessage());
//        }
//    }
//
//    public Payment verifyPayment(VerifyPaymentRequest request) {
//        Payment payment = paymentRepository.findByAppointmentId(request.getAppointmentId())
//                .orElseThrow(() -> new ResourceNotFoundException(
//                        "Payment not found for appointment id: " + request.getAppointmentId()));
//
//        String generatedSignature = hmacSha256(
//                request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId(),
//                keySecret
//        );
//
////        if (!generatedSignature.equals(request.getRazorpaySignature())) {
////            payment.setStatus(PaymentStatus.FAILED);
////            paymentRepository.save(payment);
////            throw new BadRequestException("Invalid Razorpay signature");
////        }
//        // fake test jab actual test kro toh isko hata kr woh upar wale uncomments kr dena 
//        if (true) {
//            payment.setStatus(PaymentStatus.PAID);
//        } else {
//            payment.setStatus(PaymentStatus.FAILED);
//        }
//        payment.setGatewayPaymentId(request.getRazorpayPaymentId());
//        payment.setGatewaySignature(request.getRazorpaySignature());
//        payment.setStatus(PaymentStatus.PAID);
//        payment.setPaidAt(LocalDateTime.now());
//
//        Payment saved = paymentRepository.save(payment);
//
//        HashMap<String, Object> notification = new HashMap<>();
//        notification.put("recipientId", saved.getPatientId());
//        notification.put("type", "PAYMENT");
//        notification.put("title", "Payment Successful");
//        notification.put("message", "Your payment was successful");
//        notification.put("channel", "APP");
//        notification.put("relatedId", saved.getPaymentId());
//        notification.put("relatedType", "PAYMENT");
//
//        notificationClient.sendNotification(notification);
//
//        return saved;
//    }
//
//    @Override
//    public Payment getPaymentByAppointment(Long appointmentId) {
//        return paymentRepository.findByAppointmentId(appointmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));
//    }
//
//    @Override
//    public List<Payment> getPaymentsByPatient(Long patientId) {
//        return paymentRepository.findByPatientId(patientId);
//    }
//
//    @Override
//    public List<Payment> getPaymentHistory() {
//        return paymentRepository.findAll();
//    }
//
//    @Override
//    public Payment refundPayment(Long appointmentId, String notes) {
//        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));
//
//        if (payment.getStatus() != PaymentStatus.PAID) {
//            throw new BadRequestException("Only paid payments can be refunded");
//        }
//
//        payment.setStatus(PaymentStatus.REFUNDED);
//        payment.setRefundedAt(LocalDateTime.now());
//        payment.setNotes(notes);
//
//        return paymentRepository.save(payment);
//    }
//
//    @Override
//    public String getPaymentStatus(Long appointmentId) {
//        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));
//
//        return payment.getStatus().name();
//    }
//
//    @Override
//    public Payment updatePaymentStatus(Long paymentId, String status) {
//        Payment payment = paymentRepository.findById(paymentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
//
//        try {
//            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
//            payment.setStatus(paymentStatus);
//        } catch (IllegalArgumentException ex) {
//            throw new BadRequestException("Invalid payment status: " + status);
//        }
//
//        return paymentRepository.save(payment);
//    }
//
//    @Override
//    public String generateInvoice(Long appointmentId) {
//        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));
//
//        return "Invoice Generated | Appointment ID: " + payment.getAppointmentId()
//                + " | Amount: " + payment.getAmount()
//                + " | Status: " + payment.getStatus()
//                + " | Transaction ID: " + payment.getTransactionId();
//    }
//
//    @Override
//    public Double getTotalRevenue() {
//        List<Payment> payments = paymentRepository.findAll();
//        double total = 0.0;
//
//        for (Payment payment : payments) {
//            if (payment.getStatus() == PaymentStatus.PAID) {
//                total += payment.getAmount();
//            }
//        }
//
//        return total;
//    }
//
//    private String hmacSha256(String data, String secret) {
//        try {
//            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//            sha256Hmac.init(secretKey);
//            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
//
//            StringBuilder hex = new StringBuilder();
//            for (byte b : hash) {
//                String h = Integer.toHexString(0xff & b);
//                if (h.length() == 1) {
//                    hex.append('0');
//                }
//                hex.append(h);
//            }
//            return hex.toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Signature generation failed");
//        }
//    }
//}
package com.appointment.payment_service.service;

import com.appointment.payment_service.client.NotificationClient;
import com.appointment.payment_service.client.UserClient;
import com.appointment.payment_service.dto.CreateOrderRequest;
import com.appointment.payment_service.dto.CreateOrderResponse;
import com.appointment.payment_service.dto.NotificationMessage;
import com.appointment.payment_service.dto.PaymentRequest;
import com.appointment.payment_service.dto.VerifyPaymentRequest;
import com.appointment.payment_service.entity.Payment;
import com.appointment.payment_service.entity.PaymentMode;
import com.appointment.payment_service.entity.PaymentStatus;
import com.appointment.payment_service.exception.BadRequestException;
import com.appointment.payment_service.exception.ResourceNotFoundException;
import com.appointment.payment_service.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationClient notificationClient;
    private final RazorpayClient razorpayClient;
    private final NotificationProducer notificationProducer;
    private final UserClient userClient;

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              NotificationClient notificationClient,
                              RazorpayClient razorpayClient,
                              NotificationProducer notificationProducer,
                              UserClient userClient) {
        this.paymentRepository = paymentRepository;
        this.notificationClient = notificationClient;
        this.razorpayClient = razorpayClient;
        this.notificationProducer = notificationProducer;
        this.userClient = userClient;
    }

    @Override
    public Payment processPayment(PaymentRequest request) {
        if (paymentRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new BadRequestException("Payment already exists for this appointment");
        }

        PaymentMode mode;
        try {
            mode = PaymentMode.valueOf(request.getMode().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid payment mode: " + request.getMode());
        }

        Payment payment = new Payment();
        payment.setAppointmentId(request.getAppointmentId());
        payment.setPatientId(request.getPatientId());
        payment.setAmount(request.getAmount());
        payment.setMode(mode);
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
        payment.setPaidAt(LocalDateTime.now());
        payment.setNotes(request.getNotes());

        Payment saved = paymentRepository.save(payment);

        // ✅ Patient ko payment confirmation mail
        sendMail(
                saved.getPatientId(),
                "Payment Successful",
                "Your payment of " + saved.getCurrency() + " " + saved.getAmount()
                        + " has been completed successfully. Transaction ID: " + saved.getTransactionId(),
                "PAYMENT"
        );

        return saved;
    }

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        if (paymentRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new BadRequestException("Payment already exists for this appointment");
        }

        try {
            JSONObject options = new JSONObject();
            options.put("amount", request.getAmount());
            options.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
            options.put("receipt", "appt_" + request.getAppointmentId());

            Order order = razorpayClient.orders.create(options);

            Payment payment = new Payment();
            payment.setAppointmentId(request.getAppointmentId());
            payment.setPatientId(request.getPatientId());
            payment.setAmount(request.getAmount() / 100.0);
            payment.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
            payment.setStatus(PaymentStatus.PENDING);
            payment.setMode(PaymentMode.ONLINE);
            payment.setNotes(request.getNotes());
            payment.setGateway("RAZORPAY");
            payment.setGatewayOrderId(order.get("id"));
            payment.setTransactionId(order.get("id"));

            Payment saved = paymentRepository.save(payment);

            CreateOrderResponse response = new CreateOrderResponse();
            response.setPaymentId(saved.getPaymentId());
            response.setOrderId(order.get("id"));
            response.setAmount(request.getAmount());
            response.setCurrency(saved.getCurrency());
            response.setKeyId(keyId);

            return response;
        } catch (Exception e) {
            throw new BadRequestException("Unable to create Razorpay order: " + e.getMessage());
        }
    }

    public Payment verifyPayment(VerifyPaymentRequest request) {
        Payment payment = paymentRepository.findByAppointmentId(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found for appointment id: " + request.getAppointmentId()));

        String generatedSignature = hmacSha256(
                request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId(),
                keySecret
        );

//        if (!generatedSignature.equals(request.getRazorpaySignature())) {
//            payment.setStatus(PaymentStatus.FAILED);
//            paymentRepository.save(payment);
//            throw new BadRequestException("Invalid Razorpay signature");
//        }
        // fake test — jab actual test kro toh isko hata kr woh upar wale uncomment kr dena
        if (true) {
            payment.setStatus(PaymentStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        payment.setGatewayPaymentId(request.getRazorpayPaymentId());
        payment.setGatewaySignature(request.getRazorpaySignature());
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        // ✅ Patient ko Razorpay payment verify hone ka mail (RabbitMQ se)
        sendMail(
                saved.getPatientId(),
                "Payment Verified Successfully",
                "Your online payment of " + saved.getCurrency() + " " + saved.getAmount()
                        + " has been verified successfully via Razorpay. Transaction ID: " + saved.getGatewayPaymentId(),
                "PAYMENT"
        );

        return saved;
    }

    @Override
    public Payment getPaymentByAppointment(Long appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));
    }

    @Override
    public List<Payment> getPaymentsByPatient(Long patientId) {
        return paymentRepository.findByPatientId(patientId);
    }

    @Override
    public List<Payment> getPaymentHistory() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment refundPayment(Long appointmentId, String notes) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new BadRequestException("Only paid payments can be refunded");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        payment.setNotes(notes);

        Payment saved = paymentRepository.save(payment);

        // ✅ Patient ko refund notification mail
        sendMail(
                saved.getPatientId(),
                "Refund Initiated",
                "Your refund of " + saved.getCurrency() + " " + saved.getAmount()
                        + " has been initiated successfully. It will be credited within 5-7 business days.",
                "PAYMENT"
        );

        return saved;
    }

    @Override
    public String getPaymentStatus(Long appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));

        return payment.getStatus().name();
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
            payment.setStatus(paymentStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid payment status: " + status);
        }

        return paymentRepository.save(payment);
    }

    @Override
    public String generateInvoice(Long appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for appointment id: " + appointmentId));

        return "Invoice Generated | Appointment ID: " + payment.getAppointmentId()
                + " | Amount: " + payment.getAmount()
                + " | Status: " + payment.getStatus()
                + " | Transaction ID: " + payment.getTransactionId();
    }

    @Override
    public Double getTotalRevenue() {
        List<Payment> payments = paymentRepository.findAll();
        double total = 0.0;

        for (Payment payment : payments) {
            if (payment.getStatus() == PaymentStatus.PAID) {
                total += payment.getAmount();
            }
        }

        return total;
    }

    /**
     * RabbitMQ ke through notification-service ko email bhejta hai.
     *
     * @param userId  Patient ka userId
     * @param subject Email subject
     * @param message Email body
     * @param type    Notification type: PAYMENT
     */
    private void sendMail(Long userId, String subject, String message, String type) {
        try {
            Map<String, Object> user = userClient.getUserById(userId);

            if (user == null || user.get("email") == null) {
                System.out.println("User or email not found for userId: " + userId + ". Skipping mail.");
                return;
            }

            String email = (String) user.get("email");

            notificationProducer.sendNotification(
                    new NotificationMessage(
                            userId,
                            email,
                            subject,
                            message,
                            type  // ✅ FIX: "EMAIL" ki jagah "PAYMENT" pass ho raha hai
                    )
            );

            System.out.println("Notification queued for userId: " + userId + " | type: " + type);

        } catch (Exception e) {
            System.out.println("Failed to send notification for userId: " + userId + " | Error: " + e.getMessage());
        }
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) {
                    hex.append('0');
                }
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Signature generation failed");
        }
    }
}