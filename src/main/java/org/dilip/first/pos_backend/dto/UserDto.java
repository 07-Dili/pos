package org.dilip.first.pos_backend.dto;

import org.dilip.first.pos_backend.api.UserApi;
import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.entity.UserEntity;
import org.dilip.first.pos_backend.model.data.UserData;
import org.dilip.first.pos_backend.model.form.UserForm;
import org.dilip.first.pos_backend.util.conversion.UserConversionUtil;
import org.dilip.first.pos_backend.util.helper.PasswordUtil;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.dilip.first.pos_backend.util.validator.UserRoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDto {

    @Autowired
    private UserApi userApi;

    public UserData signup(UserForm form) {

        String email = StringUtil.normalizeToLowerCase(form.getEmail());
        String hashedPassword = PasswordUtil.hashPassword(form.getPassword());
        UserEntity entity = userApi.createUser(email, hashedPassword);
        return UserConversionUtil.convertEntityToData(entity);
    }

    public UserData login(UserForm form) {

        String email = StringUtil.normalizeToLowerCase(form.getEmail());
        UserEntity entity = userApi.authenticateUser(email, form.getPassword());
        return UserConversionUtil.convertEntityToData(entity);
    }
}
