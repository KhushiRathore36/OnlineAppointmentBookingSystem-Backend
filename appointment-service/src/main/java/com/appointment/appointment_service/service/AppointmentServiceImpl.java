//package com.appointment.appointment_service.service;
//
//import com.appointment.appointment_service.client.NotificationClient;
//import com.appointment.appointment_service.client.ProviderClient;
//import com.appointment.appointment_service.client.ScheduleServiceClient;
//import com.appointment.appointment_service.client.UserClient;
//import com.appointment.appointment_service.dto.AppointmentRequest;
//import com.appointment.appointment_service.dto.NotificationMessage;
//import com.appointment.appointment_service.dto.RescheduleRequest;
//import com.appointment.appointment_service.dto.SlotResponse;
//import com.appointment.appointment_service.entity.Appointment;
//import com.appointment.appointment_service.entity.AppointmentStatus;
//import com.appointment.appointment_service.exception.BadRequestException;
//import com.appointment.appointment_service.exception.ResourceNotFoundException;
//import com.appointment.appointment_service.repository.AppointmentRepository;
//
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//public class AppointmentServiceImpl implements AppointmentService {
//
//    private final AppointmentRepository appointmentRepository;
//    private final NotificationClient notificationClient;
//    private final ScheduleServiceClient scheduleServiceClient;
//    private final NotificationProducer notificationProducer;
//    private final UserClient userClient;
//    private final ProviderClient providerClient;
//
//    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
//                                  NotificationClient notificationClient,
//                                  ScheduleServiceClient scheduleServiceClient,
//                                  NotificationProducer notificationProducer,
//                                  UserClient userClient,
//                                  ProviderClient providerClient) {
//        this.appointmentRepository = appointmentRepository;
//        this.notificationClient = notificationClient;
//        this.scheduleServiceClient = scheduleServiceClient;
//        this.notificationProducer = notificationProducer;
//        this.userClient = userClient;
//        this.providerClient = providerClient;
//    }
//
//    @Override
//    public Appointment bookAppointment(AppointmentRequest request) {
//        Optional<Appointment> existing = appointmentRepository.findBySlotId(request.getSlotId());
//
//        if (existing.isPresent() && existing.get().getStatus() == AppointmentStatus.SCHEDULED) {
//            throw new BadRequestException("This slot is already booked");
//        }
//
//        SlotResponse slot = scheduleServiceClient.getSlotById(request.getSlotId());
//
//        if (slot == null) {
//            throw new ResourceNotFoundException("Slot not found");
//        }
//
//        if (!slot.getProviderId().equals(request.getProviderId())) {
//            throw new BadRequestException("Slot does not belong to the given provider");
//        }
//
//        if (!"AVAILABLE".equalsIgnoreCase(slot.getStatus())) {
//            throw new BadRequestException("Slot is not available for booking");
//        }
//
//        scheduleServiceClient.bookSlot(request.getSlotId());
//
//        Appointment appointment = new Appointment();
//        appointment.setPatientId(request.getPatientId());
//        appointment.setProviderId(request.getProviderId());
//        appointment.setSlotId(request.getSlotId());
//        appointment.setServiceType(request.getServiceType());
//        appointment.setAppointmentDate(slot.getDate());
//        appointment.setStartTime(slot.getStartTime());
//        appointment.setEndTime(slot.getEndTime());
//        appointment.setStatus(AppointmentStatus.SCHEDULED);
//        appointment.setNotes(request.getNotes());
//        appointment.setModeOfConsultation(request.getModeOfConsultation());
//
//        Appointment saved = appointmentRepository.save(appointment);
//
//        sendMail(
//                saved.getPatientId(),
//                "Appointment Booked",
//                "Your appointment has been booked successfully."
//        );
//
//        Long doctorUserId = getDoctorUserId(saved.getProviderId());
//
//        if (doctorUserId != null) {
//            sendMail(
//                    doctorUserId,
//                    "New Appointment Booked",
//                    "A new appointment has been booked with you."
//            );
//        }
//
//        return saved;
//    }
//
//    @Override
//    public Appointment getById(Long id) {
//        return appointmentRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
//    }
//
//    @Override
//    public List<Appointment> getByPatient(Long patientId) {
//        return appointmentRepository.findByPatientId(patientId);
//    }
//
//    @Override
//    public List<Appointment> getByProvider(Long providerId) {
//        return appointmentRepository.findByProviderId(providerId);
//    }
//
//    @Override
//    public List<Appointment> getByProviderAndDate(Long providerId, LocalDate date) {
//        return appointmentRepository.findByProviderIdAndAppointmentDate(providerId, date);
//    }
//
//    @Override
//    public Appointment cancelAppointment(Long id) {
//        Appointment appointment = getById(id);
//
//        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
//            throw new BadRequestException("Appointment is already cancelled");
//        }
//
//        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
//            throw new BadRequestException("Completed appointment cannot be cancelled");
//        }
//
//        scheduleServiceClient.releaseSlot(appointment.getSlotId());
//
//        appointment.setStatus(AppointmentStatus.CANCELLED);
//
//        Appointment saved = appointmentRepository.save(appointment);
//
//        sendMail(
//                saved.getPatientId(),
//                "Appointment Cancelled",
//                "Your appointment has been cancelled."
//        );
//
//        Long doctorUserId = getDoctorUserId(saved.getProviderId());
//
//        if (doctorUserId != null) {
//            sendMail(
//                    doctorUserId,
//                    "Appointment Cancelled",
//                    "An appointment with you has been cancelled."
//            );
//        }
//
//        return saved;
//    }
//
//    @Override
//    public Appointment rescheduleAppointment(Long id, RescheduleRequest request) {
//        Appointment appointment = getById(id);
//
//        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
//            throw new BadRequestException("Only scheduled appointments can be rescheduled");
//        }
//
//        SlotResponse newSlot = scheduleServiceClient.getSlotById(request.getNewSlotId());
//
//        if (newSlot == null) {
//            throw new ResourceNotFoundException("New slot not found");
//        }
//
//        if (!newSlot.getProviderId().equals(appointment.getProviderId())) {
//            throw new BadRequestException("New slot must belong to same provider");
//        }
//
//        if (!"AVAILABLE".equalsIgnoreCase(newSlot.getStatus())) {
//            throw new BadRequestException("New slot is not available");
//        }
//
//        scheduleServiceClient.releaseSlot(appointment.getSlotId());
//        scheduleServiceClient.bookSlot(request.getNewSlotId());
//
//        appointment.setSlotId(request.getNewSlotId());
//        appointment.setAppointmentDate(newSlot.getDate());
//        appointment.setStartTime(newSlot.getStartTime());
//        appointment.setEndTime(newSlot.getEndTime());
//
//        Appointment saved = appointmentRepository.save(appointment);
//
//        sendMail(
//                saved.getPatientId(),
//                "Appointment Rescheduled",
//                "Your appointment has been rescheduled."
//        );
//
//        Long doctorUserId = getDoctorUserId(saved.getProviderId());
//
//        if (doctorUserId != null) {
//            sendMail(
//                    doctorUserId,
//                    "Appointment Rescheduled",
//                    "An appointment with you has been rescheduled."
//            );
//        }
//
//        return saved;
//    }
//
//    @Override
//    public Appointment completeAppointment(Long id) {
//        Appointment appointment = getById(id);
//
//        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
//            throw new BadRequestException("Only scheduled appointments can be completed");
//        }
//
//        appointment.setStatus(AppointmentStatus.COMPLETED);
//
//        Appointment saved = appointmentRepository.save(appointment);
//
//        sendMail(
//                saved.getPatientId(),
//                "Appointment Completed",
//                "Your appointment has been marked as completed."
//        );
//
//        Long doctorUserId = getDoctorUserId(saved.getProviderId());
//
//        if (doctorUserId != null) {
//            sendMail(
//                    doctorUserId,
//                    "Appointment Completed",
//                    "An appointment with you has been marked as completed."
//            );
//        }
//
//        return saved;
//    }
//
//    @Override
//    public Appointment updateStatus(Long id, String status) {
//        Appointment appointment = getById(id);
//
//        try {
//            AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
//            appointment.setStatus(appointmentStatus);
//        } catch (IllegalArgumentException ex) {
//            throw new BadRequestException("Invalid status: " + status);
//        }
//
//        Appointment saved = appointmentRepository.save(appointment);
//
//        sendMail(
//                saved.getPatientId(),
//                "Appointment Status Updated",
//                "Your appointment status has been updated to " + saved.getStatus()
//        );
//
//        Long doctorUserId = getDoctorUserId(saved.getProviderId());
//
//        if (doctorUserId != null) {
//            sendMail(
//                    doctorUserId,
//                    "Appointment Status Updated",
//                    "Appointment status has been updated to " + saved.getStatus()
//            );
//        }
//
//        return saved;
//    }
//
//    @Override
//    public List<Appointment> getUpcomingByPatient(Long patientId) {
//        return appointmentRepository.findByPatientIdAndAppointmentDateAfter(patientId, LocalDate.now());
//    }
//
//    @Override
//    public long getAppointmentCount(Long providerId) {
//        return appointmentRepository.countByProviderId(providerId);
//    }
//
//    private void sendMail(Long userId, String subject, String message) {
//        Map<String, Object> user = userClient.getUserById(userId);
//
//        if (user == null || user.get("email") == null) {
//            return;
//        }
//
//        String email = (String) user.get("email");
//
//        notificationProducer.sendNotification(
//                new NotificationMessage(
//                        userId,
//                        email,
//                        subject,
//                        message,
//                        "EMAIL"
//                )
//        );
//    }
//
//    private Long getDoctorUserId(Long providerId) {
//        Map<String, Object> provider = providerClient.getProviderById(providerId);
//
//        if (provider == null || provider.get("userId") == null) {
//            return null;
//        }
//
//        Object userIdObj = provider.get("userId");
//
//        if (userIdObj instanceof Integer) {
//            return ((Integer) userIdObj).longValue();
//        }
//
//        if (userIdObj instanceof Long) {
//            return (Long) userIdObj;
//        }
//
//        return Long.valueOf(userIdObj.toString());
//    }
//}
package com.appointment.appointment_service.service;

