package org.dilip.first.pos_backend.api;

import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.dao.UserDao;
import org.dilip.first.pos_backend.entity.UserEntity;
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
            throw new RuntimeException("User already exists");
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setPassword(hashedPassword);
        entity.setRole(role);

        return userDao.save(entity);
    }

    public UserEntity login(String email, String password) {
        
        UserEntity entity = userDao.findByEmail(email);
        if (entity == null) {
            throw new RuntimeException("User not found");
        }

        if (!PasswordUtil.verify(password, entity.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return entity;  
    }
}

