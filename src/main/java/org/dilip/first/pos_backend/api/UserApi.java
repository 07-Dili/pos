package org.dilip.first.pos_backend.api;


import org.dilip.first.pos_backend.constants.UserRole;
import org.dilip.first.pos_backend.dao.UserDao;
import org.dilip.first.pos_backend.entity.UserEntity;
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
}

