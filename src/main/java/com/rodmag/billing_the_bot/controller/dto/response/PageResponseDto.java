package com.rodmag.billing_the_bot.controller.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last,
        boolean first
) {
    public static <T> PageResponseDto<T> from(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst()
        );
    }
}
