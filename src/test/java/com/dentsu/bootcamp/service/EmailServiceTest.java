package com.dentsu.bootcamp.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

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
}