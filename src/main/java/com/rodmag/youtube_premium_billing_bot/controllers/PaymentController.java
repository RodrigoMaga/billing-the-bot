package com.rodmag.youtube_premium_billing_bot.controllers;

import com.rodmag.youtube_premium_billing_bot.controllers.dto.request.NewPaymentRequestDto;
import com.rodmag.youtube_premium_billing_bot.controllers.dto.response.PaymentResponseDto;
import com.rodmag.youtube_premium_billing_bot.entities.Participant;
import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.exceptions.ResourceNotFoundException;
import com.rodmag.youtube_premium_billing_bot.services.ParticipantService;
import com.rodmag.youtube_premium_billing_bot.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ParticipantService participantService;

    @PostMapping

    public PaymentResponseDto insert(@RequestBody NewPaymentRequestDto obj) {

        Participant participant = participantService
                .findById(obj.participantId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found"));

        Payment payment = new Payment();
        payment.setMonth(obj.month());
        payment.setYear(obj.year());
        payment.setPaymentStatus(obj.paymentStatus());
        payment.setParticipant(participant);

        paymentService.insert(payment);

        return new PaymentResponseDto(payment);
    }
}
