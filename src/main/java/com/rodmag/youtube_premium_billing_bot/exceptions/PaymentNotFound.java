package com.rodmag.youtube_premium_billing_bot.exceptions;

public class PaymentNotFound extends RuntimeException{
    public PaymentNotFound(String msg) {
        super(msg);
    }
}
