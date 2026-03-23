package com.rodmag.youtube_premium_billing_bot.controller.dto.response;

import com.rodmag.youtube_premium_billing_bot.entity.Payment;
import com.rodmag.youtube_premium_billing_bot.entity.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponseDto(
        Long id,
        Integer month,
        Integer year,
        PaymentStatus paymentStatus,
        ParticipantResponseDto participant,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public PaymentResponseDto(Payment payment) {
        this(
                payment.getId(),
                payment.getMonth(),
                payment.getYear(),
                payment.getPaymentStatus(),
                new ParticipantResponseDto(payment.getParticipant()),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
