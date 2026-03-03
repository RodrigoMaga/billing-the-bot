package com.rodmag.youtube_premium_billing_bot.controller.dto.request;

import com.rodmag.youtube_premium_billing_bot.entity.enums.SortBy;
import com.rodmag.youtube_premium_billing_bot.entity.enums.SortDirection;

public record PageRequestDto(
        Integer pageNumber,
        Integer pageSize,
        SortBy sortBy,
        SortDirection sortDirection
) {
}
