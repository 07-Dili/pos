package org.dilip.first.pos_backend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.exception.ErrorData;
import org.dilip.first.pos_backend.exception.FieldErrorData;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ErrorData handleApiException(ApiException e, HttpServletResponse response) {
        response.setStatus(e.getStatus().value());
        return ErrorData.of(e.getStatus(), e.getMessage(), e.getErrors());
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorData handleValidationException(Exception ex, HttpServletResponse response) {

        BindingResult br = ex instanceof BindException
                ? ((BindException) ex).getBindingResult()
                : ((MethodArgumentNotValidException) ex).getBindingResult();

        List<FieldErrorData> errors = new ArrayList<>();
        for (FieldError fe : br.getFieldErrors()) {
            FieldErrorData fd = new FieldErrorData();
            fd.setField(fe.getField());
            fd.setMessage(fe.getDefaultMessage());
            errors.add(fd);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ErrorData.of(HttpStatus.BAD_REQUEST, "Input validation failed", errors);
    }
}
