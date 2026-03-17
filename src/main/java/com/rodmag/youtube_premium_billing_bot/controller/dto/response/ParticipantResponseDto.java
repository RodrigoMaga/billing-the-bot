package com.rodmag.youtube_premium_billing_bot.controller.dto.response;

import com.rodmag.youtube_premium_billing_bot.entity.Participant;

public record ParticipantResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        Integer billingOrder,
        Boolean notificationEnable
) {
    public ParticipantResponseDto(Participant participant) {
        this(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getPhone(),
                participant.getBillingOrder(),
                participant.getNotificationEnable()
        );
    }
}
