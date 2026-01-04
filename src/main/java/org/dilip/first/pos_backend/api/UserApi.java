package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.ApiStatus;
import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.dao.UserDao;
import org.dilip.first.pos_backend.entity.UserEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.util.helper.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserApi {

    @Autowired
    private UserDao userDao;

    public UserEntity createUser(String email, String hashedPassword) {

        if (userDao.findByEmail(email) != null) {
            throw new ApiException(409, "User already exists "+email);
        }
        UserRole role = extractRoleFromEmail(email);
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPassword(hashedPassword);
        entity.setRole(role);

        return userDao.save(entity);
    }

    private UserRole extractRoleFromEmail(String email) {

        String rolePart = email.substring(email.indexOf('@') + 1).toLowerCase().trim();

        if (rolePart.startsWith("operator")) {
            return UserRole.OPERATOR;
        }

        if (rolePart.startsWith("supervisor")) {
            return UserRole.SUPERVISOR;
        }

        throw new ApiException(400, "Invalid role in email. Allowed roles: OPERATOR, SUPERVISOR "+email);
    }

    public UserEntity authenticateUser(String email, String rawPassword) {

        UserEntity user = userDao.findByEmail(email);

        if (user == null) {
            throw new ApiException(404, "Invalid email "+email);
        }

        if (!PasswordUtil.verifyPassword(rawPassword, user.getPassword())) {
            throw new ApiException(401, "Incorrect password "+rawPassword);
        }

        return user;
    }
}
