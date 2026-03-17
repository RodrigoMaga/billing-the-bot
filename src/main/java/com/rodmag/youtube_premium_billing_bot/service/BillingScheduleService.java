package com.rodmag.youtube_premium_billing_bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BillingScheduleService {

    private static final Logger log = LoggerFactory.getLogger(BillingScheduleService.class);

    private final BillingService billingService;

    public BillingScheduleService(BillingService billingService) {
        this.billingService = billingService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression means "At 00:00:00am every day"
    public void processDailyBilling() {
        log.info("Starting daily billing process...");
        billingService.generateMonthlyBillings();
        billingService.dailyBillingCheck();
        log.info("Daily billing process completed.");
    }
}