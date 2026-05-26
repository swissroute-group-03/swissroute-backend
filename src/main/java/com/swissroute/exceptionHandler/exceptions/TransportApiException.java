package com.swissroute.exceptionHandler.exceptions;

import lombok.Getter;

@Getter
public class TransportApiException extends RuntimeException {

    private final int status;
    private final String responseBody;

    public TransportApiException(int status, String reason, String responseBody) {
        super("External API error %d %s: %s".formatted(status, reason, responseBody));
        this.status = status;
        this.responseBody = responseBody;
    }
}
