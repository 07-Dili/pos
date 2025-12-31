package org.dilip.first.pos_backend.util.validator;

import org.dilip.first.pos_backend.constants.ApiStatus;
import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.exception.ApiException;

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

        throw new ApiException(ApiStatus.BAD_DATA, "Invalid role in email");
    }
}
