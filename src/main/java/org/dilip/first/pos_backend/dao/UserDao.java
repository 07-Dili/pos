package org.dilip.first.pos_backend.dao;

import org.dilip.first.pos_backend.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends AbstractDao<UserEntity> {

    public UserDao() {
        super(UserEntity.class);
    }

    static final String FIND_BY_EMAIL_QUERY = "SELECT u FROM UserEntity u WHERE LOWER(u.email) = LOWER(:email)";

    public UserEntity findByEmail(String email) {
        return em.createQuery( FIND_BY_EMAIL_QUERY, UserEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
