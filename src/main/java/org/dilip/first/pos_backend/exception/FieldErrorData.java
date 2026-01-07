package org.dilip.first.pos_backend.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldErrorData {

    private String field;
    private String message;
}
