package com.rodmag.billing_the_bot.service;

import com.rodmag.billing_the_bot.entity.Payment;
import com.rodmag.billing_the_bot.entity.enums.PaymentStatus;
import com.rodmag.billing_the_bot.exception.ParticipantNotFoundException;
import com.rodmag.billing_the_bot.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentSettlementService {

    private final PaymentRepository paymentRepository;

    public PaymentSettlementService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public void execute(Payment newPayment) {

        paymentRepository.findFirstByMonthAndYearAndParticipant_Id(newPayment.getMonth(), newPayment.getYear(), newPayment.getParticipant().getId())
                        .filter(foundPayment -> foundPayment.getPaymentStatus() == PaymentStatus.NOT_PAID)
                                .ifPresentOrElse(foundPayment -> {
                                    foundPayment.setPaymentStatus(PaymentStatus.PAID);
                                    paymentRepository.save(foundPayment);
                                    System.out.println("Payment settled successfully for participant: " + foundPayment.getParticipant().getEmail() + " for month: " + foundPayment.getMonth() + " and year: " + foundPayment.getYear());
                                }, () -> {
                                    throw new ParticipantNotFoundException("No pending payment found for the given month, year, and participant.");
                                });
    }
}
