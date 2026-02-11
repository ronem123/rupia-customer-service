/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:27/01/2026
 * Time:15:16
 */


package com.ronem.customer.exception;

import com.ronem.customer.model.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Http-Status: 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NoHandlerFoundException ne) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(false, HttpStatus.NOT_FOUND, "Resource you are looking for is not found", Instant.now());
        return new ResponseEntity<>(errorResponse, errorResponse.errorCode());
    }


    // Http-Status: 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(false, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred." + ex.getMessage(), Instant.now());
        return new ResponseEntity<>(errorResponse, errorResponse.errorCode());
    }
}