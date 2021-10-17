package com.flower.util.exception;

import com.flower.util.exception.custom.EmailInUseException;
import com.flower.util.exception.custom.EmailNotFoundException;
import com.flower.util.exception.custom.InvalidCredentialsException;
import com.flower.util.exception.custom.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionsHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EmailInUseException.class)
    public ResponseEntity<Object> handleEmailInUse(final EmailInUseException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentials(final InvalidCredentialsException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(final ResourceNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Object> handleEmailNotFound(final EmailNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildResponseEntity(final String message, final HttpStatus status) {
        ApiException apiException = new ApiException(status);
        apiException.setMessage(message);
        return buildResponseEntity(apiException);
    }

    private ResponseEntity<Object> buildResponseEntity(final String message, final HttpStatus status, String type) {
        ApiException apiException = new ApiException(status);
        apiException.setMessage(message);
        apiException.setType(type);
        return buildResponseEntity(apiException);
    }

    private ResponseEntity<Object> buildResponseEntity(final ApiException apiException) {
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }
}