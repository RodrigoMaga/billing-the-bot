package com.rodmag.youtube_premium_billing_bot.controllers.dto.request;

import com.rodmag.youtube_premium_billing_bot.entities.enums.SortBy;
import com.rodmag.youtube_premium_billing_bot.entities.enums.SortDirection;

public record PageRequestDto(
        Integer pageNumber,
        Integer pageSize,
        SortBy sortBy,
        SortDirection sortDirection
) {
}
