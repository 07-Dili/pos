package org.dilip.first.pos_backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class ErrorData {

    private int status;
    private String error;
    private String message;
    private List<FieldErrorData> errors;

    public static ErrorData of(HttpStatus status, String message, List<FieldErrorData> errors) {
        ErrorData data = new ErrorData();
        data.setStatus(status.value());
        data.setError(status.name());
        data.setMessage(message);
        data.setErrors(errors);
        return data;
    }
}
