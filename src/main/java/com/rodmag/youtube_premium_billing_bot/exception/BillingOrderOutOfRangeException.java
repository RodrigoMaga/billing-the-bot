package com.rodmag.youtube_premium_billing_bot.exception;

import org.springframework.http.HttpStatus;

public class BillingOrderOutOfRangeException extends BusinessException {

    public BillingOrderOutOfRangeException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}