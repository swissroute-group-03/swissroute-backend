package com.swissroute.exceptionHandler.exceptions;

public class FavoriteStationNotFoundException extends RuntimeException {
    public FavoriteStationNotFoundException(String message) {
        super(message);
    }
}
