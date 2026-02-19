package com.rodmag.youtube_premium_billing_bot.controllers.dto.filter;

import com.rodmag.youtube_premium_billing_bot.entities.enums.PaymentStatus;

public record PaymentFilterDto(
            Integer month,
            Integer year,
            PaymentStatus paymentStatus,
            Long participantId
) {
}
