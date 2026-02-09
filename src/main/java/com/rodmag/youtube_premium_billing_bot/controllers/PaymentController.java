package com.rodmag.youtube_premium_billing_bot.controllers;

import com.rodmag.youtube_premium_billing_bot.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

//    @PostMapping
//    public PaymentResponseDto insert(@RequestBody NewPaymentRequestDto obj) {
//        Payment payment = new Payment();
//        payment.setMonth(obj.month());
//        payment.setYear(obj.year());
//        payment.setPaymentStatus(obj.paymentStatus());
//        paymentService.insert(payment);
//        return new PaymentResponseDto(payment);
//    }
}
