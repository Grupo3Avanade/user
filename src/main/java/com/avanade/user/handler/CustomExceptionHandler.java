package com.avanade.user.handler;


import com.avanade.user.exceptions.DatabaseException;
import com.avanade.user.exceptions.ResourceAlreadyExistsException;
import com.avanade.user.exceptions.ResourceNotFoundException;
import com.avanade.user.payloads.response.ResponseStandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStandardError> handleInvalidMethodArguments(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST, String.join("; ", errorMessages));
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseStandardError> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseStandardError> handleUnreadableMessage(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseStandardError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ResponseStandardError> handleResourceAlreadyExists(ResourceAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseStandardError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ResponseStandardError> handleBadRequestException(DatabaseException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStandardError> handleNullPointerException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ResponseStandardError> buildErrorResponse(Exception ex, HttpServletRequest request, HttpStatus status) {
        return buildErrorResponse(ex, request, status, ex.getMessage());
    }

    private ResponseEntity<ResponseStandardError> buildErrorResponse(Exception ex, HttpServletRequest request, HttpStatus status, String message) {
        LOGGER.error("{}: {}", ex.getClass().getName(), ex.getMessage());
        ResponseStandardError error = new ResponseStandardError(
                status.value(),
                message,
                new Date().toString()
        );
        return ResponseEntity.status(status).body(error);
    }
}
