
package com.app.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String email, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("RentGrid - Password Reset");
        message.setText(
            "Hello,\n\n"
            + "We received a request to reset your RentGrid password.\n\n"
            + "Click the link below to reset your password:\n"
            + resetLink
            + "\n\nThis link will expire in 15 minutes.\n\n"
            + "If you did not request a password reset, please ignore this email.\n\n"
            + "Regards,\nRentGrid Team"
        );

        mailSender.send(message);
    }
}
