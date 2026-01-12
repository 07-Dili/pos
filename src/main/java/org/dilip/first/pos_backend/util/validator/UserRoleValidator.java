package org.dilip.first.pos_backend.util.validator;

import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserRoleValidator {

    private UserRoleValidator() {}

    public static UserRole deriveRoleFromEmail(String email) {

        String domain = email.substring(email.indexOf('@') + 1);

        if (domain.startsWith("supervisor")) {
            return UserRole.SUPERVISOR;
        }

        if (domain.startsWith("operator")) {
            return UserRole.OPERATOR;
        }

        throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid role in email "+email);
    }
}
