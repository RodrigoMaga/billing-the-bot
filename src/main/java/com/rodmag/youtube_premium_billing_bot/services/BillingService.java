package com.rodmag.youtube_premium_billing_bot.services;

import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.entities.enums.PaymentStatus;
import com.rodmag.youtube_premium_billing_bot.exceptions.ParticipantNotFoundException;
import com.rodmag.youtube_premium_billing_bot.repositories.ParticipantRepository;
import com.rodmag.youtube_premium_billing_bot.repositories.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    private final ParticipantRepository participantRepository;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;

    public BillingService(ParticipantRepository participantRepository, PaymentRepository paymentRepository, EmailService emailService) {
        this.participantRepository = participantRepository;
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void generateMonthlyBillings() {

        LocalDate today = LocalDate.now();
        Integer currentDay = today.getDayOfMonth();
        Integer currentMonth = today.getMonthValue();
        Integer currentYear = today.getYear();

        if (currentDay != 1) {
            return;
        }


        Optional<Payment> lastPayment = paymentRepository.findTopByOrderByIdDesc();
        List<Participant> participants = participantRepository.findAll();

        Payment newPayment = new Payment();
        newPayment.setMonth(currentMonth);
        newPayment.setYear(currentYear);
        newPayment.setPaymentStatus(PaymentStatus.NOT_PAID);

        if (lastPayment.isEmpty()) {
            newPayment.setParticipant(participants.stream()
                    .min(Comparator.comparingInt(Participant::getBillingOrder))
                    .orElseThrow(() -> new ParticipantNotFoundException("No participant found")));
            paymentRepository.save(newPayment);
        } else {
            Integer lastParticipantOrder = lastPayment.get().getParticipant().getBillingOrder();
            Integer nextParticipantOrder;
            if (lastParticipantOrder.equals(participants.size())) {
                nextParticipantOrder = 1;
            } else {
                nextParticipantOrder = lastParticipantOrder + 1;
            }
            newPayment.setMonth(currentMonth);
            newPayment.setYear(currentYear);
            newPayment.setPaymentStatus(PaymentStatus.NOT_PAID);
            newPayment.setParticipant(participants.stream()
                    .filter(p -> p.getBillingOrder().equals(nextParticipantOrder))
                    .findFirst()
                    .orElseThrow(() -> new ParticipantNotFoundException("No participant found")));
            paymentRepository.save(newPayment);
        }
    }
    @Transactional
    public void dailyBillingCheck() {

        List<Payment> pendingPayments = paymentRepository.findAllByPaymentStatus(PaymentStatus.NOT_PAID);

        if (!pendingPayments.isEmpty()) {

            List<Participant> participantsToNotify = pendingPayments.stream()
                    .map(Payment::getParticipant)
                    .toList();

            participantsToNotify.forEach(participant -> emailService.sendEmail(participant.getEmail(), "A wild senhor barriga appeared!",
                    participant.getName() + " is being notified about the pending payment."));
        }
    }
}