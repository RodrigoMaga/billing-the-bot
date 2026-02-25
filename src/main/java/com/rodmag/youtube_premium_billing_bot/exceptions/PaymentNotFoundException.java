package com.rodmag.youtube_premium_billing_bot.exceptions;

public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(String msg) {
        super(msg);
    }
}
