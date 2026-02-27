package com.rodmag.youtube_premium_billing_bot.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BillingScheduleService {

    private final BillingService billingService;

    public BillingScheduleService(BillingService billingService) {
        this.billingService = billingService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression means "At 00:00:00am every day"
    public void processDailyBilling() {
        billingService.generateMonthlyBillings();

    }
}
