package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.dao.UserDao;
import org.dilip.first.pos_backend.entity.UserEntity;
import org.dilip.first.pos_backend.exception.ApiException;
import org.dilip.first.pos_backend.util.helper.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.dilip.first.pos_backend.util.helper.StringUtil.extractRoleFromEmail;

@Service
@Transactional
public class UserApi {

    @Autowired
    private UserDao userDao;

    public UserEntity createUser(String email, String hashedPassword) {

        if (userDao.findByEmail(email) != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "User already exists "+email);
        }
        UserRole role = extractRoleFromEmail(email);
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPassword(hashedPassword);
        entity.setRole(role);

        return userDao.save(entity);
    }

    public UserEntity authenticateUser(String email, String rawPassword) {

        UserEntity user = userDao.findByEmail(email);

        if (user == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email not exists : "+email);
        }

        if (!PasswordUtil.verifyPassword(rawPassword, user.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Incorrect password ");
        }

        return user;
    }
}
