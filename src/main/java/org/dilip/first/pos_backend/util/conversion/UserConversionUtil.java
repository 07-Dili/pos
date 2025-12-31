package org.dilip.first.pos_backend.util.conversion;

import org.dilip.first.pos_backend.entity.UserEntity;
import org.dilip.first.pos_backend.model.data.UserData;

public class UserConversionUtil {

    private UserConversionUtil() {}

    public static UserData convertEntityToData(UserEntity entity) {
        UserData data = new UserData();
        data.setId(entity.getId());
        data.setEmail(entity.getEmail());
        data.setRole(entity.getRole());
        return data;
    }
}
