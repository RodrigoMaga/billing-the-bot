package com.rodmag.youtube_premium_billing_bot.service;

import com.rodmag.youtube_premium_billing_bot.entity.Payment;
import com.rodmag.youtube_premium_billing_bot.entity.enums.PaymentStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class BillingEmailComposer {

    private final TemplateEngine templateEngine;
    private final EmailService emailService;


    public BillingEmailComposer(TemplateEngine templateEngine, EmailService emailService) {
        this.templateEngine = templateEngine;
        this.emailService = emailService;
    }

        public void composeAndSendBillingEmail(String email, String subject,
                                               String participantName, Integer paymentMonth,
                                               Integer paymentYear, PaymentStatus paymentStatus) {
            Context context = new Context();
            context.setVariable("participantName", participantName.split(" ")[0]);
            context.setVariable("paymentMonth", paymentMonth);
            context.setVariable("paymentYear", paymentYear);
            context.setVariable("paymentStatus", paymentStatus.getDisplayName());
            context.setVariable("paymentValue", String.format("%.2f", Payment.PAYMENT_VALUE));
            String htmlContent = templateEngine.process("billing-notification", context);
            emailService.sendHtmlEmail(email, subject, htmlContent);
        }
}
