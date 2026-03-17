package com.rodmag.youtube_premium_billing_bot.controller.dto.response;

import com.rodmag.youtube_premium_billing_bot.entity.Payment;
import com.rodmag.youtube_premium_billing_bot.entity.enums.PaymentStatus;

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
