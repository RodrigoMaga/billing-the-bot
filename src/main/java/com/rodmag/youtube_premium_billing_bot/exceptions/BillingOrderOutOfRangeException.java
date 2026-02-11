package com.rodmag.youtube_premium_billing_bot.exceptions;

public class BillingOrderOutOfRangeException extends RuntimeException {

    public BillingOrderOutOfRangeException(String msg) {
        super(msg);
    }
}