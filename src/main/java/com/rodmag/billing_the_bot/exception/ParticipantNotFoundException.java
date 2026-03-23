package com.rodmag.billing_the_bot.exception;

import org.springframework.http.HttpStatus;

public class ParticipantNotFoundException extends BusinessException {

    public ParticipantNotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }
}
