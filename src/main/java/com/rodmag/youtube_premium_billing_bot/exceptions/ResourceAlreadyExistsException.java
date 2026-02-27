package com.rodmag.youtube_premium_billing_bot.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}
