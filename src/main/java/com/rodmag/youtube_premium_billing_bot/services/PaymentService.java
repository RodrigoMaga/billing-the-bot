package com.rodmag.youtube_premium_billing_bot.services;

import com.rodmag.youtube_premium_billing_bot.entities.Payment;
import com.rodmag.youtube_premium_billing_bot.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment insert(Payment obj) {
        return paymentRepository.save(obj);
    }
}
