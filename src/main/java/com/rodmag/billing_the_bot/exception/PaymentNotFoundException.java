package com.rodmag.billing_the_bot.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends BusinessException{
    public PaymentNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
