package com.rodmag.youtube_premium_billing_bot.controllers.dto.request;

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
        @Min(1)
        @Max(6)
        Integer billingOrder
) {
}
