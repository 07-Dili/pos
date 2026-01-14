package org.dilip.first.pos_backend.util.helper;

import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.exception.ApiException;
import org.springframework.http.HttpStatus;

public class StringUtil {

    public static String normalizeToLowerCase(String input) {
        return input == null ? null : input.trim().toLowerCase();
    }

    public static UserRole extractRoleFromEmail(String email) {

        String rolePart = email.substring(email.indexOf('@') + 1).toLowerCase().trim();

        if (rolePart.startsWith("operator")) {
            return UserRole.OPERATOR;
        }

        if (rolePart.startsWith("supervisor")) {
            return UserRole.SUPERVISOR;
        }

        throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid role in email. Allowed roles: OPERATOR, SUPERVISOR "+email);
    }
}


