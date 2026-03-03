package com.rodmag.youtube_premium_billing_bot.services;

import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.entities.enums.PaymentStatus;
import com.rodmag.youtube_premium_billing_bot.exceptions.ParticipantNotFoundException;
import com.rodmag.youtube_premium_billing_bot.repositories.ParticipantRepository;
import com.rodmag.youtube_premium_billing_bot.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);

    private final ParticipantRepository participantRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    public BillingService(ParticipantRepository participantRepository, PaymentRepository paymentRepository, NotificationService notificationService) {
        this.participantRepository = participantRepository;
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void generateMonthlyBillings() {
        log.info("Checking if monthly billing generation is needed...");

        LocalDate today = LocalDate.now();
        Integer currentDay = today.getDayOfMonth();
        Integer currentMonth = today.getMonthValue();
        Integer currentYear = today.getYear();

        if (currentDay != 1) {
            log.debug("Not the 1st day of the month (current day: {}). Skipping monthly billing generation.", currentDay);
            return;
        }

        log.info("Generating monthly billing for {}/{}", currentMonth, currentYear);

        Optional<Payment> lastPayment = paymentRepository.findTopByOrderByIdDesc();
        List<Participant> participants = participantRepository.findAll();

        Payment newPayment = new Payment();
        newPayment.setMonth(currentMonth);
        newPayment.setYear(currentYear);
        newPayment.setPaymentStatus(PaymentStatus.NOT_PAID);

        if (lastPayment.isEmpty()) {
            log.info("No previous payments found. Starting with billing order 1.");
            newPayment.setParticipant(participants.stream()
                    .min(Comparator.comparingInt(Participant::getBillingOrder))
                    .orElseThrow(() -> new ParticipantNotFoundException("No participant found")));
        } else {
            Integer lastParticipantOrder = lastPayment.get().getParticipant().getBillingOrder();
            Integer nextParticipantOrder;
            if (lastParticipantOrder.equals(participants.size())) {
                nextParticipantOrder = 1;
                log.info("Cycling back to billing order 1 (last order was {})", lastParticipantOrder);
            } else {
                nextParticipantOrder = lastParticipantOrder + 1;
                log.info("Moving to next billing order: {} (previous was {})", nextParticipantOrder, lastParticipantOrder);
            }
            newPayment.setMonth(currentMonth);
            newPayment.setYear(currentYear);
            newPayment.setPaymentStatus(PaymentStatus.NOT_PAID);
            newPayment.setParticipant(participants.stream()
                    .filter(p -> p.getBillingOrder().equals(nextParticipantOrder))
                    .findFirst()
                    .orElseThrow(() -> new ParticipantNotFoundException("No participant found")));
        }
        paymentRepository.save(newPayment);
        log.info("Monthly billing created for participant: {} (Billing Order: {})",
                newPayment.getParticipant().getName(), newPayment.getParticipant().getBillingOrder());
    }

    @Transactional
    public void dailyBillingCheck() {
        log.info("Starting daily billing check for pending payments...");

        List<Payment> pendingPayments = paymentRepository.findAllByPaymentStatus(PaymentStatus.NOT_PAID);

        if (pendingPayments.isEmpty()) {
            log.info("No pending payments found. No notifications needed.");
            return;
        }

        log.info("Found {} pending payment(s). Preparing notifications...", pendingPayments.size());

        List<Participant> participantsToNotify = pendingPayments.stream()
                .map(Payment::getParticipant)
                .distinct()
                .toList();

        log.info("Notifying {} unique participant(s) about pending payments.", participantsToNotify.size());

        participantsToNotify.forEach(participant -> {
            log.info("Sending email notification to: {} ({})", participant.getName(), participant.getEmail());
            notificationService.emailNotification(participant.getEmail(), "A wild senhor barriga appeared!",
                    participant.getName() + " is being notified about the pending payment.");

            log.info("Sending WhatsApp notification to: {} ({})", participant.getName(), participant.getPhone());
            notificationService.whatsAppNotification(participant.getPhone(), "A wild senhor barriga appeared!" +
                    participant.getName() + " is being notified about the pending payment.");

            log.info("Sending Telegram notification to: {} ({})", participant.getName(), participant.getPhone());
            notificationService.telegramNotification(participant.getPhone(), "A wild senhor barriga appeared!" +
                    participant.getName() + " is being notified about the pending payment.");
        });

        log.info("Daily billing check completed.");
    }
}