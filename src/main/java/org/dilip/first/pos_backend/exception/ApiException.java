package org.dilip.first.pos_backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final List<FieldErrorData> errors;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.errors = null;
    }

    public ApiException(HttpStatus status, String message, List<FieldErrorData> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }
}
