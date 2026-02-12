package com.rodmag.youtube_premium_billing_bot.services;

import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.entities.enums.PaymentStatus;
import com.rodmag.youtube_premium_billing_bot.exceptions.ParticipantNotFoundException;
import com.rodmag.youtube_premium_billing_bot.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentSettlementService {

    @Autowired
    private PaymentRepository paymentRepository;

    public void execute(Payment newPayment) {

        paymentRepository.findFirstByMonthAndYearAndParticipant_Id(newPayment.getMonth(), newPayment.getYear(), newPayment.getParticipant().getId())
                        .filter(foundPayment -> foundPayment.getPaymentStatus() == PaymentStatus.NOT_PAID)
                                .ifPresentOrElse(foundPayment -> {
                                    foundPayment.setPaymentStatus(PaymentStatus.PAID);
                                    paymentRepository.save(foundPayment);
                                }, () -> {
                                    throw new ParticipantNotFoundException("No pending payment found for the given month, year, and participant.");
                                });
    }
}
