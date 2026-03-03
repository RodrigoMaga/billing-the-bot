package com.rodmag.youtube_premium_billing_bot.service;

import com.rodmag.youtube_premium_billing_bot.controller.dto.filter.PaymentFilterDto;
import com.rodmag.youtube_premium_billing_bot.controller.dto.request.PageRequestDto;
import com.rodmag.youtube_premium_billing_bot.entity.Payment;
import com.rodmag.youtube_premium_billing_bot.entity.enums.SortDirection;
import com.rodmag.youtube_premium_billing_bot.exception.PaymentNotFoundException;
import com.rodmag.youtube_premium_billing_bot.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
    }

    public Page<Payment> search(PageRequestDto pageRequestDto, PaymentFilterDto paymentFilterDto) {

        Sort sort = Sort.by(pageRequestDto.sortBy().property());



        sort = pageRequestDto.sortDirection() == SortDirection.DESC
                ? sort.descending()
                : sort.ascending();

        Pageable pageable = PageRequest.of(
                pageRequestDto.pageNumber(),
                pageRequestDto.pageSize(),
                sort
        );

        return paymentRepository.findAll(buildSpecification(paymentFilterDto), pageable);
    }

    public static Specification<Payment> buildSpecification(PaymentFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (filter.month() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("month"), filter.month()));
            }
            if (filter.year() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("year"), filter.year()));
            }
            if (filter.participantId() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.equal(root.get("participant").get("id"), filter.participantId()));
            }
            if (filter.paymentStatus() != null) {
                predicates = criteriaBuilder.and(predicates,criteriaBuilder.equal(root.get("paymentStatus"), filter.paymentStatus())
                );
            }

            return predicates;
        };
    }
}
