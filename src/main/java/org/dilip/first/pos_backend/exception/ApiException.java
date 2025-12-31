package org.dilip.first.pos_backend.exception;

import lombok.Getter;
import org.dilip.first.pos_backend.constants.ApiStatus;
import org.dilip.first.pos_backend.model.error.FieldErrorData;

import java.util.List;

@Getter
public class ApiException extends RuntimeException {

    private final ApiStatus status;
    private final List<FieldErrorData> errors;

    public ApiException(ApiStatus status, String message) {
        super(message);
        this.status = status;
        this.errors = null;
    }

    public ApiException(ApiStatus status, String message, List<FieldErrorData> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }
}
