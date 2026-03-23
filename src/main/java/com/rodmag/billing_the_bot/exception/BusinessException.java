package com.rodmag.billing_the_bot.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
