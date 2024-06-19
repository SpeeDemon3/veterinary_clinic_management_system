package com.aruiz.user.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class for setting up JavaMailSender bean.
 * This configuration sets up the properties needed to send emails using Gmail's SMTP server.
 */
@Configuration
public class MailConfig {

    /**
     * Configures and provides a JavaMailSender bean.
     *
     * This method sets up a JavaMailSenderImpl with the necessary SMTP properties to connect to Gmail's SMTP server.
     * The properties include the host, port, username, and password for the email account, as well as various SMTP settings.
     *
     * @return A configured JavaMailSender bean.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Set mail properties
        mailSender.setUsername("your-email@gmail.com");
        mailSender.setPassword("your-email-password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;

    }

}
