package com.aruiz.user.notification.service.impl;

import com.aruiz.user.notification.controller.dto.UserResponse;
import com.aruiz.user.notification.entity.NotificationEntity;
import com.aruiz.user.notification.repository.NotificationRepository;
import com.aruiz.user.notification.service.NotificationService;
import com.aruiz.user.notification.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public void sendNotification(String message, String ownerEmail) {
        kafkaTemplate.send(TOPIC, message);

        // Guardar la notificación en MongoDB
        NotificationEntity notification = new NotificationEntity();
        notification.setMessage(message);
        notification.setEmail(ownerEmail);
        notificationRepository.save(notification);

        // Enviar correo electrónico al propietario
        sendEmail(ownerEmail, message);

    }

    @Override
    public void sendEmail(String destinationEmail, String message) {

        try {

            UserResponse userResponse = userService.findByEmail(destinationEmail);

            if (userResponse.getEmail() != null) {
                SimpleMailMessage messageObj = new SimpleMailMessage();
                messageObj.setFrom("your-email@gmail.com");
                messageObj.setTo(destinationEmail);
                messageObj.setSubject("Notificación Importante");
                messageObj.setText(String.valueOf(message));
                javaMailSender.send(messageObj);
            } else {
                log.error("User email not found {}", destinationEmail);
            }

        } catch (MailException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
