package com.rodmag.billing_the_bot.exception;

import org.springframework.http.HttpStatus;

public class BillingOrderOutOfRangeException extends BusinessException {

    public BillingOrderOutOfRangeException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}