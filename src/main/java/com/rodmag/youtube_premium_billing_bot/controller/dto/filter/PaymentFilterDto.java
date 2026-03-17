package com.rodmag.youtube_premium_billing_bot.controller.dto.filter;

import com.rodmag.youtube_premium_billing_bot.entity.enums.PaymentStatus;

public record PaymentFilterDto(
            Integer month,
            Integer year,
            PaymentStatus paymentStatus,
            Long participantId
) {
}
