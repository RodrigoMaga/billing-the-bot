package com.rodmag.youtube_premium_billing_bot.controllers.dto.request;


public record NewPaymentSettlementRequestDto(
        Integer month,
        Integer year,
        Long participantId
) {
}
