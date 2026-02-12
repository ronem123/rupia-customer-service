/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:12/02/2026
 * Time:15:40
 */


package com.ronem.customer.exception;

import org.springframework.http.HttpStatus;

public class AuthServiceException extends RuntimeException {
    private final HttpStatus status;

    public AuthServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}