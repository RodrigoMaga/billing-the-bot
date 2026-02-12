package com.rodmag.youtube_premium_billing_bot.controllers;

import com.rodmag.youtube_premium_billing_bot.controllers.dto.request.NewPaymentSettlementRequestDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.response.PaymentResponseDto;
import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.exceptions.ParticipantNotFoundException;
import com.rodmag.youtube_premium_billing_bot.services.ParticipantService;
import com.rodmag.youtube_premium_billing_bot.services.PaymentSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    @Autowired
    private PaymentSettlementService paymentSettlementService;

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
}
