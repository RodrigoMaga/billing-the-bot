package com.rodmag.youtube_premium_billing_bot.controller.dto.request;

import jakarta.validation.constraints.*;

public record NewParticipantRequestDto(
        @NotBlank
        @Size(max = 150)
        String name,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(max = 20)
        String phone,

        @NotNull
        Boolean notificationEnable
) {
}
