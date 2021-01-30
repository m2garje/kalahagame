package com.backbase.game.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * This ExceptionResponse prepares whenever system behaves wrongly and the
 * same will be send back to the caller.
 *
 * @author Mahesh G
 *
 */
@Getter
public class ExceptionResponse {

    private final HttpStatus status;
    private final String message;

    public ExceptionResponse(String message, HttpStatus status) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionResponse of(final String message, HttpStatus status) {
        return new ExceptionResponse(message, status);
    }

}
