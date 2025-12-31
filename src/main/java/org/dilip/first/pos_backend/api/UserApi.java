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

    public UserEntity createUser(String email, String hashedPassword, UserRole role) {

        if (userDao.findByEmail(email) != null) {
            throw new ApiException(ApiStatus.RESOURCE_EXISTS, "User already exists");
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPassword(hashedPassword);
        entity.setRole(role);

        return userDao.save(entity);
    }

    public UserEntity authenticateUser(String email, String rawPassword) {

        UserEntity user = userDao.findByEmail(email);

        if (user == null || !PasswordUtil.verifyPassword(rawPassword, user.getPassword())) {
            throw new ApiException(ApiStatus.AUTH_ERROR, "Invalid email or password");
        }

        return user;
    }
}
