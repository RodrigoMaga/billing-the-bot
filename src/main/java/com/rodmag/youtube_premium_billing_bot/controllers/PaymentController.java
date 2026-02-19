package com.rodmag.youtube_premium_billing_bot.controllers;

import com.rodmag.youtube_premium_billing_bot.controllers.dto.filter.PaymentFilterDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.request.NewPaymentSettlementRequestDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.request.PageRequestDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.response.PageResponseDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.response.PaymentResponseDto;
import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.entities.enums.PaymentStatus;
import com.rodmag.youtube_premium_billing_bot.entities.enums.SortBy;
import com.rodmag.youtube_premium_billing_bot.entities.enums.SortDirection;
import com.rodmag.youtube_premium_billing_bot.services.PaymentService;
import com.rodmag.youtube_premium_billing_bot.services.PaymentSettlementService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    @Autowired
    private PaymentSettlementService paymentSettlementService;
    @Autowired
    private PaymentService paymentService;

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
    @GetMapping
    public PageResponseDto<PaymentResponseDto> search(@NotNull @RequestParam(defaultValue = "0") Integer page,
                                                       @NotNull @RequestParam(defaultValue = "10") Integer size,
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
