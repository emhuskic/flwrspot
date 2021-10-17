package com.flower.util.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NONE, property = "error", visible = true)
class ApiException {
    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;
    private String type;

    private ApiException() {
        timestamp = LocalDateTime.now();
    }

    public ApiException(final HttpStatus status) {
        this();
        this.status = status;
    }

    ApiException(final HttpStatus status, final Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
    }

    ApiException(final HttpStatus status, final String message, final Throwable ex) {
        this();
        this.status = status;
        this.message = message;
    }

    public void setMessageFromMap(final Map<String, String> errors) {
        this.message = String.join(", ", errors.values());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
