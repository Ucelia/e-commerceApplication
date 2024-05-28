package com.example.ecommerceapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

//    public void sendEmail(String recipient, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("ecommerce001@gmail.com");
//        message.setTo(recipient);
//        message.setSubject(subject);
//        message.setText(text);
//        emailSender.send(message);
//    }
}
