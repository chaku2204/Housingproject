package com.example.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {


    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordEmail(String to, String name, String rawPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your account has been created");
        message.setText("Hi " + name + ",\n\nYour account has been created.\nYour password is: " + rawPassword +

                "\n\nPlease log in and change your password.\n\nRegards,\nAdmin");

        mailSender.send(message);
    }
}