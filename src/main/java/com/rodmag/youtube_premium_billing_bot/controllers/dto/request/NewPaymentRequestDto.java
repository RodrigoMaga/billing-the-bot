package com.rodmag.youtube_premium_billing_bot.controllers.dto.request;

import com.rodmag.youtube_premium_billing_bot.entities.enums.PaymentStatus;

public record NewPaymentRequestDto(
        Integer month,
        Integer year,
        PaymentStatus paymentStatus
) {
}
