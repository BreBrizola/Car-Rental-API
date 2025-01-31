package com.dentsu.bootcamp.scheduler;

import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.service.EmailService;
import com.dentsu.bootcamp.service.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ReservationScheduler {

    private ReservationService reservationService;
    private EmailService emailService;
    private String CHECKIN_EMAIL_TITLE = "Reservation ready for Check-in";
    private String CHECKIN_EMAIL_STATUS = "Your reservation is available for Check-in. Please check the details below.";

    public ReservationScheduler(ReservationService reservationService, EmailService emailService) {
        this.reservationService = reservationService;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkReservations(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInTime = now.plus(24, ChronoUnit.HOURS);

        List<ReservationEntity> reservations = reservationService.findByPickupDate(checkInTime);

        for (ReservationEntity reservation : reservations) {
            emailService.sendMail(reservation.getEmail(), CHECKIN_EMAIL_TITLE, reservation.getConfirmationNumber(), reservation.getFirstName(), reservation. getLastName(), CHECKIN_EMAIL_STATUS);
        }
    }
}
