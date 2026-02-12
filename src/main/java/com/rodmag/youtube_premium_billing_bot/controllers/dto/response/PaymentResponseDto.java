package com.rodmag.youtube_premium_billing_bot.controllers.dto.response;

import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.entities.enums.PaymentStatus;

public record PaymentResponseDto(
        Long id,
        Integer month,
        Integer year,
        PaymentStatus paymentStatus,
        ParticipantResponseDto participant
) {
    public PaymentResponseDto(Payment payment) {
        this(
                payment.getId(),
                payment.getMonth(),
                payment.getYear(),
                payment.getPaymentStatus(),
                new ParticipantResponseDto(payment.getParticipant())
        );
    }
}
