package com.rodmag.billing_the_bot.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}
