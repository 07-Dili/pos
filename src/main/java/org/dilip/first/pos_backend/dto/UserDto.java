package org.dilip.first.pos_backend.dto;


import org.dilip.first.pos_backend.api.UserApi;
import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.entity.UserEntity;
import org.dilip.first.pos_backend.model.data.UserData;
import org.dilip.first.pos_backend.model.form.UserForm;
import org.dilip.first.pos_backend.util.helper.PasswordUtil;
import org.dilip.first.pos_backend.util.helper.StringUtil;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

@Component
public class UserDto {

    private final UserApi userApi;

    public UserDto(UserApi userApi) {
        this.userApi = userApi;
    }

    public UserData signup(UserForm form) {

        String email = StringUtil.normalize(form.getEmail());
        String password = PasswordUtil.hash(form.getPassword());

        UserRole role = extractRoleFromEmail(email);

        UserEntity entity = userApi.createUser(email, password, role);
        return convertEntityToUserData(entity);
    }

    public UserData login(UserForm form) {

        String email = StringUtil.normalize(form.getEmail());
        String password = form.getPassword();
         
        UserEntity entity = userApi.login(email, password);
        if (entity == null) {
            throw new RuntimeException("User not found");
        }

        return convertEntityToUserData(entity);

    }

    public void logout() {
        
    }

    private UserRole extractRoleFromEmail(String email) {
        String domain = email.substring(email.indexOf('@') + 1);

        if (domain.startsWith("supervisor.")) {
            return UserRole.SUPERVISOR;
        }
        if (domain.startsWith("operator.")) {
            return UserRole.OPERATOR;
        }

        throw new RuntimeException("Invalid role in email");
    }

    private UserData convertEntityToUserData(UserEntity entity) {
        UserData data = new UserData();
        data.setId(entity.getId());
        data.setEmail(entity.getEmail());
        data.setRole(entity.getRole());
        return data;
    }
}

