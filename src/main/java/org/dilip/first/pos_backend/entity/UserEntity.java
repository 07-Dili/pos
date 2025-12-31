package org.dilip.first.pos_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dilip.first.pos_backend.constants.UserRole;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        indexes = {@Index(name = "idx_users_email", columnList = "email", unique = true)}
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
}