import com.appointment.appointment_service.client.NotificationClient;
import com.appointment.appointment_service.client.ProviderClient;
import com.appointment.appointment_service.client.ScheduleServiceClient;
import com.appointment.appointment_service.client.UserClient;
import com.appointment.appointment_service.dto.AppointmentRequest;
import com.appointment.appointment_service.dto.NotificationMessage;
import com.appointment.appointment_service.dto.RescheduleRequest;
import com.appointment.appointment_service.dto.SlotResponse;
import com.appointment.appointment_service.entity.Appointment;
import com.appointment.appointment_service.entity.AppointmentStatus;
import com.appointment.appointment_service.exception.BadRequestException;
import com.appointment.appointment_service.exception.ResourceNotFoundException;
import com.appointment.appointment_service.repository.AppointmentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationClient notificationClient;
    private final ScheduleServiceClient scheduleServiceClient;
    private final NotificationProducer notificationProducer;
    private final UserClient userClient;
    private final ProviderClient providerClient;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  NotificationClient notificationClient,
                                  ScheduleServiceClient scheduleServiceClient,
                                  NotificationProducer notificationProducer,
                                  UserClient userClient,
                                  ProviderClient providerClient) {
        this.appointmentRepository = appointmentRepository;
        this.notificationClient = notificationClient;
        this.scheduleServiceClient = scheduleServiceClient;
        this.notificationProducer = notificationProducer;
        this.userClient = userClient;
        this.providerClient = providerClient;
    }

    @Override
    public Appointment bookAppointment(AppointmentRequest request) {
        Optional<Appointment> existing = appointmentRepository.findBySlotId(request.getSlotId());

        if (existing.isPresent() && existing.get().getStatus() == AppointmentStatus.SCHEDULED) {
            throw new BadRequestException("This slot is already booked");
        }

        SlotResponse slot = scheduleServiceClient.getSlotById(request.getSlotId());

        if (slot == null) {
            throw new ResourceNotFoundException("Slot not found");
        }

        if (!slot.getProviderId().equals(request.getProviderId())) {
            throw new BadRequestException("Slot does not belong to the given provider");
        }

        if (!"AVAILABLE".equalsIgnoreCase(slot.getStatus())) {
            throw new BadRequestException("Slot is not available for booking");
        }

        scheduleServiceClient.bookSlot(request.getSlotId());

        Appointment appointment = new Appointment();
        appointment.setPatientId(request.getPatientId());
        appointment.setProviderId(request.getProviderId());
        appointment.setSlotId(request.getSlotId());
        appointment.setServiceType(request.getServiceType());
        appointment.setAppointmentDate(slot.getDate());
        appointment.setStartTime(slot.getStartTime());
        appointment.setEndTime(slot.getEndTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes(request.getNotes());
        appointment.setModeOfConsultation(request.getModeOfConsultation());

        Appointment saved = appointmentRepository.save(appointment);

        // Patient ko booking confirmation mail
        sendMail(
                saved.getPatientId(),
                "Appointment Booked",
                "Your appointment has been booked successfully for " + saved.getAppointmentDate()
                        + " from " + saved.getStartTime() + " to " + saved.getEndTime() + ".",
                "BOOKING"
        );

        // Doctor ko naya appointment notification
        Long doctorUserId = getDoctorUserId(saved.getProviderId());
        if (doctorUserId != null) {
            sendMail(
                    doctorUserId,
                    "New Appointment Booked",
                    "A new appointment has been booked with you for " + saved.getAppointmentDate()
                            + " from " + saved.getStartTime() + " to " + saved.getEndTime() + ".",
                    "BOOKING"
            );
        }

        return saved;
    }

    @Override
    public Appointment getById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    public List<Appointment> getByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    public List<Appointment> getByProvider(Long providerId) {
        return appointmentRepository.findByProviderId(providerId);
    }

    @Override
    public List<Appointment> getByProviderAndDate(Long providerId, LocalDate date) {
        return appointmentRepository.findByProviderIdAndAppointmentDate(providerId, date);
    }

    @Override
    public Appointment cancelAppointment(Long id) {
        Appointment appointment = getById(id);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Appointment is already cancelled");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Completed appointment cannot be cancelled");
        }

        scheduleServiceClient.releaseSlot(appointment.getSlotId());

        appointment.setStatus(AppointmentStatus.CANCELLED);

        Appointment saved = appointmentRepository.save(appointment);

        // Patient ko cancellation mail
        sendMail(
                saved.getPatientId(),
                "Appointment Cancelled",
                "Your appointment scheduled for " + saved.getAppointmentDate()
                        + " from " + saved.getStartTime() + " to " + saved.getEndTime()
                        + " has been cancelled.",
                "CANCELLATION"
        );

        // Doctor ko cancellation mail
        Long doctorUserId = getDoctorUserId(saved.getProviderId());
        if (doctorUserId != null) {
            sendMail(
                    doctorUserId,
                    "Appointment Cancelled",
                    "An appointment scheduled for " + saved.getAppointmentDate()
                            + " from " + saved.getStartTime() + " to " + saved.getEndTime()
                            + " has been cancelled by the patient.",
                    "CANCELLATION"
            );
        }

        return saved;
    }

    @Override
    public Appointment rescheduleAppointment(Long id, RescheduleRequest request) {
        Appointment appointment = getById(id);

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BadRequestException("Only scheduled appointments can be rescheduled");
        }

        SlotResponse newSlot = scheduleServiceClient.getSlotById(request.getNewSlotId());

        if (newSlot == null) {
            throw new ResourceNotFoundException("New slot not found");
        }

        if (!newSlot.getProviderId().equals(appointment.getProviderId())) {
            throw new BadRequestException("New slot must belong to same provider");
        }

        if (!"AVAILABLE".equalsIgnoreCase(newSlot.getStatus())) {
            throw new BadRequestException("New slot is not available");
        }

        scheduleServiceClient.releaseSlot(appointment.getSlotId());
        scheduleServiceClient.bookSlot(request.getNewSlotId());

        appointment.setSlotId(request.getNewSlotId());
        appointment.setAppointmentDate(newSlot.getDate());
        appointment.setStartTime(newSlot.getStartTime());
        appointment.setEndTime(newSlot.getEndTime());

        Appointment saved = appointmentRepository.save(appointment);

        // Patient ko reschedule mail
        sendMail(
                saved.getPatientId(),
                "Appointment Rescheduled",
                "Your appointment has been rescheduled to " + saved.getAppointmentDate()
                        + " from " + saved.getStartTime() + " to " + saved.getEndTime() + ".",
                "RESCHEDULE"
        );

        // Doctor ko reschedule mail
        Long doctorUserId = getDoctorUserId(saved.getProviderId());
        if (doctorUserId != null) {
            sendMail(
                    doctorUserId,
                    "Appointment Rescheduled",
                    "An appointment has been rescheduled to " + saved.getAppointmentDate()
                            + " from " + saved.getStartTime() + " to " + saved.getEndTime() + ".",
                    "RESCHEDULE"
            );
        }

        return saved;
    }

    @Override
    public Appointment completeAppointment(Long id) {
        Appointment appointment = getById(id);

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BadRequestException("Only scheduled appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        Appointment saved = appointmentRepository.save(appointment);

        // Patient ko completion mail
        sendMail(
                saved.getPatientId(),
                "Appointment Completed",
                "Your appointment on " + saved.getAppointmentDate()
                        + " has been marked as completed. Thank you for visiting!",
                "COMPLETED"
        );

        // Doctor ko completion mail
        Long doctorUserId = getDoctorUserId(saved.getProviderId());
        if (doctorUserId != null) {
            sendMail(
                    doctorUserId,
                    "Appointment Completed",
                    "The appointment on " + saved.getAppointmentDate()
                            + " has been marked as completed.",
                    "COMPLETED"
            );
        }

        return saved;
    }

    @Override
    public Appointment updateStatus(Long id, String status) {
        Appointment appointment = getById(id);

        try {
            AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
            appointment.setStatus(appointmentStatus);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid status: " + status);
        }

        Appointment saved = appointmentRepository.save(appointment);

        sendMail(
                saved.getPatientId(),
                "Appointment Status Updated",
                "Your appointment status has been updated to " + saved.getStatus(),
                saved.getStatus().name()
        );

        Long doctorUserId = getDoctorUserId(saved.getProviderId());
        if (doctorUserId != null) {
            sendMail(
                    doctorUserId,
                    "Appointment Status Updated",
                    "Appointment status has been updated to " + saved.getStatus(),
                    saved.getStatus().name()
            );
        }

        return saved;
    }

    @Override
    public List<Appointment> getUpcomingByPatient(Long patientId) {
        return appointmentRepository.findByPatientIdAndAppointmentDateAfter(patientId, LocalDate.now());
    }

    @Override
    public long getAppointmentCount(Long providerId) {
        return appointmentRepository.countByProviderId(providerId);
    }

    /**
     * RabbitMQ ke through notification-service ko email bhejta hai.
     *
     * @param userId   Patient ya Doctor ka userId (user-service mein)
     * @param subject  Email ka subject
     * @param message  Email ka body
     * @param type     Notification type: BOOKING, CANCELLATION, COMPLETED, RESCHEDULE etc.
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
                            type   // ✅ FIX: "EMAIL" ki jagah actual type pass ho raha hai
                    )
            );

            System.out.println("Notification queued for userId: " + userId + " | type: " + type);

        } catch (Exception e) {
            System.out.println("Failed to send notification for userId: " + userId + " | Error: " + e.getMessage());
        }
    }

    private Long getDoctorUserId(Long providerId) {
        try {
            Map<String, Object> provider = providerClient.getProviderById(providerId);

            if (provider == null || provider.get("userId") == null) {
                return null;
            }

            Object userIdObj = provider.get("userId");

            if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            }

            if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            }

            return Long.valueOf(userIdObj.toString());

        } catch (Exception e) {
            System.out.println("Failed to get doctor userId for providerId: " + providerId + " | Error: " + e.getMessage());
            return null;
        }
    }
}