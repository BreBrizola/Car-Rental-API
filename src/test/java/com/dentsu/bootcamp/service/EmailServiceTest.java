package com.dentsu.bootcamp.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void givenCreateReservation_whenNewReservationCreated_thenSendEmail() throws Exception {
        String to = "test@example.com";
        String subject = "Reservation confirmed";
        String reservationNumber = "12345678";
        String firstName = "Name";
        String lastName = "Last Name";
        String reservationStatus = "Confirmed";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendMail(to, subject, reservationNumber, firstName, lastName, reservationStatus);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void givenCancelReservation_whenReservationCancelled_thenSendCancellingEmail() throws Exception {
        String to = "test@example.com";
        String subject = "Reservation cancelled";
        String reservationNumber = "87654321";
        String firstName = "Name";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendCancellingEmail(to, subject, reservationNumber, firstName);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void givenMailSendFails_whenSendMail_thenHandleException() throws Exception {
        String to = "test@example.com";
        String subject = "Reservation confirmed";
        String reservationNumber = "12345678";
        String firstName = "Name";
        String lastName = "Last Name";
        String reservationStatus = "Confirmed";

        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Error to send email"));

        assertDoesNotThrow(() ->
                emailService.sendMail(to, subject, reservationNumber, firstName, lastName, reservationStatus)
        );

        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }
}