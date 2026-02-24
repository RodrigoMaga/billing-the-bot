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
import java.util.List;
import java.util.Optional;

@Service
public class BillingService {

    private final ParticipantRepository participantRepository;
    private final PaymentRepository paymentRepository;

    public BillingService(ParticipantRepository participantRepository, PaymentRepository paymentRepository) {
        this.participantRepository = participantRepository;
        this.paymentRepository = paymentRepository;
    }


    @Transactional
    public void generateMonthlyBillings() {

        LocalDate today = LocalDate.now();
        Integer currentMonth = today.getMonthValue();
        Integer currentYear = today.getYear();
        Integer currentDay = today.getDayOfMonth();

        Optional<Payment> lastPayment = paymentRepository.findTopByOrderByIdDesc();
        List<Participant> participants = participantRepository.findAll();

        Integer nextParticipantOrder;

        if (currentDay == 1) {
            if (lastPayment.isEmpty()) {
                Payment newPayment = new Payment();
                newPayment.setMonth(currentMonth);
                newPayment.setYear(currentYear);
                newPayment.setPaymentStatus(PaymentStatus.NOT_PAID);
                newPayment.setParticipant(participants.stream()
                        .filter(p -> p.getBillingOrder().equals(1))
                        .findFirst()
                        .orElseThrow(() -> new ParticipantNotFoundException("No participant found")));
                paymentRepository.save(newPayment);
            } else {
                Integer lastParticipantOrder = lastPayment.get().getParticipant().getBillingOrder();
                if (lastParticipantOrder.equals(participants.size())) {
                    nextParticipantOrder = 1;
                } else {
                    nextParticipantOrder = lastParticipantOrder + 1;
                }
                Payment newPayment = new Payment();
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
    }
}
