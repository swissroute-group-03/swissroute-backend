package com.swissroute.exceptionHandler;

import java.time.LocalDateTime;

import com.swissroute.exceptionHandler.exceptions.ConflictException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleConflictException(ResourceNotFoundException resourceNotFoundException, HttpServletRequest request){
        return new ResponseEntity<>(
            new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                resourceNotFoundException.getMessage(),
                request.getRequestURI()
            ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request){
        return new ResponseEntity<>(
            new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI()
            ), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleUsernameNotFoundException(UsernameNotFoundException ex, HttpServletRequest request){
        return new ResponseEntity<>(
            new ErrorDetails(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI()
            ), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleConflictException(
            ConflictException ex,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        "Conflict",
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleTransportApiException(
            TransportApiException ex,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        LocalDateTime.now(),
                        ex.getStatus(),
                        "External API Error",
                        ex.getMessage(),
                        request.getRequestURI()
                ),
                HttpStatus.valueOf(ex.getStatus())
        );
    }
}
