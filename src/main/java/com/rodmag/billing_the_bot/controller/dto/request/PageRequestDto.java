package com.rodmag.billing_the_bot.controller.dto.request;

import com.rodmag.billing_the_bot.entity.enums.SortBy;
import com.rodmag.billing_the_bot.entity.enums.SortDirection;

public record PageRequestDto(
        Integer pageNumber,
        Integer pageSize,
        SortBy sortBy,
        SortDirection sortDirection
) {
}
