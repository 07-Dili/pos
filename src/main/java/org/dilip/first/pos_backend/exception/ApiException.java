package org.dilip.first.pos_backend.exception;

import lombok.Getter;
import org.dilip.first.pos_backend.constants.ApiStatus;
import org.dilip.first.pos_backend.model.error.FieldErrorData;

import java.util.List;

@Getter
public class ApiException extends RuntimeException {

    private final int status;
    private final List<FieldErrorData> errors;

    public ApiException(int statusCode, String message) {
        super(message);
        this.status = statusCode;
        this.errors = null;
    }
}
