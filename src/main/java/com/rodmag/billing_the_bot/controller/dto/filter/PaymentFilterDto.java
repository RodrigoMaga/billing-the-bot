package com.rodmag.billing_the_bot.controller.dto.filter;

import com.rodmag.billing_the_bot.entity.enums.PaymentStatus;

public record PaymentFilterDto(
            Integer month,
            Integer year,
            PaymentStatus paymentStatus,
            Long participantId
) {
}
