package com.rodmag.youtube_premium_billing_bot.exceptions;

import org.springframework.http.HttpStatus;

public class ParticipantNotFoundException extends BusinessException {

    public ParticipantNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
