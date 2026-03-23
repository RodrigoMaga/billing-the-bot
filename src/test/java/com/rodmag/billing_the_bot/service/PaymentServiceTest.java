package com.rodmag.billing_the_bot.service;

import com.rodmag.billing_the_bot.controller.dto.filter.PaymentFilterDto;
import com.rodmag.billing_the_bot.controller.dto.request.PageRequestDto;
import com.rodmag.billing_the_bot.entity.Payment;
import com.rodmag.billing_the_bot.entity.enums.PaymentStatus;
import com.rodmag.billing_the_bot.entity.enums.SortBy;
import com.rodmag.billing_the_bot.entity.enums.SortDirection;
import com.rodmag.billing_the_bot.exception.PaymentNotFoundException;
import com.rodmag.billing_the_bot.repository.PaymentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    PaymentService paymentService;

     @BeforeEach
     void setup() {
             System.out.println("Setting up test environment...");
             paymentService = new PaymentService(paymentRepository);
     }

    @Test
    void given_filters_when_search_then_repository_should_be_called_with_correct_specification_and_pageable() {

         System.out.println("Running test: given_filters_when_search_then_repository_should_be_called_with_correct_specification_and_pageable");

         // given
         PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
         PaymentFilterDto paymentFilterDto = new PaymentFilterDto(6, 2024, PaymentStatus.PAID, 1L);

         List<Payment> payments = Arrays.asList(
                    new Payment(1L, 6, 2024, null, PaymentStatus.NOT_PAID),
                    new Payment(2L, 6, 2024, null, PaymentStatus.NOT_PAID)
            );

         Page<Payment> expectedPage = new PageImpl<>(payments);

         // when
            Mockito.when(paymentRepository.findAll(
                    Mockito.any(Specification.class),
                    Mockito.any(Pageable.class)
            )).thenReturn(expectedPage);

         Page<Payment> result = paymentService.search(pageRequestDto, paymentFilterDto);

         // then
         Assertions.assertThat(result).isNotNull();
         Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
         Assertions.assertThat(result.getContent()).hasSize(2);
         Mockito.verify(paymentRepository, Mockito.times(1)).findAll(
                 Mockito.any(Specification.class),
                 Mockito.any(Pageable.class)
         );
     }

    @Test
    void should_fail_when_payment_not_found_by_id() {

         System.out.println("Running test: should_fail_when_payment_not_found_by_id");

         // given
         Long nonExistentId = 999L;

         // when
         Mockito.when(paymentRepository.findById(nonExistentId)).thenReturn(Optional.empty());

         // then
         Assertions.assertThatThrownBy(() -> paymentService.findById(nonExistentId))
                 .isInstanceOf(PaymentNotFoundException.class)
                 .hasMessageContaining("Payment not found with id: " + nonExistentId);
     }

    @Test
    void should_find_payment_by_id() {

         System.out.println("Running test: should_find_payment_by_id");

         // given
         Long existingId = 1L;
         Payment payment = new Payment(existingId, 2024, 6, null, PaymentStatus.PAID);
         Mockito.when(paymentRepository.findById(existingId)).thenReturn(Optional.of(payment));

         // when
         Payment foundPayment = paymentService.findById(existingId);

         // then
         Assertions.assertThat(foundPayment).isNotNull();
         Assertions.assertThat(foundPayment.getId()).isEqualTo(existingId);
     }


    @Test
    void should_fail_when_search_with_invalid_page_request() {
        System.out.println("Running test: should_fail_when_search_with_invalid_page_request");

        PageRequestDto invalidPageRequestDto = new PageRequestDto(1, 0, SortBy.YEAR, SortDirection.ASC);
        PaymentFilterDto paymentFilterDto = new PaymentFilterDto(2024, 6, PaymentStatus.PAID, 1L);

        Assertions.assertThatThrownBy(() -> paymentService.search(invalidPageRequestDto, paymentFilterDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page size must not be less than one");
    }

    @Test
    void should_fail_when_search_with_invalid_filters() {
        System.out.println("Running test: should_fail_when_search_with_invalid_filters");

        PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
        PaymentFilterDto invalidPaymentFilterDto = new PaymentFilterDto(1850, 6, PaymentStatus.PAID, 1L);

        Assertions.assertThatThrownBy(() -> paymentService.search(pageRequestDto, invalidPaymentFilterDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Payment filter must have a valid month and year");
    }

    @Nested
    @DisplayName("Build Specification Tests")
    class BuildSpecificationTestSuite {

        @Test
        void should_build_specification_with_all_filters() {
            System.out.println("Running test: should_build_specification_with_all_filters");

            // given
            PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
            PaymentFilterDto filterDto = new PaymentFilterDto(6, 2024, PaymentStatus.PAID, 1L);

            List<Payment> payments = List.of(new Payment(1L, 6, 2024, null, PaymentStatus.PAID));
            Page<Payment> expectedPage = new PageImpl<>(payments);

            Mockito.when(paymentRepository.findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(expectedPage);

            // when
            Page<Payment> result = paymentService.search(pageRequestDto, filterDto);

            // then
            Assertions.assertThat(result).isNotNull();
            Mockito.verify(paymentRepository).findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            );
        }

        @Test
        void should_build_specification_with_only_month_filter() {
            System.out.println("Running test: should_build_specification_with_only_month_filter");

            // given
            PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
            PaymentFilterDto filterDto = new PaymentFilterDto(6, null, null, null);

            Page<Payment> expectedPage = new PageImpl<>(List.of());

            Mockito.when(paymentRepository.findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(expectedPage);

            // when
            Page<Payment> result = paymentService.search(pageRequestDto, filterDto);

            // then
            Assertions.assertThat(result).isNotNull();
            Mockito.verify(paymentRepository).findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            );
        }

        @Test
        void should_build_specification_with_only_year_filter() {
            System.out.println("Running test: should_build_specification_with_only_year_filter");

            // given
            PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
            PaymentFilterDto filterDto = new PaymentFilterDto(null, 2024, null, null);

            Page<Payment> expectedPage = new PageImpl<>(List.of());

            Mockito.when(paymentRepository.findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(expectedPage);

            // when
            Page<Payment> result = paymentService.search(pageRequestDto, filterDto);

            // then
            Assertions.assertThat(result).isNotNull();
            Mockito.verify(paymentRepository).findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            );
        }

        @Test
        void should_build_specification_with_only_participant_id_filter() {
            System.out.println("Running test: should_build_specification_with_only_participant_id_filter");

            // given
            PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
            PaymentFilterDto filterDto = new PaymentFilterDto(null, null, null, 1L);

            Page<Payment> expectedPage = new PageImpl<>(List.of());

            Mockito.when(paymentRepository.findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(expectedPage);

            // when
            Page<Payment> result = paymentService.search(pageRequestDto, filterDto);

            // then
            Assertions.assertThat(result).isNotNull();
            Mockito.verify(paymentRepository).findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            );
        }

        @Test
        void should_build_specification_with_only_payment_status_filter() {
            System.out.println("Running test: should_build_specification_with_only_payment_status_filter");

            // given
            PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
            PaymentFilterDto filterDto = new PaymentFilterDto(null, null, PaymentStatus.PAID, null);

            Page<Payment> expectedPage = new PageImpl<>(List.of());

            Mockito.when(paymentRepository.findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(expectedPage);

            // when
            Page<Payment> result = paymentService.search(pageRequestDto, filterDto);

            // then
            Assertions.assertThat(result).isNotNull();
            Mockito.verify(paymentRepository).findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            );
        }

        @Test
        void should_build_specification_with_no_filters() {
            System.out.println("Running test: should_build_specification_with_no_filters");

            // given
            PageRequestDto pageRequestDto = new PageRequestDto(0, 10, SortBy.YEAR, SortDirection.ASC);
            PaymentFilterDto filterDto = new PaymentFilterDto(null, null, null, null);

            Page<Payment> expectedPage = new PageImpl<>(List.of());

            Mockito.when(paymentRepository.findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            )).thenReturn(expectedPage);

            // when
            Page<Payment> result = paymentService.search(pageRequestDto, filterDto);

            // then
            Assertions.assertThat(result).isNotNull();
            Mockito.verify(paymentRepository).findAll(
                    ArgumentMatchers.any(Specification.class),
                    ArgumentMatchers.any(Pageable.class)
            );
        }
    }
}