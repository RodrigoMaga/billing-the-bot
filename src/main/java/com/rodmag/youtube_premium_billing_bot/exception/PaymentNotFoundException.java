package com.rodmag.youtube_premium_billing_bot.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends BusinessException{
    public PaymentNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
