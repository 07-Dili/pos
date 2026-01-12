package org.dilip.first.pos_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "clients",
        indexes = {
                @Index(name = "idx_client_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_client_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_client_email", columnNames = "email")
        }
)
public class ClientEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;
}
