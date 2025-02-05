package com.dentsu.bootcamp.scheduler;

import com.dentsu.bootcamp.model.ReservationEntity;
import com.dentsu.bootcamp.repository.ReservationRepository;
import com.dentsu.bootcamp.service.EmailService;
import com.dentsu.bootcamp.service.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationScheduler {

    private ReservationService reservationService;
    private EmailService emailService;
    private String CHECKIN_EMAIL_TITLE = "Reservation ready for Check-in";
    private String CHECKIN_EMAIL_STATUS = "Your reservation is available for Check-in. Please check the details below.";
    private ReservationRepository reservationRepository;

    public ReservationScheduler(ReservationService reservationService, EmailService emailService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.emailService = emailService;
        this.reservationRepository = reservationRepository;
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void checkReservations(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInDateTime = now.plusDays(1);
        LocalDate checkInDate= checkInDateTime.toLocalDate();

        List<ReservationEntity> reservations = reservationService.findByPickupDate(checkInDate);

        for (ReservationEntity reservation : reservations) {
            if(!reservation.isCheckInEmailSent()){
                emailService.sendMail(reservation.getEmail(), CHECKIN_EMAIL_TITLE, reservation.getConfirmationNumber(), reservation.getFirstName(), reservation. getLastName(), CHECKIN_EMAIL_STATUS);
                reservation.setCheckInEmailSent(true);
                reservationRepository.save(reservation);
            }
        }
    }
}
