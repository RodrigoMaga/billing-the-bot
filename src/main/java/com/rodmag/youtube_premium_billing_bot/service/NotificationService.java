package com.rodmag.youtube_premium_billing_bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void emailNotification(String email, String subject, String message) {
        emailService.sendEmail(email, subject, message);
    }

    @Async
    public void whatsAppNotification(String phoneNumber, String message) {
        log.info("Sending WhatsApp notification to {}: {}", phoneNumber, message);
    }

    @Async
    public void telegramNotification(String phoneNumber, String message) {
        log.info("Sending Telegram notification to chat ID {}: {}", phoneNumber, message);
    }
}