package com.prueba.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(400);
        errorResponse.setMessage(errors.toString());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(409);
        errorResponse.setMessage(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(404);
        errorResponse.setMessage(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadObjectException.class)
    public ResponseEntity<ErrorResponse> handleBadObjectException(BadObjectException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(404);
        errorResponse.setMessage(exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(400);
        errorResponse.setMessage(String.format("El parámetro %s es requerido y debe estar en formato yyyy-MM-dd", exception.getParameterName()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

}
