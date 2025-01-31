package com.dentsu.bootcamp.service;

import com.dentsu.bootcamp.model.ReservationEntity;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    public static final String FROM_MAIL = "CarRentalProjectTest@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String reservationNumber,
                         String firstName,String lastName, String reservationStatus) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(FROM_MAIL);
            helper.setTo(to);
            helper.setSubject(subject);

            String template = loadTemplateEmail();

            String linkReserva = "http://localhost:8090/reservation/retrieve?confirmationNumber="
                    + reservationNumber
                    + "&firstName=" + firstName
                    + "&lastName=" + lastName;

            template = template.replace("#{Name}", firstName);
            template = template.replace("#{ReservationStatus}", reservationStatus);
            template = template.replace("#{ReservationNumber}", reservationNumber);
            template = template.replace("#{Title}", subject);
            template = template.replace("#{LinkReserva}", linkReserva);

            helper.setText(template, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println("Failed to send email");
        }
    }

    public void sendCancellingEmail(String to, String subject,String reservationNumber, String firstName){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(FROM_MAIL);
            helper.setTo(to);
            helper.setSubject(subject);

            String template = loadTemplateCancellingEmail();

            template = template.replace("#{Name}", firstName);
            template = template.replace("#{Title}", subject);
            template = template.replace("#{ReservationNumber}", reservationNumber);

            helper.setText(template, true);
            mailSender.send(message);

        } catch (Exception exception) {
            System.out.println("Failed to send email");
        }

    }

    public String loadTemplateEmail() throws IOException{
        ClassPathResource resource = new ClassPathResource("templates/template-email.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String loadTemplateCancellingEmail() throws IOException{
        ClassPathResource resource = new ClassPathResource("templates/template-cancelling-email.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
