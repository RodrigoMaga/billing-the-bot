package com.rodmag.billing_the_bot.service;

import com.rodmag.billing_the_bot.entity.enums.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final BillingEmailComposer billingEmailComposer;

    public NotificationService(BillingEmailComposer billingEmailComposer) {
        this.billingEmailComposer = billingEmailComposer;
    }

    @Async
    public void emailNotification(String email, String subject,
                                  String participantName, Integer paymentMonth,
                                  Integer paymentYear, String paymentStatus) {
        billingEmailComposer.composeAndSendBillingEmail(email, subject, participantName, paymentMonth, paymentYear, PaymentStatus.valueOf(paymentStatus));
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