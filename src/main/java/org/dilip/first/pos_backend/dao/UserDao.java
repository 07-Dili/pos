package org.dilip.first.pos_backend.dao;


import org.dilip.first.pos_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Integer> {
    UserEntity findByEmail(String email);
}

