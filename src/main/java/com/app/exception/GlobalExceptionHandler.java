
package com.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.app.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = "Invalid request";

        if (ex.getBindingResult().getFieldError() != null) {
            message = ex.getBindingResult()
                    .getFieldError()
                    .getDefaultMessage();
        }

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle duplicate email and other IllegalArgumentException errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(
            DuplicateEmailException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex) {

        ex.printStackTrace();

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong"
        );

        return new ResponseEntity<>(
                error,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    

@ExceptionHandler(VehicleNotFoundException.class)
public ResponseEntity<ErrorResponse> handleVehicleNotFound(
        VehicleNotFoundException ex) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
    );

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}

@ExceptionHandler(ResponseStatusException.class)
public ResponseEntity<ErrorResponse> handleResponseStatusException(
        ResponseStatusException ex) {

    ErrorResponse error = new ErrorResponse(
            ex.getStatusCode().value(),
            ex.getReason());

    return ResponseEntity
            .status(ex.getStatusCode())
            .body(error);
}
}
