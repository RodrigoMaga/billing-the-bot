package com.rodmag.youtube_premium_billing_bot.controller;

import com.rodmag.youtube_premium_billing_bot.controller.dto.request.NewPaymentRequestDto;
import com.rodmag.youtube_premium_billing_bot.entities.Payment;
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

    @PostMapping
    public Payment inset(@RequestBody NewPaymentRequestDto obj) {
        Payment payment = new Payment();
        payment.setMonth(obj.month());
        payment.setYear(obj.year());
        payment.setPaymentStatus(obj.paymentStatus());
        return paymentService.insert(payment);

    }
}
