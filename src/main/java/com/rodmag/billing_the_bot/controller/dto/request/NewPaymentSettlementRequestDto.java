package com.rodmag.billing_the_bot.controller.dto.request;


public record NewPaymentSettlementRequestDto(
        Integer month,
        Integer year,
        Long participantId
) {
}
