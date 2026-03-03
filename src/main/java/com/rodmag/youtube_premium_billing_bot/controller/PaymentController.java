package com.rodmag.youtube_premium_billing_bot.controller;

import com.rodmag.youtube_premium_billing_bot.controller.dto.filter.PaymentFilterDto;
import com.rodmag.youtube_premium_billing_bot.controller.dto.request.NewPaymentSettlementRequestDto;
import com.rodmag.youtube_premium_billing_bot.controller.dto.request.PageRequestDto;
import com.rodmag.youtube_premium_billing_bot.controller.dto.response.PageResponseDto;
import com.rodmag.youtube_premium_billing_bot.controller.dto.response.PaymentResponseDto;
import com.rodmag.youtube_premium_billing_bot.entity.Participant;
import com.rodmag.youtube_premium_billing_bot.entity.Payment;
import com.rodmag.youtube_premium_billing_bot.entity.enums.PaymentStatus;
import com.rodmag.youtube_premium_billing_bot.entity.enums.SortBy;
import com.rodmag.youtube_premium_billing_bot.entity.enums.SortDirection;
import com.rodmag.youtube_premium_billing_bot.service.BillingScheduleService;
import com.rodmag.youtube_premium_billing_bot.service.PaymentService;
import com.rodmag.youtube_premium_billing_bot.service.PaymentSettlementService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    private final PaymentSettlementService paymentSettlementService;
    private final PaymentService paymentService;
    private final BillingScheduleService billingScheduleService;

    public PaymentController(PaymentSettlementService paymentSettlementService, PaymentService paymentService, BillingScheduleService billingScheduleService) {
        this.paymentSettlementService = paymentSettlementService;
        this.paymentService = paymentService;
        this.billingScheduleService = billingScheduleService;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/settlement")
    public void settle(@RequestBody NewPaymentSettlementRequestDto obj) {

        Participant participant = new Participant();
        participant.setId(obj.participantId());
        Payment payment = new Payment();
        payment.setMonth(obj.month());
        payment.setYear(obj.year());
        payment.setParticipant(participant);

        paymentSettlementService.execute(payment);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public PaymentResponseDto findById(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        return new PaymentResponseDto(payment);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public PageResponseDto<PaymentResponseDto> search(@NotNull @Min(0) @RequestParam(defaultValue = "0") Integer page,
                                                       @NotNull @Positive @RequestParam(defaultValue = "10") Integer size,
                                                      @RequestParam(defaultValue = "YEAR") SortBy sortBy,
                                                      @RequestParam(defaultValue = "ASC") SortDirection sortDirection,
                                                      @RequestParam(required = false) Integer month,
                                                      @RequestParam(required = false) Integer year,
                                                      @RequestParam(required = false) PaymentStatus paymentStatus,
                                                      @RequestParam(required = false) Long participantId) {

        PageRequestDto pageRequestDto = new PageRequestDto(page, size, sortBy, sortDirection);
        PaymentFilterDto filter = new PaymentFilterDto(month, year, paymentStatus, participantId);

        Page<PaymentResponseDto> result = paymentService
                .search(pageRequestDto, filter)
                .map(PaymentResponseDto::new);

        return PageResponseDto.from(result);
    }
}
